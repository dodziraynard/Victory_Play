package com.innova.victoryplay.ui.pdf;

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
import com.innova.victoryplay.adapters.PdfAdapter;
import com.innova.victoryplay.databinding.ActivityPdfBinding;
import com.innova.victoryplay.models.Pdf;
import com.innova.victoryplay.services.DownloadService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfActivity extends AppCompatActivity {
    PdfAdapter adapter;
    private PdfActivityViewModel viewModel;
    List<Pdf> pdfs;

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
    };
    private static final int REQUEST_PERMISSIONS_CODE = 32;
    private Pdf pdfToDownload;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PdfActivityViewModel.class);
        ActivityPdfBinding binding = ActivityPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle("PDFs");

        adapter = new PdfAdapter(this);
        pdfs = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(adapter);

        viewModel.getPdfList().observe(this, pdfs -> {
            this.pdfs = pdfs;
            adapter.setData(pdfs);
        });

        viewModel.getIsLoadingPdf().observe(this, aBoolean -> {
            if (aBoolean) {
                binding.progressCircular.setVisibility(View.VISIBLE);
            } else {
                binding.progressCircular.setVisibility(View.GONE);
            }
        });

        viewModel.loadPdf("");
        adapter.setOnItemClickListener(pdf -> downloadPdfInBackground(pdf));
    }

    private void downloadPdfInBackground(Pdf pdf) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionsDenied()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS_CODE);
            return;
        }
        String path = "/" + getString(R.string.app_name);
        final File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        final String fullPath = Environment.getExternalStorageDirectory() + path + "/" + pdf.getTitle();
        if (!new File(fullPath).exists()) {
            if (this != null) {
                Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show();
                this.startService(DownloadService.getDownloadService(this, pdf.getUrl(), folder.getPath(), pdf.getTitle()));
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
                viewModel.loadPdf(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Pdf> filteredPdfs = new ArrayList<>();
                for (Pdf pdf : pdfs) {
                    if (pdf.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                            pdf.getDescription().toLowerCase().contains(newText.toLowerCase())) {
                        filteredPdfs.add(pdf);
                        adapter.setData(filteredPdfs);
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
            downloadPdfInBackground(pdfToDownload);
        }
    }
}