package com.innova.victoryplay.ui.bible;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.victoryplay.R;
import com.innova.victoryplay.adapters.VerseAdapter;
import com.innova.victoryplay.models.BibleBook;
import com.innova.victoryplay.ui.MainActivity;
import com.innova.victoryplay.ui.notepad.NewNoteActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.TextUtils.join;
import static com.innova.victoryplay.models.BibleBook.getBookIdByName;
import static com.innova.victoryplay.utils.Constants.BOOK_ID;
import static com.innova.victoryplay.utils.Constants.CHAPTER_ID;
import static com.innova.victoryplay.utils.Constants.CONTINUE_BIBLE_NOTE;
import static com.innova.victoryplay.utils.Constants.VERSE_ID;
import static com.innova.victoryplay.utils.Functions.updateRecentBookPref;

public class BibleActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    VerseAdapter adapter;
    BibleViewModel viewModel;
    private BibleBook book;
    private int chapterId;
    private int verseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(0f);

        int bookId = getIntent().getIntExtra(BOOK_ID, -1);
        chapterId = getIntent().getIntExtra(CHAPTER_ID, -1);
        verseId = getIntent().getIntExtra(VERSE_ID, -1);


        // Get selected text from text processor intents
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            CharSequence text = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            if (text != null) {
                try {
                    String quotation = text.toString();
                    verseId = Integer.parseInt(quotation.split(":")[1]);
                    chapterId = Integer.parseInt(quotation.split(":")[0].split(" ")[quotation.split(":")[0].split(" ").length - 1]);
                    String[] bookNameInfo = Arrays.copyOfRange(quotation.split(":")[0].split(" "), 0, quotation.split(":")[0].split(" ").length - 1);
                    List<String> strings = Arrays.asList(bookNameInfo);
                    String bookName = join(" ", strings);
                    bookId = getBookIdByName(bookName);

                    if (bookId == 0) {
                        navigateToTestamentsActivity();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    navigateToTestamentsActivity();
                    return;
                }
            }
        }

        book = BibleBook.getBookById(bookId);
        updateRecentBookPref(this, book.getId());

        setTitle(book.getName() + " " + chapterId + ":" + verseId);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(BibleViewModel.class);

        adapter = new VerseAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        viewModel.getAllVerses(bookId, chapterId).observe(this, verses -> {
            adapter.setData(verses);
            recyclerView.smoothScrollToPosition(verseId - 1);
        });
    }

    private void navigateToTestamentsActivity() {
        Intent bibleIntent = new Intent(this, TestamentsActivity.class);
        bibleIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(bibleIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_take_note:
                Intent noteIntent = new Intent(this, NewNoteActivity.class);
                noteIntent.setAction(CONTINUE_BIBLE_NOTE);
                noteIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                noteIntent.putExtra("NOTE_TITLE", "Bible Note");
                noteIntent.putExtra("NOTE_INTRO", book.getName() + " " + chapterId + ":" + verseId);
                startActivity(noteIntent);
                return true;
            case R.id.action_content:
                Intent bibleIntent = new Intent(this, TestamentsActivity.class);
                bibleIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bibleIntent);
                return true;
            case R.id.action_close:
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bible, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}