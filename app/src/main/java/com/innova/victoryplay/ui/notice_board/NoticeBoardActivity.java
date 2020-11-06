package com.innova.victoryplay.ui.notice_board;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.innova.victoryplay.R;
import com.innova.victoryplay.adapters.NoticeAdapter;
import com.innova.victoryplay.databinding.ActivityNoticeBoardBinding;
import com.innova.victoryplay.models.Notice;

import java.util.ArrayList;
import java.util.List;

public class NoticeBoardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNoticeBoardBinding binding = ActivityNoticeBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.notice_board));

        List<Notice> notices = new ArrayList<>();
        String html = "<h1 style=\"color:blue;\">A Blue Heading</h1>\n";
        for (int i = 0; i < 10; i++) {

            notices.add(new Notice("Title "+1, "content " + html, 29348348));
        }

        NoticeAdapter adapter = new NoticeAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setData(notices);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            showDeleteAllSnackbar();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllSnackbar() {
//        Snackbar snackbar = Snackbar.make(
//                findViewById(android.R.id.content),
//                R.string.delete_all_confirmation,
//                Snackbar.LENGTH_LONG)
//                .setAction(R.string.delete, v -> viewModel.deleteAllNote());
//        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notice_board, menu);
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

        return false;
    }
}