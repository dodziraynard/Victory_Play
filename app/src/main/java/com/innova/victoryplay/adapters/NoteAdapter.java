package com.innova.victoryplay.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.victoryplay.R;
import com.innova.victoryplay.models.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<Note> notes;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void setOnNoteSelectedListener(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NoteAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);

        notes = new ArrayList<>();
    }

    public void setData(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        Note video = notes.get(position);
        holder.tvTitle.setText(video.getTitle());
        holder.content.setText(video.getContent());
        holder.tvTimestamp.setText(video.getPublished());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView content;
        private final TextView tvTimestamp;
        private final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_content);
            tvTimestamp = itemView.findViewById(R.id.tv_time_stamp);
            cardView = itemView.findViewById(R.id.cd_note);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.setOnNoteSelectedListener(notes.get(getLayoutPosition()));
                }
            });
        }
    }
}
