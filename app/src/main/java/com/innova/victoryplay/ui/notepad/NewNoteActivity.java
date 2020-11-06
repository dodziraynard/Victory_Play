package com.innova.victoryplay.ui.notepad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.innova.victoryplay.R;
import com.innova.victoryplay.custom_view.LinedEditText;
import com.innova.victoryplay.models.Note;
import com.innova.victoryplay.ui.bible.TestamentsActivity;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.innova.victoryplay.ui.notepad.NotepadActivity.NOTE;
import static com.innova.victoryplay.utils.Constants.CONTINUE_BIBLE_NOTE;
import static com.innova.victoryplay.utils.Constants.CURRENT_BIBLE_NOTE_ID;
import static com.innova.victoryplay.utils.Constants.SHARED_PREFS_FILE;
import static com.innova.victoryplay.utils.Functions.getReadableDate;

public class NewNoteActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.tv_time_stamp)
    TextView tvTimeStamp;

    @BindView(R.id.et_content)
    LinedEditText edContent;

    @BindView(R.id.ed_title)
    TextInputEditText edTitle;

    private Menu menu;
    private Note note;
    private boolean continueBibleNote;
    private SharedPreferences prefs;
    private NotepadViewModel viewModel;
    private boolean isCanceling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.note));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewModel = new ViewModelProvider(this).get(NotepadViewModel.class);

        continueBibleNote = Objects.equals(getIntent().getAction(), CONTINUE_BIBLE_NOTE);

        if (continueBibleNote) {
            prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
            long noteId = prefs.getLong(CURRENT_BIBLE_NOTE_ID, -1);
            String newContent = getIntent().getStringExtra("NOTE_INTRO");

            viewModel.getNote(noteId).observe(this, note -> {
                if (note != null) {
                    this.note = note;
                    tvTimeStamp.setText(note.getPublished());
                    edTitle.setText(note.getTitle());
                    edContent.setText(note.getContent());
                    edContent.append("\n" + newContent);
                } else {
                    String title = getIntent().getStringExtra("NOTE_TITLE");
                    edTitle.setText(title);
                    edContent.setText(newContent);
                }
            });
        } else {
            if (getIntent().getExtras() != null)
                note = getIntent().getExtras().getParcelable(NOTE);
            if (note != null) {
                tvTimeStamp.setText(note.getPublished());
                edContent.setText(note.getContent());
                edTitle.setText(note.getTitle());
            } else {
                tvTimeStamp.setText(getReadableDate(Calendar.getInstance().getTimeInMillis()));
            }
        }

        edContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (menu != null) {
                    menu.findItem(R.id.action_save).setVisible(!editable.toString().isEmpty());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isCanceling)
            saveNote();
    }

    public void saveNote() {
        String title = edTitle.getText().toString().isEmpty() ? "Title" : edTitle.getText().toString();
        String content = edContent.getText().toString();
        if (content.isEmpty()) {
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show();
            return;
        }
        if (note != null) {
            note.setTitle(title);
            note.setContent(content);
            note.setTimeStamp(Calendar.getInstance().getTimeInMillis());
            viewModel.updateNote(note);
        } else {
            note = new Note(title, content, Calendar.getInstance().getTimeInMillis());
            if (continueBibleNote) {
                note.setFromBible(true);
                long id = viewModel.insertNote(note);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(CURRENT_BIBLE_NOTE_ID, id);
                editor.apply();
                return;
            }
            viewModel.insertNote(note);
        }
    }

    private void showDeleteSnackbar(Note note) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Sure you want to delete note?", Snackbar.LENGTH_LONG)
                .setAction("DELETE", v -> {
                    viewModel.deleteNote(note);
                    isCanceling = true;
                    finish();
                });
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        menu.findItem(R.id.action_save).setVisible(false);

        if (note == null)
            menu.findItem(R.id.action_delete).setVisible(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_bible:
                Intent intent = new Intent(this, TestamentsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_save:
                if (continueBibleNote) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(CURRENT_BIBLE_NOTE_ID, note.getId());
                    editor.apply();
                }
                saveNote();
                onBackPressed();
                return true;
            case R.id.action_delete:
                if (note != null) {
                    showDeleteSnackbar(note);
                }
                return true;
            case R.id.action_cancel:
                isCanceling = true;
                onBackPressed();
        }
        return false;
    }
}