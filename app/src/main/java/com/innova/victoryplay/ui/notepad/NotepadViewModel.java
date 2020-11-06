package com.innova.victoryplay.ui.notepad;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.innova.victoryplay.VictoryRoomDatabase;
import com.innova.victoryplay.daos.NoteDao;
import com.innova.victoryplay.models.Note;

import java.util.List;


public class NotepadViewModel extends AndroidViewModel {
    Application application;
    private final NoteDao noteDao;

    public NotepadViewModel(@NonNull Application application) {
        super(application);
        VictoryRoomDatabase db = VictoryRoomDatabase.getDatabase(application);
        this.application = application;
        noteDao = db.noteDao();
    }

    public LiveData<List<Note>> getAllNotes() {
        return noteDao.getAllNotes();
    }

    public LiveData<Note> getNote(long id) {
        return noteDao.getNote(id);
    }

    public long insertNote(Note note) {
        return noteDao.insertNote(note);
    }

    public void updateNote(Note note) {
        noteDao.updateNote(note);
    }

    public void deleteNote(Note note) {
        noteDao.deleteNote(note);
    }

    public void deleteAllNote() {
        noteDao.deleteAll();
    }
}
