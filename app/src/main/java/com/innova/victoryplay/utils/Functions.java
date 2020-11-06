package com.innova.victoryplay.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.innova.victoryplay.models.Verse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.innova.victoryplay.utils.Constants.CASHED_RECENT_BOOK1;
import static com.innova.victoryplay.utils.Constants.CASHED_RECENT_BOOK2;
import static com.innova.victoryplay.utils.Constants.FULL_USERNAME;
import static com.innova.victoryplay.utils.Constants.SHARED_PREFS_FILE;
import static com.innova.victoryplay.utils.Constants.USER_ID;

public class Functions {
    private final String TAG = this.getClass().getSimpleName();

    public static String getReadableDate(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static String getFileFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static List<Verse> readBibleJsonFromFile(Context context) {
        return getVersesFromXml(context);
//
//        String jsonFileString = getFileFromAssets(context, "bible.json");
//        List<Verse> verseList = new ArrayList<>();
//        try {
//            JSONParser parser = new JSONParser();
//            Object json = parser.parse(jsonFileString);
//
//            JSONArray books = (JSONArray) json;
//            for (int i = 0; i < books.size(); i++) {
//                JSONObject book = (JSONObject) books.get(i);
//                JSONArray chapters = (JSONArray) book.get("Chapter");
//                int bookId = i + 1;
//
//                for (int j = 0; j < chapters.size(); j++) {
//                    JSONObject chapter = (JSONObject) chapters.get(j);
//                    JSONArray verses = (JSONArray) chapter.get("Verse");
//                    int chapterId = j + 1;
//                    for (int k = 0; k < verses.size(); k++) {
//                        int verseId = k + 1;
//                        JSONObject verseObj = (JSONObject) verses.get(k);
//                        String verseText = verseObj.get("Verse").toString();
//                        Verse verse = new Verse(bookId, chapterId, verseId, verseText);
//                        verseList.add(verse);
//                    }
//                }
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return verseList;
    }

    public static List<Verse> getVersesFromXml(Context context) {
        List<Verse> verses = new ArrayList<>();
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open("niv.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList bookList = doc.getElementsByTagName("b");

            // Getting books
            for (int i = 0; i < bookList.getLength(); i++) {
                Node bookNode = bookList.item(i);
                if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element bookElement = (Element) bookNode;
                    String book = bookElement.getAttribute("n");

                    // Getting chapters
                    NodeList chapterList = bookElement.getElementsByTagName("c");
                    for (int j = 0; j < chapterList.getLength(); j++) {
                        Node chapterNode = chapterList.item(j);
                        if (chapterNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element chapterElement = (Element) chapterNode;
                            String chapter = chapterElement.getAttribute("n");

                            //Getting verses
                            NodeList verseList = chapterElement.getElementsByTagName("v");
                            for (int k = 0; k < verseList.getLength(); k++) {
                                Node verseNode = verseList.item(k);
                                if (verseNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element verseElement = (Element) verseNode;
                                    String verse = verseElement.getTextContent();
                                    Verse verseObject = new Verse(i + 1, j + 1, k + 1, verse);
                                    verses.add(verseObject);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return verses;
    }

    public static List<Verse> getVersesFromFile(Context context) {
        return readBibleJsonFromFile(context);
    }

    public static void updateRecentBookPref(Context context, int noteId) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        int recent1 = prefs.getInt(CASHED_RECENT_BOOK1, -1);
        int recent2;

        recent2 = recent1;
        recent1 = noteId;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(CASHED_RECENT_BOOK1, recent1);
        editor.putInt(CASHED_RECENT_BOOK2, recent2);
        editor.apply();
    }

    public static String getFullUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        return prefs.getString(FULL_USERNAME, "");
    }

    public static long getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        return prefs.getLong(USER_ID, -1);
    }
}
