package com.innova.victoryplay.utils;

import android.net.Uri;

import com.innova.victoryplay.R;
import com.innova.victoryplay.models.Audio;
import com.innova.victoryplay.models.Note;
import com.innova.victoryplay.models.Pdf;
import com.innova.victoryplay.models.Verse;
import com.innova.victoryplay.models.Video;

import java.util.ArrayList;
import java.util.List;

public class DummyData {
    private final String TAG = this.getClass().getSimpleName();

    public static List<Audio> getDummyAudios() {
        Uri url = Uri.parse("android.resource://" + "com.innova.victoryplay" + "/" + R.raw.song);
        List<Audio> audios;
        audios = new ArrayList<>();
        audios.add(new Audio("Song one", "145 Description", url.toString()));
        audios.add(new Audio("Helegah Raynard Dodzi", "145 Description", url.toString()));
        audios.add(new Audio("Helegah Raynard Dodzi", "145 Description", url.toString()));
        audios.add(new Audio("Helegah Raynard Dodzi", "145 Description", url.toString()));
        audios.add(new Audio("Helegah Raynard Dodzi", "145 Description", url.toString()));
        audios.add(new Audio("Helegah Raynard Dodzi", "145 Description", url.toString()));
        audios.add(new Audio("Helegah Raynard Dodzi", "145 Description", url.toString()));
        audios.add(new Audio("Helegah Raynard Dodzi", "145 Description", url.toString()));
        return audios;
    }

    public static List<Video> getDummyVideos() {
        List<Video> videos;
        videos = new ArrayList<>();
        videos.add(new Video("Video", "145 Description", "url"));
        videos.add(new Video("Helegah Raynard Dodzi", "145 Description", "url"));
        videos.add(new Video("Helegah Raynard Dodzi", "145 Description", "url"));
        videos.add(new Video("Helegah Raynard Dodzi", "145 Description", "url"));
        videos.add(new Video("Helegah Raynard Dodzi", "145 Description", "url"));
        videos.add(new Video("Helegah Raynard Dodzi", "145 Description", "url"));
        videos.add(new Video("Helegah Raynard Dodzi", "145 Description", "url"));
        videos.add(new Video("Helegah Raynard Dodzi", "145 Description", "url"));
        return videos;
    }

    public static List<Pdf> getDummyPdfs() {
        List<Pdf> pdfs;
        pdfs = new ArrayList<>();
        pdfs.add(new Pdf("Pdf", "145 Description", "url"));
        pdfs.add(new Pdf("Helegah Raynard Dodzi", "145 Description", "url"));
        pdfs.add(new Pdf("Helegah Raynard Dodzi", "145 Description", "url"));
        pdfs.add(new Pdf("Helegah Raynard Dodzi", "145 Description", "url"));
        pdfs.add(new Pdf("Helegah Raynard Dodzi", "145 Description", "url"));
        pdfs.add(new Pdf("Helegah Raynard Dodzi", "145 Description", "url"));
        pdfs.add(new Pdf("Helegah Raynard Dodzi", "145 Description", "url"));
        pdfs.add(new Pdf("Helegah Raynard Dodzi", "145 Description", "url"));
        return pdfs;
    }

    public static List<Note> getDummyNotes() {
        List<Note> notes;
        notes = new ArrayList<>();
        notes.add(new Note("Note", "145 Description", 1604102400000L));
        notes.add(new Note("Helegah Raynard Dodzi", "145 Description", 220448575));
        notes.add(new Note("Helegah Raynard Dodzi", "145 Description", 220448575));
        notes.add(new Note("Helegah Raynard Dodzi", "145 DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription", 220448575));
        notes.add(new Note("Helegah Raynard Dodzi", "145 Description", 220448575));
        notes.add(new Note("Helegah Raynard Dodzi", "145 Description", 220448575));
        notes.add(new Note("Helegah Raynard Dodzi", "145 Description", 220448575));
        notes.add(new Note("Helegah Raynard Dodzi", "145 Description", 220448575));
        return notes;
    }

    public static List<Verse> getDummyVerses() {
        List<Verse> verses;
        verses = new ArrayList<>();
        verses.add(new Verse(2, 3, 4, "And there was no bread in all the land; for the famine was very sore, so that the land of Egypt and all the land of Canaan fainted by reason of the famine."));
        verses.add(new Verse(2, 3, 4, "And Joseph gathered up all the money that was found in the land of Egypt, and in the land of Canaan, for the corn which they bought: and Joseph brought the money into Pharaoh's house."));
        verses.add(new Verse(2, 3, 4, "And when money failed in the land of Egypt, and in the land of Canaan, all the Egyptians came unto Joseph, and said, Give us bread: for why should we die in your presence? for the money fails."));
        verses.add(new Verse(2, 3, 4, "And Joseph said, Give your cattle; and I will give you for your cattle, if money fail."));
        verses.add(new Verse(2, 3, 4, "And they brought their cattle unto Joseph: and Joseph gave them bread in exchange for horses, and for the flocks, and for the cattle of the herds, and for the asses: and he fed them with bread for all their cattle for that year."));
        verses.add(new Verse(2, 4, 4, "When that year was ended, they came unto him the second year, and said unto him, We will not hide it from my lord, how that our money is spent; my lord also has our herds of cattle; there is not ought left in the sight of my lord, but our bodies, and our lands:"));
        verses.add(new Verse(2, 4, 4, "Wherefore shall we die before yours eyes, both we and our land? buy us and our land for bread, and we and our land will be servants unto Pharaoh: and give us seed, that we may live, and not die, that the land be not desolate."));
        verses.add(new Verse(2, 4, 4, "Wherefore shall we die before yours eyes, both we and our land? buy us and our land for bread, and we and our land will be servants unto Pharaoh: and give us seed, that we may live, and not die, that the land be not desolate."));
        return verses;
    }
}
