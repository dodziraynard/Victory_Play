package com.innova.victoryplay.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.victoryplay.R;
import com.innova.victoryplay.databinding.ItemVerseBinding;
import com.innova.victoryplay.models.Verse;

import java.util.ArrayList;
import java.util.List;

public class VerseAdapter extends RecyclerView.Adapter<VerseAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<Verse> verses;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void setOnVerseSelectedListener(Verse audio);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public VerseAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);

        verses = new ArrayList<>();
    }

    public void setData(List<Verse> hourLeaders) {
        this.verses = hourLeaders;
        notifyDataSetChanged();
    }


    public int getCurrentPosition(){
        return getCurrentPosition();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = mLayoutInflater.inflate(R.layout.item_verse, parent, false);
        ItemVerseBinding binding = ItemVerseBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VerseAdapter.ViewHolder holder, int position) {
        Verse verse = verses.get(position);
        holder.binding.tvVerseNumber.setText(String.valueOf(verse.getVerseId()));
        holder.binding.tvVerse.setText(verse.getVerse());
       }

    @Override
    public int getItemCount() {
        return verses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemVerseBinding binding;

        public ViewHolder(@NonNull ItemVerseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.layout.setOnClickListener(view -> {
                if (listener != null)
                    listener.setOnVerseSelectedListener(verses.get(getLayoutPosition()));
            });
        }
    }
}
