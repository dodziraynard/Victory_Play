package com.innova.victoryplay.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import androidx.annotation.DrawableRes;

import com.innova.victoryplay.R;
import com.innova.victoryplay.models.Audio;

import java.io.IOException;
import java.net.URL;

public final class Samples {

    public static final class Sample {
        public final Uri uri;
        public final String mediaId;
        public final String title;
        public final String description;
        public final int bitmapResource;

        public Sample(
                String uri, String mediaId, String title, String description, int bitmapResource) {
            this.uri = Uri.parse(uri);
            this.mediaId = mediaId;
            this.title = title;
            this.description = description;
            this.bitmapResource = bitmapResource;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    public static final Sample[] SAMPLES = new Sample[]{
            new Sample(
                    "https://storage.googleapis.com/automotive-media/Jazz_In_Paris.mp3",
                    "audio_1",
                    "Jazz in Paris",
                    "Jazz for the masses",
                    R.mipmap.art_1),
            new Sample(
                    "https://storage.googleapis.com/automotive-media/The_Messenger.mp3",
                    "audio_2",
                    "The messenger",
                    "Hipster guide to London",
                    R.mipmap.art_2),
            new Sample(
                    "https://storage.googleapis.com/automotive-media/Talkies.mp3",
                    "audio_3",
                    "Talkies",
                    "If it talks like a duck and walks like a duck.",
                    R.mipmap.art_1),
    };

    public static MediaDescriptionCompat getMediaDescription(Context context, Audio audio) {
        Bundle extras = new Bundle();
//        Bitmap bitmap = null;
//        try {
//            URL url = new URL(audio.getImageUrl());
//            bitmap = BitmapFactory.decodeStream(url.openStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Bitmap bitmap = getBitmap(context, R.mipmap.art_2);
        MediaDescriptionCompat.Builder builder = new MediaDescriptionCompat.Builder();
        builder.setMediaId(String.valueOf(audio.getId()));
        builder.setTitle(audio.getTitle());
        builder.setDescription(audio.getDescription());

        if (bitmap != null) {
            extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap);
            extras.putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap);
            builder.setIconBitmap(bitmap);
            builder.setExtras(extras);
        }
        return builder.build();
//        return new MediaDescriptionCompat.Builder()
//                .setMediaId(String.valueOf(audio.getId()))
//                .setIconBitmap(bitmap)
//                .setTitle(audio.getTitle())
//                .setDescription(audio.getDescription())
//                .setExtras(extras)
//                .build();
    }

    public static Bitmap getBitmap(Context context, @DrawableRes int bitmapResource) {
        return ((BitmapDrawable) context.getResources().getDrawable(bitmapResource)).getBitmap();
    }

}

