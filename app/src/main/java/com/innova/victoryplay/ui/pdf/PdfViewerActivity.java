package com.innova.victoryplay.ui.pdf;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.innova.victoryplay.R;
import com.innova.victoryplay.databinding.ActivityPdfViewerBinding;
import com.innova.victoryplay.models.Pdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PdfViewerActivity extends AppCompatActivity {
    private static final String TAG = "PdfViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.innova.victoryplay.databinding.ActivityPdfViewerBinding binding = ActivityPdfViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Pdf pdf = getIntent().getParcelableExtra("pdf");
        String path = "/" + getString(R.string.app_name);
        final String fullPath = Environment.getExternalStorageDirectory() + path + "/" + pdf.getTitle();

        File file = new File(fullPath);
        Uri uri = Uri.fromFile(file);
        binding.pdfView.fromUri(uri)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .onPageChange((page, pageCount) -> {
                    page++;
                    Toast.makeText(getApplicationContext(), page + "/" + pageCount, Toast.LENGTH_LONG).show();
                })
                .onError(t -> {
                    onErrorListener(t);
                })
                .onPageError((page, t) -> onPageErrorListener())
                .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .load();
    }

    private void onErrorListener(Throwable t) {
        Toast.makeText(this, "Can't load PDF: File not downloaded", Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    private void onPageErrorListener() {
        Toast.makeText(this, "Page Error", Toast.LENGTH_SHORT).show();
    }
}