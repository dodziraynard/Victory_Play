package com.innova.victoryplay.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.victoryplay.R;
import com.innova.victoryplay.models.BibleBook;
import com.innova.victoryplay.ui.bible.SelectorActivity;

import java.util.ArrayList;
import java.util.List;

import static com.innova.victoryplay.utils.Constants.ACTION_CHAPTER;
import static com.innova.victoryplay.utils.Constants.BOOK_ID;

public class BibleBookAdapter extends RecyclerView.Adapter<BibleBookAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<BibleBook> books;

    public BibleBookAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);

        books = new ArrayList<>();
    }

    public void setData(List<BibleBook> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_bible_books, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BibleBookAdapter.ViewHolder holder, int position) {
        BibleBook book = books.get(position);
        holder.tvName.setText(book.getName());

        if (book.isRecent()) {
            holder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.colorContrast2));
        } else {
            if (!book.getOldTestament()) {
                holder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.colorSecondary));
            } else {
                holder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.colorContrast1));
            }
        }

        holder.layout.setOnClickListener(view -> {
//            updateRecentBookPref(mContext, book.getId());
            Intent chapterSelectionActivity = new Intent(mContext, SelectorActivity.class);
            chapterSelectionActivity.setAction(ACTION_CHAPTER);
            chapterSelectionActivity.putExtra(BOOK_ID, book.getId());
            mContext.startActivity(chapterSelectionActivity);
        });
    }

    private int getFromIndex(int length) {
        if (length <= 4)
            return 0;
        else
            return length - 4;
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
