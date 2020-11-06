package com.innova.victoryplay.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.victoryplay.databinding.ItemNoticeBinding;
import com.innova.victoryplay.models.Notice;

import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private List<Notice> notices;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void setOnNoticeSelectedListener(Notice notice);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NoticeAdapter() {
        notices = new ArrayList<>();
    }

    public void setData(List<Notice> notices) {
        this.notices = notices;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoticeBinding binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.ViewHolder holder, int position) {
        Notice notice = notices.get(position);
        holder.binding.tvTitle.setText(notice.getTitle());
        holder.binding.tvTimeStamp.setText(notice.getPublished());

        holder.binding.webView.loadData(notice.getContent(), "text/html; charset=UTF-8", null);
    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemNoticeBinding binding;

        public ViewHolder(@NonNull ItemNoticeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.cdNote.setOnClickListener(v -> {
                if (listener != null)
                    listener.setOnNoticeSelectedListener(notices.get(getLayoutPosition()));
            });
        }
    }
}
