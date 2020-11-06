package com.innova.victoryplay;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.innova.victoryplay.daos.VerseDao;
import com.innova.victoryplay.models.Verse;

import java.util.List;

import static com.innova.victoryplay.utils.Functions.getVersesFromFile;

public class Repository {
    Application application;
    private final VerseDao verseDao;

    public Repository(Application application) {
        VictoryRoomDatabase db = VictoryRoomDatabase.getDatabase(application);
        this.application = application;
        verseDao = db.verseDao();
    }

    // Verse
    public LiveData<List<Verse>> getAllVerses(int book_id, int chapter_id) {
        return verseDao.getAllVerses(book_id, chapter_id);
    }

    public LiveData<Verse> getVerse(long uid) {
        return verseDao.getVerse(uid);
    }

    public LiveData<Integer> getNumberOfChapters(int book_id) {
        return verseDao.getNumberOfChapters(book_id);
    }

    public LiveData<Integer> getNumberOfVerses(int book_id, int chapter_id) {
        return verseDao.getNumberOfVerses(book_id, chapter_id);
    }

    public void loadVersesIntoDatabase() {
        verseDao.deleteAll();
        verseDao.insertVerse(getVersesFromFile(application));
    }

    // Verses
}
