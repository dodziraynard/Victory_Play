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
import com.innova.victoryplay.databinding.ItemPdfBinding;
import com.innova.victoryplay.models.Pdf;
import com.innova.victoryplay.ui.pdf.PdfViewerActivity;

import java.util.ArrayList;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final Context mContext;
    private List<Pdf> pdfs;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void setOnDownloadMenuItemSelectedListener(Pdf pdf);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PdfAdapter(Context context) {
        mContext = context;
        pdfs = new ArrayList<>();
    }

    public void setData(List<Pdf> hourLeaders) {
        this.pdfs = hourLeaders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPdfBinding binding = ItemPdfBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfAdapter.ViewHolder holder, int position) {
        Pdf pdf = pdfs.get(position);
        holder.binding.pdfTitle.setText(pdf.getTitle());
        holder.binding.pdfDescription.setText(pdf.getDescription());

        Glide
                .with(mContext)
                .load(pdf.getImageUrl())
                .placeholder(R.drawable.ic_book)
                .into(holder.binding.pdfImage);
    }

    @Override
    public int getItemCount() {
        return pdfs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemPdfBinding binding;

        public ViewHolder(@NonNull ItemPdfBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            Context context = binding.getRoot().getContext();

            binding.cardView.setOnClickListener(view -> {
                Intent pdfIntent = new Intent(context, PdfViewerActivity.class);
                pdfIntent.putExtra("pdf", pdfs.get(getLayoutPosition()));
                context.startActivity(pdfIntent);
            });

            binding.btnMore.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(view.getContext(), itemView);
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.download) {
                        if (listener != null)
                            listener.setOnDownloadMenuItemSelectedListener(pdfs.get(getLayoutPosition()));
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
