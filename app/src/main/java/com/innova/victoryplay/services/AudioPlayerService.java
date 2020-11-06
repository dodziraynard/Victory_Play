package com.innova.victoryplay.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.innova.victoryplay.R;
import com.innova.victoryplay.models.Audio;
import com.innova.victoryplay.ui.audio.AudioActivity;
import com.innova.victoryplay.utils.Samples;

import static com.google.android.exoplayer2.ui.PlayerNotificationManager.ACTION_NEXT;
import static com.google.android.exoplayer2.ui.PlayerNotificationManager.ACTION_PAUSE;
import static com.google.android.exoplayer2.ui.PlayerNotificationManager.ACTION_PLAY;
import static com.google.android.exoplayer2.ui.PlayerNotificationManager.ACTION_PREVIOUS;
import static com.google.android.exoplayer2.ui.PlayerNotificationManager.ACTION_STOP;
import static com.innova.victoryplay.utils.Constants.GET_PLAYER_POSITION;
import static com.innova.victoryplay.utils.Constants.MEDIA_SESSION_TAG;
import static com.innova.victoryplay.utils.Constants.PLAYBACK_CHANNEL_ID;
import static com.innova.victoryplay.utils.Constants.PLAYBACK_NOTIFICATION_ID;
import static com.innova.victoryplay.utils.Constants.PLAYER_POSITION_SENT;
import static com.innova.victoryplay.utils.Constants.UPDATE_PLAYER_STATUS;
import static com.innova.victoryplay.utils.Samples.SAMPLES;

public class AudioPlayerService extends Service implements AudioManager.OnAudioFocusChangeListener {

    private SimpleExoPlayer player;
    private PlayerNotificationManager playerNotificationManager;
    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;
    private MediaControllerCompat.TransportControls transportControls;
    private Context context;
    private AudioManager audioManager;
    private Audio currentAudio;

    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    private long playerPosition = 0;

    private boolean isPlaying = false;
    private DefaultDataSourceFactory dataSourceFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        callStateListener();
        registerReceivers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        removeAudioFocus();
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        mediaSession.release();
        mediaSessionConnector.setPlayer(null);
        playerNotificationManager.setPlayer(null);
        if (player != null) {
            player.release();
            player = null;
        }
        stopForeground(true);
        unRegisterReceivers();
    }

    // Binder given to clients
    private final IBinder iBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!requestAudioFocus()) {
            stopSelf();
        }

        if (player == null) {
            initExoPlayer();
        }

        handleIncomingActions(intent);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        onDestroy();
    }

    @Override
    public void onAudioFocusChange(int focusState) {
        //Invoked when the audio focus of the system is updated.
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (player == null) initExoPlayer();
                else if (!player.isPlaying()) player.play();
                player.setVolume(1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                if (player != null && player.isPlaying()) {
                    player.stop();
                    player.release();
                }
                player = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (player.isPlaying()) player.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (player.isPlaying()) player.setVolume(0.1f);
                break;
        }
    }

    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (player != null) {
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (player != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    private boolean initExoPlayer() {
        player = new SimpleExoPlayer.Builder(context)
                .setTrackSelector(new DefaultTrackSelector(context))
                .build();

        dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, getString(R.string.app_name)));

//        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
//        for (Samples.Sample sample : SAMPLES) {
//            MediaItem mediaItem = MediaItem.fromUri(sample.uri);
////            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
//            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(RawResourceDataSource.buildRawResourceUri(R.raw.song));
//            concatenatingMediaSource.addMediaSource(mediaSource);
//        }
//
//        player.setMediaSource(concatenatingMediaSource);
//        player.prepare();
        player.setPlayWhenReady(true);

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                AudioPlayerService.this.isPlaying = isPlaying;
                sendPlayerStatus(isPlaying);
            }

        });

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context,
                PLAYBACK_CHANNEL_ID,
                R.string.playback_channel_name,
                R.string.playback_channel_description,
                PLAYBACK_NOTIFICATION_ID,
                new PlayerNotificationManager.MediaDescriptionAdapter() {

                    @Override
                    public String getCurrentContentTitle(Player player) {
                        return currentAudio.getTitle();
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        Intent intent = new Intent(context, AudioActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        return currentAudio.getDescription();
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                        return Samples.getBitmap(
                                context, SAMPLES[player.getCurrentWindowIndex()].bitmapResource);
                    }
                },
                new PlayerNotificationManager.NotificationListener() {
                    @Override
                    public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                        stopSelf();
                        stopForeground(true);
                    }

                    @Override
                    public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                        startForeground(notificationId, notification);
                        if (!ongoing) {
                            stopForeground(false);
                        }
                    }
                }
        );

        playerNotificationManager.setPlayer(player);

        mediaSession = new MediaSessionCompat(context, MEDIA_SESSION_TAG);
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        transportControls = mediaSession.getController().getTransportControls();

        playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken());

        mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(mediaSession) {
            @Override
            public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
                return Samples.getMediaDescription(context, currentAudio);
            }
        });
        mediaSessionConnector.setPlayer(player);
        return true;
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }

    private void registerReceivers() {
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);

        IntentFilter getProgressFilter = new IntentFilter(GET_PLAYER_POSITION);
        registerReceiver(sendProgressRequest, getProgressFilter);
    }

    private void unRegisterReceivers() {
        try {
            unregisterReceiver(becomingNoisyReceiver);
            unregisterReceiver(sendProgressRequest);
        } catch (IllegalArgumentException ignored) {

        }
    }

    // Receivers
    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseMedia();
        }
    };

    private BroadcastReceiver sendProgressRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (player.isPlaying())
                postPlayerPosition();
        }
    };

    public void pauseMedia() {
        if (player != null) {
            player.pause();
            playerPosition = player.getCurrentPosition();
        }
    }

    public void playAudio(Audio audio) {
        currentAudio = audio;
        if (player != null) {
            MediaItem mediaItem = MediaItem.fromUri(audio.getUrl());
            player.setMediaItem(mediaItem);
            player.seekTo(0);
            player.prepare();
            player.play();
        }
    }

    public void resumeMedia() {
        if (player != null) {
            player.seekTo(playerPosition);
            player.play();
        }
    }

    public long getDuration() {
        if (player != null) {
            return player.getDuration();
        }
        return 0;
    }

    public void seekTo(long position) {
        Log.d("TAG", "seekTo: " + position);
        Log.d("TAG", "dr: " + getDuration());
        if (player != null) {
            player.seekTo(position);
            player.play();
        }
    }

    public boolean isPlaying() {
        if (player != null)
            return isPlaying;
        else return false;
    }

    public void postPlayerPosition() {
        Intent broadcastIntent = new Intent(PLAYER_POSITION_SENT);
        broadcastIntent.putExtra("playerPosition", player.getCurrentPosition());
        sendBroadcast(broadcastIntent);
    }

    public void sendPlayerStatus(boolean isPlaying) {
        Intent broadcastIntent = new Intent(UPDATE_PLAYER_STATUS);
        broadcastIntent.putExtra("isPlaying", isPlaying);
        sendBroadcast(broadcastIntent);
    }

    public class LocalBinder extends Binder {
        public AudioPlayerService getService() {
            return AudioPlayerService.this;
        }
    }
}
