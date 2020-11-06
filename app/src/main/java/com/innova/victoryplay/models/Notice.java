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

@Entity(tableName = Notice.NOTE_TABLE)
public class Notice implements Parcelable {
    @Ignore
    public static final String NOTE_TABLE = "notice_table";

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String content;
    @ColumnInfo(name = "time_stamp")
    private long timeStamp;

    public Notice(String title, String content, long timeStamp) {
        this.title = title;
        this.content = content;
        this.timeStamp = timeStamp;
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

    private Notice(Parcel in) {
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

    public static final Creator<Notice> CREATOR = new Creator<Notice>() {
        public Notice createFromParcel(Parcel in) {
            return new Notice(in);
        }

        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };
}
