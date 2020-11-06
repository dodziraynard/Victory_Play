package com.innova.victoryplay.adapters;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.innova.victoryplay.R;
import com.innova.victoryplay.databinding.ItemAudioBinding;
import com.innova.victoryplay.models.Audio;

import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final Context mContext;
    private List<Audio> audios;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void setOnAudioSelectedListener(Audio audio);
        void setOnDownloadMenuItemSelectedListener(Audio audio);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AudioAdapter(Context context) {
        mContext = context;
        audios = new ArrayList<>();
    }

    public void setData(List<Audio> audios) {
        this.audios = audios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAudioBinding binding = ItemAudioBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioAdapter.ViewHolder holder, int position) {
        Audio audio = audios.get(position);
        holder.binding.tvName.setText(audio.getTitle());
        holder.binding.tvDescription.setText(audio.getDescription());

        Glide
                .with(mContext)
                .load(audio.getImageUrl())
                .fitCenter()
                .placeholder(R.drawable.ic_audio)
                .into(holder.binding.audioImage);
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemAudioBinding binding;

        public ViewHolder(@NonNull ItemAudioBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            binding.layout.setOnClickListener(view -> {
                if (listener != null)
                    listener.setOnAudioSelectedListener(audios.get(getLayoutPosition()));
            });

            binding.moreBtn.setOnClickListener(view->{
                PopupMenu popup = new PopupMenu(view.getContext(), itemView);

                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.download) {
                        if (listener != null)
                            listener.setOnDownloadMenuItemSelectedListener(audios.get(getLayoutPosition()));
                        return true;
                    }
                    return false;
                });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    popup.setGravity(Gravity.RIGHT);
                }
                popup.inflate(R.menu.file_option_menu);
                popup.show();
            });
        }
    }
}
