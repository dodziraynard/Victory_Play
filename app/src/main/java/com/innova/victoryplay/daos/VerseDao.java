package com.innova.victoryplay.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.innova.victoryplay.models.Verse;

import java.util.List;

import static com.innova.victoryplay.models.Verse.VERSE_TABLE;

@Dao
public interface VerseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertVerse(Verse verse);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVerse(List<Verse> verses);

    @Query("DELETE FROM " + VERSE_TABLE)
    void deleteAll();

    @Query("SELECT * from " + VERSE_TABLE + " WHERE book_id = :book_id AND chapter_id = :chapter_id" + " ORDER BY verse_id ASC")
    LiveData<List<Verse>> getAllVerses(int book_id, int chapter_id);

    @Query("SELECT * FROM " + VERSE_TABLE + " WHERE uid = :uid")
    LiveData<Verse> getVerse(long uid);

    @Query("SELECT  COUNT(DISTINCT chapter_id) FROM " + VERSE_TABLE + " WHERE book_id = :book_id")
    LiveData<Integer> getNumberOfChapters(int book_id);

    @Query("SELECT COUNT(DISTINCT verse_id) from " + VERSE_TABLE + " WHERE book_id = :book_id AND chapter_id = :chapter_id")
    LiveData<Integer> getNumberOfVerses(int book_id, int chapter_id);

}