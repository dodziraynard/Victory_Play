package com.innova.victoryplay.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.innova.victoryplay.R;
import com.innova.victoryplay.databinding.ItemVideoBinding;
import com.innova.victoryplay.models.Video;
import com.innova.victoryplay.ui.video.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private static final String TAG = "VideoAdapter";
    private final Context mContext;
    private List<Video> videos;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void setOnDownloadMenuItemSelectedListener(Video video);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public VideoAdapter(Context context) {
        mContext = context;
        videos = new ArrayList<>();
    }

    public void setData(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVideoBinding binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.binding.videoTitle.setText(video.getTitle());
        holder.binding.videoDescription.setText(video.getDescription());

        Glide
                .with(mContext)
                .load(video.getImageUrl())
                .fitCenter()
                .placeholder(R.drawable.ic_video)
                .into(holder.binding.videoImage);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemVideoBinding binding;

        public ViewHolder(@NonNull ItemVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            Context context = binding.getRoot().getContext();

            binding.cardView.setOnClickListener(view -> {
                Intent videoPlayerIntent = new Intent(context, VideoPlayerActivity.class);
                videoPlayerIntent.putExtra("videoUrl", videos.get(getLayoutPosition()).getUrl());
                context.startActivity(videoPlayerIntent);
            });

            binding.btnMore.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(view.getContext(), itemView);
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.download) {
                        if (listener != null)
                            listener.setOnDownloadMenuItemSelectedListener(videos.get(getLayoutPosition()));
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