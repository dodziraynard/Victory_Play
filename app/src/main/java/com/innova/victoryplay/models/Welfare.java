package com.innova.victoryplay.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = Welfare.WELFARE_TABLE)
public class Welfare implements Parcelable {
    @Ignore
    public static final String WELFARE_TABLE = "welfare_table";

    @PrimaryKey(autoGenerate = true)
    private long id;

    private double amount;
    @SerializedName("time_stamp")
    @ColumnInfo(name = "time_stamp")
    private long timeStamp;
    private int month;
    private int year;

    @SerializedName("user")
    @ColumnInfo(name = "user_id")
    private String userId;

    public Welfare(double amount, long timeStamp, int month, int year, String userId) {
        this.amount = amount;
        this.timeStamp = timeStamp;
        this.month = month;
        this.year = year;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getReadableDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public String getReadableAmount() {
        amount = amount * 100;
        amount = (double) Math.round(amount) / 100;
        return "GH¢" + amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Welfare(Parcel in) {
        amount = in.readDouble();
        timeStamp = in.readLong();
        userId = in.readString();
    }

    @Override
    public String toString() {
        return userId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(amount);
        parcel.writeLong(timeStamp);
        parcel.writeString(userId);
    }

    public static final Creator<Welfare> CREATOR = new Creator<Welfare>() {
        public Welfare createFromParcel(Parcel in) {
            return new Welfare(in);
        }

        public Welfare[] newArray(int size) {
            return new Welfare[size];
        }
    };
}
