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

import java.util.ArrayList;
import java.util.List;

public class SelectorAdapter extends RecyclerView.Adapter<SelectorAdapter.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private List<Integer> numbers;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void setOnItemSelectedListener(Integer number);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public SelectorAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);

        numbers = new ArrayList<>();
    }

    public void setData(List<Integer> numbers) {
        this.numbers = numbers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_chapters_and_verses, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectorAdapter.ViewHolder holder, int position) {
        Integer number = numbers.get(position);
        holder.tvName.setText(number.toString());
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            tvName = itemView.findViewById(R.id.tv_name);

            layout.setOnClickListener(view -> {
                if (listener != null)
                    listener.setOnItemSelectedListener(numbers.get(getLayoutPosition()));
            });
        }
    }
}
