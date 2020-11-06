package com.innova.victoryplay.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = Verse.VERSE_TABLE)
public class Verse {
    @Ignore
    public static final String VERSE_TABLE = "verse_table";

    @PrimaryKey(autoGenerate = true)
    long uid;

    @ColumnInfo(name = "verse_id")
    private int verseId;

    @ColumnInfo(name = "book_id")
    private int bookId;

    @ColumnInfo(name = "chapter_id")
    private int chapterId;

    private String verse;

    public Verse(int bookId, int chapterId, int verseId, String verse) {
        this.verseId = verseId;
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.verse = verse;
    }

    public long getUid() {
        return uid;
    }

    public int getVerseId() {
        return verseId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerseId(int verseId) {
        this.verseId = verseId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
