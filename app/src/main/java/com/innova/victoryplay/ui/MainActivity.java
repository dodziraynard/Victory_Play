package com.innova.victoryplay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.innova.victoryplay.databinding.ActivityMainBinding;
import com.innova.victoryplay.ui.authentication.LoginActivity;
import com.innova.victoryplay.ui.bible.TestamentsActivity;
import com.innova.victoryplay.ui.audio.AudioActivity;
import com.innova.victoryplay.ui.notepad.NotepadActivity;
import com.innova.victoryplay.ui.notice_board.NoticeBoardActivity;
import com.innova.victoryplay.ui.pdf.PdfActivity;
import com.innova.victoryplay.ui.momo.MomoActivity;
import com.innova.victoryplay.ui.video.VideoActivity;
import com.innova.victoryplay.ui.welfare.WelfareActivity;
import com.innova.victoryplay.utils.SwipeListener;

import static com.innova.victoryplay.utils.Constants.ACTION_PRAYER_REQUEST;
import static com.innova.victoryplay.utils.Constants.ACTION_TESTIMONY;
import static com.innova.victoryplay.utils.Functions.getVersesFromXml;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getVersesFromXml(this);

        binding.userSettings.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        binding.cdAudio.setOnClickListener(view -> {
            startActivity(new Intent(this, AudioActivity.class));
        });

        binding.cdVideo.setOnClickListener(view -> {
            startActivity(new Intent(this, VideoActivity.class));
        });

        binding.cdPdf.setOnClickListener(view -> {
            startActivity(new Intent(this, PdfActivity.class));
        });

        binding.cvWelfare.setOnClickListener(view -> {
            startActivity(new Intent(this, WelfareActivity.class));
        });

        binding.cvNotepad.setOnClickListener(view -> {
            startActivity(new Intent(this, NotepadActivity.class));
        });

        binding.cvBible.setOnClickListener(view -> {
            startActivity(new Intent(this, TestamentsActivity.class));
        });

        binding.cvPrayerRequest.setOnClickListener(view -> {
            Intent intent = new Intent(this, TextActivity.class);
            intent.setAction(ACTION_PRAYER_REQUEST);
            startActivity(intent);
        });

        binding.cvTestimony.setOnClickListener(view -> {
            Intent intent = new Intent(this, TextActivity.class);
            intent.setAction(ACTION_TESTIMONY);
            startActivity(intent);
        });

        binding.cdDonations.setOnClickListener(view -> {
            startActivity(new Intent(this, MomoActivity.class));
        });

        SwipeListener swipeListener = new SwipeListener();
        swipeListener.setHandler(new SwipeListener.SwipeHandler() {
            @Override
            public void onRight() {
                Toast.makeText(MainActivity.this, "Swiped", Toast.LENGTH_SHORT).show();
            }
        });
    }
}