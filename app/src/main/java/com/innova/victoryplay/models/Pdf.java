package com.innova.victoryplay.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = Pdf.PDF_TABLE)
public class Pdf implements Parcelable {
    @Ignore
    public static final String PDF_TABLE = "pdf_table";

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    @SerializedName("desc")
    private String description;

    @SerializedName("file")
    private String url;

    @SerializedName("image")
    @ColumnInfo(name = "image_url")
    private String imageUrl;

    public Pdf(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
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

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Pdf(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
    }

    public static final Parcelable.Creator<Pdf> CREATOR = new Parcelable.Creator<Pdf>() {
        public Pdf createFromParcel(Parcel in) {
            return new Pdf(in);
        }

        public Pdf[] newArray(int size) {
            return new Pdf[size];
        }
    };
}
