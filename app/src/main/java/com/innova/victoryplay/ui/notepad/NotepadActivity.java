package com.innova.victoryplay.ui.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.innova.victoryplay.R;
import com.innova.victoryplay.adapters.NoteAdapter;
import com.innova.victoryplay.models.Note;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotepadActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static final String NOTE = "note";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    NoteAdapter adapter;
    private NotepadViewModel viewModel;
    private List<Note> notesLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.notepad));

        notesLists = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(NotepadViewModel.class);

        adapter = new NoteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        viewModel.getAllNotes().observe(this, notes -> {
            notesLists = notes;
            adapter.setData(notes);
        });

        adapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(NotepadActivity.this, NewNoteActivity.class);
            intent.putExtra(NOTE, note);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            showDeleteAllSnackbar();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllSnackbar() {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                R.string.delete_all_confirmation,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.delete, v -> viewModel.deleteAllNote());
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notepad, menu);
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
        List<Note> notes = new ArrayList<>();
        for (Note note : notesLists) {
            if (note.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                    note.getContent().toLowerCase().contains(newText.toLowerCase())) {
                notes.add(note);
            }
        }
        adapter.setData(notes);
        return true;
    }

    @OnClick(R.id.floatingActionButton)
    public void launchNewNote() {
        Intent intent = new Intent(this, NewNoteActivity.class);
        startActivity(intent);
    }
}