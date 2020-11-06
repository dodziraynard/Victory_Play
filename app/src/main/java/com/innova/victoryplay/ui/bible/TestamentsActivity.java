package com.innova.victoryplay.ui.bible;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.innova.victoryplay.R;
import com.innova.victoryplay.adapters.BibleBookAdapter;
import com.innova.victoryplay.databinding.ActivityTestamentsBinding;
import com.innova.victoryplay.models.BibleBook;

import java.util.ArrayList;
import java.util.List;

import static com.innova.victoryplay.utils.Constants.CASHED_RECENT_BOOK1;
import static com.innova.victoryplay.utils.Constants.CASHED_RECENT_BOOK2;
import static com.innova.victoryplay.utils.Constants.SHARED_PREFS_FILE;

public class TestamentsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    BibleBookAdapter allAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTestamentsBinding binding = ActivityTestamentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.all_testaments));

        // Set back arrow
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allAdapter = new BibleBookAdapter(this);
        binding.allRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.allRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.allRecyclerView.setAdapter(allAdapter);
        allAdapter.setData(BibleBook.getBibleBooks());

        refreshRecentAdapterData();
    }

    private void refreshRecentAdapterData() {
        List<BibleBook> books = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        int recent1 = prefs.getInt(CASHED_RECENT_BOOK1, -1);
        int recent2 = prefs.getInt(CASHED_RECENT_BOOK2, -1);

        BibleBook book1 = BibleBook.getBookById(recent1);
        BibleBook book2 = BibleBook.getBookById(recent2);

        if (book1 != null) {
            book1.setRecent(true);
            books.add(book1);
        }

        if (book2 != null) {
            book2.setRecent(true);
            books.add(book2);
        }
        List<BibleBook> newBooks = new ArrayList<>();
        newBooks.addAll(books);
        newBooks.addAll(BibleBook.getBibleBooks());
        allAdapter.setData(newBooks);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshRecentAdapterData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bible_books, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<BibleBook> books = new ArrayList<>();
        for (BibleBook book : BibleBook.getBibleBooks()) {
            if (book.getName().toLowerCase().contains(newText.toLowerCase())) {
                books.add(book);
            }
        }
        allAdapter.setData(books);
        return true;
    }
}