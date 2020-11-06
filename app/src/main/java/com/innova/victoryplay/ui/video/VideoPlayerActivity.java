package com.innova.victoryplay.ui.video;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.innova.victoryplay.R;
import com.innova.victoryplay.databinding.ActivityVideoPlayerBinding;

public class VideoPlayerActivity extends AppCompatActivity {
    private static final String TAG = "VideoPlayerActivity";
    private ActivityVideoPlayerBinding binding;
    private SimpleExoPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String videoUrl = getIntent().getStringExtra("videoUrl");
        initializePlayer();
        play(videoUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(new DefaultTrackSelector(this))
                .build();
        binding.playerView.setPlayer(player);
        player.setPlayWhenReady(true);
    }

    private void play(String url) {
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                this, Util.getUserAgent(this, getString(R.string.app_name)));

//        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(RawResourceDataSource.buildRawResourceUri(R.raw.video));
        MediaItem mediaItem = MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
        player.prepare();
    }
}