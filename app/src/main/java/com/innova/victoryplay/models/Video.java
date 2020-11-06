package com.innova.victoryplay.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = Video.VIDEO_TABLE)
public class Video {
    @Ignore
    public static final String VIDEO_TABLE = "video_table";

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

    public Video(String title, String description, String imageUrl) {
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
}
