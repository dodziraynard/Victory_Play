package com.innova.victoryplay.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.victoryplay.databinding.ItemWelfareBinding;
import com.innova.victoryplay.models.Welfare;

import java.util.ArrayList;
import java.util.List;

public class WelfareAdapter extends RecyclerView.Adapter<WelfareAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private List<Welfare> welfares;


    public WelfareAdapter() {
        welfares = new ArrayList<>();
    }

    public void setData(List<Welfare> welfares) {
        this.welfares = welfares;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWelfareBinding binding = ItemWelfareBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WelfareAdapter.ViewHolder holder, int position) {
        Welfare audio = welfares.get(position);
        holder.binding.dateTime.setText(audio.getReadableDate());
        holder.binding.tvWelfareAmount.setText(audio.getReadableAmount());
    }

    @Override
    public int getItemCount() {
        return welfares.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemWelfareBinding binding;

        public ViewHolder(@NonNull ItemWelfareBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
