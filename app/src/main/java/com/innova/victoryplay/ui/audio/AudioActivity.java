package com.innova.victoryplay.ui.audio;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.innova.victoryplay.R;
import com.innova.victoryplay.adapters.AudioAdapter;
import com.innova.victoryplay.databinding.ActivityAudioBinding;
import com.innova.victoryplay.databinding.BottomSheetAudioPlayerBinding;
import com.innova.victoryplay.models.Audio;
import com.innova.victoryplay.services.AudioPlayerService;
import com.innova.victoryplay.services.DownloadService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.innova.victoryplay.utils.Constants.GET_PLAYER_POSITION;
import static com.innova.victoryplay.utils.Constants.PLAYER_POSITION_SENT;
import static com.innova.victoryplay.utils.Constants.UPDATE_PLAYER_STATUS;


public class AudioActivity extends AppCompatActivity {
    AudioAdapter adapter;
    Audio currentAudio;
    List<Audio> audios;
    private Handler handler;
    private AudioActivityViewModel viewModel;

    private AudioPlayerService player;
    private BottomSheetAudioPlayerBinding layoutBottomSheetBinding;

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
    };
    private static final int REQUEST_PERMISSIONS_CODE = 14;
    private Audio audioToDownload;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAudioBinding binding = ActivityAudioBinding.inflate(getLayoutInflater());
        layoutBottomSheetBinding = BottomSheetAudioPlayerBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.audios));

        adapter = new AudioAdapter(this);
        handler = new Handler();
        audios = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(AudioActivityViewModel.class);

        viewModel.getBinder().observe(this, binder -> {
            if (binder != null) {
                player = binder.getService();
                if (player.isPlaying()) {
                    layoutBottomSheetBinding.imgPlay.setImageResource(R.drawable.exo_controls_pause);
                    //TODO: get current audio from the viewmodel
                    playerStatusChanged(true);
                }
            } else {
                player = null;
            }
        });

        viewModel.getIsLoadingAudio().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressCircular.setVisibility(View.VISIBLE);
            } else {
                binding.progressCircular.setVisibility(View.GONE);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this,
                        layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void setOnAudioSelectedListener(Audio audio) {
                String path = getString(R.string.app_name);
                final String fullPath = Environment.getExternalStorageDirectory() + path + "/" + audio.getTitle();
                if (new File(fullPath).exists()) {
                    audio.setUrl(fullPath);
                }
                player.playAudio(audio);
                currentAudio = audio;
            }

            @Override
            public void setOnDownloadMenuItemSelectedListener(Audio audio) {
                audioToDownload = audio;
                downloadAudioInBackground(audio);
            }
        });

        viewModel.getAudioList().observe(this, audios -> {
            this.audios = audios;
            adapter.setData(audios);
        });
        registerReceivers();

        viewModel.loadAudio("");
        registerClickListeners();
    }

    private void downloadAudioInBackground(Audio audio) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionsDenied()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS_CODE);
            return;
        }
        String path = "/" + getString(R.string.app_name);
        final File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        final String fullPath = Environment.getExternalStorageDirectory() + path + "/" + audio.getTitle();
        if (!new File(fullPath).exists()) {
            Toast.makeText(this, "Download", Toast.LENGTH_SHORT).show();
            this.startService(DownloadService.getDownloadService(this, audio.getUrl(), folder.getPath(), audio.getTitle()));

        }
    }

    private void registerClickListeners() {
        layoutBottomSheetBinding.imgPlay.setOnClickListener(view -> playPauseAudio());
        layoutBottomSheetBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int i, boolean b) {
                if (b && player != null) {
                    player.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {

            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent broadcastIntent = new Intent(GET_PLAYER_POSITION);
            sendBroadcast(broadcastIntent);
            handler.postDelayed(runnable, 1000);
        }
    };

    private void playerStatusChanged(boolean isPlaying) {
        updateBottomSheet();

        if (viewModel.getHandlerHasCallback()) {
            handler.removeCallbacks(runnable);
        }
        if (isPlaying) {
            layoutBottomSheetBinding.imgPlay.setImageResource(R.drawable.exo_controls_pause);
            handler.postDelayed(runnable, 1000);
            viewModel.setHandlerHasCallback(true);
        } else {
            layoutBottomSheetBinding.imgPlay.setImageResource(R.drawable.exo_controls_play);
            handler.removeCallbacks(runnable);
            viewModel.setHandlerHasCallback(false);
        }
    }

    private void updateBottomSheet() {
        if(currentAudio == null) return;
        long duration = player.getDuration() / 1000;
        if (duration < 0) return;
        String min = String.format("%02d", duration / 60);
        String sec = String.format("%02d", duration % 60);
        String duText = min + ":" + sec;
        layoutBottomSheetBinding.tvTotalDuration.setText(duText);
        layoutBottomSheetBinding.seekBar.setMax((int) duration * 1000);
        layoutBottomSheetBinding.tvTitle.setText(currentAudio.getTitle());
        layoutBottomSheetBinding.tvDescription.setText(currentAudio.getDescription());
    }

    void playPauseAudio() {
        if (player != null && player.isPlaying()) {
            player.pauseMedia();
        } else if (player != null) {
            player.resumeMedia();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.loadAudio(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Audio> filteredAudios = new ArrayList<>();
                for (Audio audio : audios) {
                    if (audio.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                            audio.getDescription().toLowerCase().contains(newText.toLowerCase())) {
                        filteredAudios.add(audio);
                        adapter.setData(filteredAudios);
                    }
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (viewModel.getBinder() != null) {
            try {
                unbindService(viewModel.getServiceConnection());
            } catch (IllegalArgumentException ignore) {
            }
        }
        unRegisterReceivers();
    }

    private boolean permissionsDenied() {
        for (String permission : PERMISSIONS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsDenied() && this != null) {
            Toast.makeText(this, "Perm Denied", Toast.LENGTH_LONG).show();
            ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
        } else {
            downloadAudioInBackground(audioToDownload);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startService();
    }

    private void startService() {
        Intent serviceIntent = new Intent(this, AudioPlayerService.class);
        this.startService(serviceIntent);
        bindService();
    }

    private void bindService() {
        Intent serviceIntent = new Intent(this, AudioPlayerService.class);
        bindService(serviceIntent, viewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    private void registerReceivers() {
        IntentFilter statusFilter = new IntentFilter(UPDATE_PLAYER_STATUS);
        IntentFilter positionFilter = new IntentFilter(PLAYER_POSITION_SENT);
        this.registerReceiver(statusUpdateReceiver, statusFilter);
        this.registerReceiver(playerPositionReceiver, positionFilter);
    }

    private void unRegisterReceivers() {
        this.unregisterReceiver(statusUpdateReceiver);
        this.unregisterReceiver(playerPositionReceiver);
    }

    private final BroadcastReceiver statusUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isPlaying = intent.getBooleanExtra("isPlaying", false);
            playerStatusChanged(isPlaying);
        }
    };

    private final BroadcastReceiver playerPositionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long pos = intent.getLongExtra("playerPosition", -1) / 1000;
            if (pos < 0) return;
            String min = String.format("%02d", pos / 60);
            String sec = String.format("%02d", pos % 60);
            layoutBottomSheetBinding.seekBar.setProgress(((int) pos) * 1000);

            String elText = min + ":" + sec;
            layoutBottomSheetBinding.tvDurationCurrent.setText(elText);
        }
    };
}