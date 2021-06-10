package com.example.foreignebookreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class BookReaderAdapter extends ListAdapter<String, BookReaderAdapter.SentenceViewHolder> {

    public static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    };

    protected BookReaderAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public SentenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_page, parent, false);
        return new SentenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SentenceViewHolder holder, int position) {

    }

    static class SentenceViewHolder extends RecyclerView.ViewHolder {

        TextView mPageTextView;

        public SentenceViewHolder(@NonNull View itemView) {
            super(itemView);
            mPageTextView = itemView.findViewById(R.id.tv_page_text);
        }

        public void bindTo(String pageText) {
            mPageTextView.setText(pageText);
        }
    }
}
