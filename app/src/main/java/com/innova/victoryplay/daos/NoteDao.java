package com.innova.victoryplay.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.innova.victoryplay.models.Note;

import java.util.List;

import static com.innova.victoryplay.models.Note.NOTE_TABLE;

@Dao
public interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long  insertNote(Note note);

    @Query("DELETE FROM " + NOTE_TABLE)
    void deleteAll();

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

    @Query("SELECT * from " + NOTE_TABLE + " ORDER BY id ASC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM " + NOTE_TABLE + " WHERE id = :id")
    LiveData<Note> getNote(long id);

}