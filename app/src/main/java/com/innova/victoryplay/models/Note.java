package com.innova.victoryplay.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = Note.NOTE_TABLE)
public class Note implements Parcelable {
    @Ignore
    public static final String NOTE_TABLE = "note_table";

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String content;
    @ColumnInfo(name = "time_stamp")
    private long timeStamp;

    @ColumnInfo(name = "is_from_bible")
    private boolean isFromBible;

    public Note(String title, String content, long timeStamp) {
        this.title = title;
        this.content = content;
        this.timeStamp = timeStamp;
        isFromBible = false;
    }

    public boolean isFromBible() {
        return isFromBible;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFromBible(boolean fromBible) {
        isFromBible = fromBible;
    }

    public String getPublished() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Note(Parcel in) {
        id = in.readLong();
        title = in.readString();
        content = in.readString();
        timeStamp = in.readLong();
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeLong(timeStamp);
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
