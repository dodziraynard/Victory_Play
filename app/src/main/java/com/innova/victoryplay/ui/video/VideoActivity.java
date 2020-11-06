package com.innova.victoryplay.ui.video;

import android.Manifest;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.innova.victoryplay.R;
import com.innova.victoryplay.adapters.VideoAdapter;
import com.innova.victoryplay.databinding.ActivityVideoBinding;
import com.innova.victoryplay.models.Video;
import com.innova.victoryplay.services.DownloadService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class VideoActivity extends AppCompatActivity {
    private static final String TAG = "VideoFragment";
    private VideoAdapter adapter;
    private List<Video> videos;

    private VideoActivityViewModel viewModel;
    private Video videoToDownload;

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
    };
    private static final int REQUEST_PERMISSIONS_CODE = 44;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new VideoAdapter(this);
        videos = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(VideoActivityViewModel.class);
        ActivityVideoBinding binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle("Videos");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(adapter);

        viewModel.getVideoList().observe(this, videos -> {
            this.videos = videos;
            adapter.setData(videos);
        });

        viewModel.getIsLoadingVideo().observe(this, aBoolean -> {
            if (aBoolean) {
                binding.progressCircular.setVisibility(View.VISIBLE);
            } else {
                binding.progressCircular.setVisibility(View.GONE);
            }
        });

        viewModel.loadVideo("");

        adapter.setOnItemClickListener(video -> {
            videoToDownload = video;
            downloadVideoInBackground(videoToDownload);
        });
    }

    private void downloadVideoInBackground(Video video) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionsDenied()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS_CODE);
            return;
        }
        String path = "/" + getString(R.string.app_name);
        final File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        final String fullPath = Environment.getExternalStorageDirectory() + path + "/" + video.getTitle();
        if (!new File(fullPath).exists()) {
            if (this != null) {
                Toast.makeText(this, "Download", Toast.LENGTH_SHORT).show();
                this.startService(DownloadService.getDownloadService(this, video.getUrl(), folder.getPath(), video.getTitle()));
            }
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
                viewModel.loadVideo(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Video> filteredVideos = new ArrayList<>();
                for (Video video : videos) {
                    if (video.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                            video.getDescription().toLowerCase().contains(newText.toLowerCase())) {
                        filteredVideos.add(video);
                        adapter.setData(filteredVideos);
                    }
                }
                return true;
            }
        });

        return true;
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
        if (permissionsDenied()) {
            Toast.makeText(this, "Perm Denied", Toast.LENGTH_LONG).show();
            ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
        } else {
            downloadVideoInBackground(videoToDownload);
        }
    }

}