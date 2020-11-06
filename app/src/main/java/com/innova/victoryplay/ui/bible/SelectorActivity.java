package com.innova.victoryplay.ui.bible;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.victoryplay.R;
import com.innova.victoryplay.adapters.SelectorAdapter;
import com.innova.victoryplay.models.BibleBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.innova.victoryplay.utils.Constants.ACTION_CHAPTER;
import static com.innova.victoryplay.utils.Constants.ACTION_VERSE;
import static com.innova.victoryplay.utils.Constants.BOOK_ID;
import static com.innova.victoryplay.utils.Constants.CHAPTER_ID;
import static com.innova.victoryplay.utils.Constants.VERSE_ID;

public class SelectorActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_context)
    TextView tvContext;

    SelectorAdapter adapter;
    private BibleBook book;
    private List<Integer> numbers;
    BibleViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int bookId = intent.getIntExtra(BOOK_ID, 1);
        book = BibleBook.getBookById(bookId);

        adapter = new SelectorAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 6));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(number -> {
            if (Objects.equals(intent.getAction(), ACTION_CHAPTER)) {
                Intent verseSelectionActivity = new Intent(SelectorActivity.this, SelectorActivity.class);
                verseSelectionActivity.setAction(ACTION_VERSE);
                verseSelectionActivity.putExtra(BOOK_ID, book.getId());
                verseSelectionActivity.putExtra(CHAPTER_ID, number);
                startActivity(verseSelectionActivity);
            } else {
                Intent readIntent = new Intent(SelectorActivity.this, BibleActivity.class);
                int chapterId = intent.getIntExtra(CHAPTER_ID, -1);

                readIntent.putExtra(BOOK_ID, book.getId());
                readIntent.putExtra(CHAPTER_ID, chapterId);
                readIntent.putExtra(VERSE_ID, number);
                startActivity(readIntent);
                finish();
            }
        });

        viewModel = new ViewModelProvider(this).get(BibleViewModel.class);

        numbers = new ArrayList<>();
        if (Objects.equals(intent.getAction(), ACTION_CHAPTER)) {
            setTitle("Content: " + book.getName());
            viewModel.getNumberOfChapters(book.getId()).observe(this, (Integer integer) -> {
                if (integer == null || integer == 0) {
                    viewModel.loadVersesIntoDatabase();
                    onRestart();
                } else {
                    for (int i = 1; i <= integer; i++) {
                        numbers.add(i);
                    }
                    adapter.setData(numbers);
                }
            });
        } else {
            tvContext.setText(R.string.verse);
            int chapter_id = intent.getIntExtra(CHAPTER_ID, 1);
            setTitle("Content: " + book.getName() + " " + chapter_id);

            viewModel.getNumberOfVerses(book.getId(), chapter_id).observe(this, (Integer integer) -> {
                for (int i = 1; i <= integer; i++) {
                    numbers.add(i);
                }
                adapter.setData(numbers);
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}