package com.innova.victoryplay.models;

import java.util.ArrayList;
import java.util.List;

public final class BibleBook {
    private int id;
    private String name;
    private boolean isOldTestament;
    private boolean isRecent;

    public BibleBook(int id, String name, Boolean testament) {
        this.id = id;
        this.name = name;
        isOldTestament = testament;
        isRecent = false;
    }

    public int getId() {
        return id;
    }

    public boolean isRecent() {
        return isRecent;
    }

    public String getName() {
        return name;
    }

    public Boolean getOldTestament() {
        return isOldTestament;
    }

    public void setRecent(boolean recent) {
        isRecent = recent;
    }

    public static List<BibleBook> getBibleBooks() {
        ArrayList<BibleBook> books = new ArrayList<>();

        books.add(new BibleBook(1, "Genesis", true));
        books.add(new BibleBook(2, "Exodus", true));
        books.add(new BibleBook(3, "Leviticus", true));
        books.add(new BibleBook(4, "Numbers", true));
        books.add(new BibleBook(5, "Deuteronomy", true));
        books.add(new BibleBook(6, "Joshua", true));
        books.add(new BibleBook(7, "Judges", true));
        books.add(new BibleBook(8, "Ruth", true));
        books.add(new BibleBook(9, "1 Samuel", true));
        books.add(new BibleBook(10, "2 Samuel", true));
        books.add(new BibleBook(11, "1 Kings", true));
        books.add(new BibleBook(12, "2 Kings", true));
        books.add(new BibleBook(13, "1 Chronicles", true));
        books.add(new BibleBook(14, "2 Chronicles", true));
        books.add(new BibleBook(15, "Ezra", true));
        books.add(new BibleBook(16, "Nehemiah", true));
        books.add(new BibleBook(17, "Esther", true));
        books.add(new BibleBook(18, "Job", true));
        books.add(new BibleBook(19, "Psalms", true));
        books.add(new BibleBook(20, "Proverbs", true));
        books.add(new BibleBook(21, "Ecclesiastes", true));
        books.add(new BibleBook(22, "Song of Solomon", true));
        books.add(new BibleBook(23, "Isaiah", true));
        books.add(new BibleBook(24, "Jeremiah", true));
        books.add(new BibleBook(25, "Lamentations", true));
        books.add(new BibleBook(26, "Ezekiel", true));
        books.add(new BibleBook(27, "Daniel", true));
        books.add(new BibleBook(28, "Hosea", true));
        books.add(new BibleBook(29, "Joel", true));
        books.add(new BibleBook(30, "Amos", true));
        books.add(new BibleBook(31, "Obadiah", true));
        books.add(new BibleBook(32, "Jonah", true));
        books.add(new BibleBook(33, "Micah", true));
        books.add(new BibleBook(34, "Nahum", true));
        books.add(new BibleBook(35, "Habakkuk", true));
        books.add(new BibleBook(36, "Zephaniah", true));
        books.add(new BibleBook(37, "Haggai", true));
        books.add(new BibleBook(38, "Zechariah", true));
        books.add(new BibleBook(39, "Malachi", true));
        books.add(new BibleBook(40, "Matthew", false));
        books.add(new BibleBook(41, "Mark", false));
        books.add(new BibleBook(42, "Luke", false));
        books.add(new BibleBook(43, "John", false));
        books.add(new BibleBook(44, "Acts", false));
        books.add(new BibleBook(45, "Romans", false));
        books.add(new BibleBook(46, "1 Corinthians", false));
        books.add(new BibleBook(47, "2 Corinthians", false));
        books.add(new BibleBook(48, "Galatians", false));
        books.add(new BibleBook(49, "Ephesians", false));
        books.add(new BibleBook(50, "Philippians", false));
        books.add(new BibleBook(51, "Colossians", false));
        books.add(new BibleBook(52, "1 Thessalonians", false));
        books.add(new BibleBook(53, "2 Thessalonians", false));
        books.add(new BibleBook(54, "1 Timothy", false));
        books.add(new BibleBook(55, "2 Timothy", false));
        books.add(new BibleBook(56, "Titus", false));
        books.add(new BibleBook(57, "Philemon", false));
        books.add(new BibleBook(58, "Hebrews", false));
        books.add(new BibleBook(59, "James", false));
        books.add(new BibleBook(60, "1 Peter", false));
        books.add(new BibleBook(61, "2 Peter", false));
        books.add(new BibleBook(62, "1 John", false));
        books.add(new BibleBook(63, "2 John", false));
        books.add(new BibleBook(64, "3 John", false));
        books.add(new BibleBook(65, "Jude", false));
        books.add(new BibleBook(66, "Revelation", false));

        return books;
    }

    public static BibleBook getBookById(int id) {
        for (BibleBook book : BibleBook.getBibleBooks()) {
            if (book.id == id) {
                return book;
            }
        }
        return null;
    }

    public static int getBookIdByName(String name) {
        for (BibleBook book : BibleBook.getBibleBooks()) {
            if (book.getName().equals(name)) {
                return book.getId();
            }
        }
        return 0;
    }
}
