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

    AppViewModel mViewModel;
    BookReaderActivity mParentActivity;

    protected BookReaderAdapter(AppViewModel viewModel, BookReaderActivity parentActivity) {
        super(DIFF_CALLBACK);
        mViewModel = viewModel;
        mParentActivity = parentActivity;
    }

    @NonNull
    @Override
    public SentenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_page, parent, false);
        return new SentenceViewHolder(view, mViewModel, mParentActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull SentenceViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    static class SentenceViewHolder extends RecyclerView.ViewHolder {

        TextView mPageTextView;
        TextView mTranslationTextView;
        AppViewModel mViewModel;
        BookReaderActivity mParentActivity;

        public SentenceViewHolder(@NonNull View itemView, AppViewModel viewModel, BookReaderActivity parentActivity) {
            super(itemView);
            mPageTextView = itemView.findViewById(R.id.tv_page_text);
            mTranslationTextView = itemView.findViewById(R.id.tv_page_translation);
            mViewModel = viewModel;
            mParentActivity = parentActivity;
        }

        public void bindTo(String pageText) {
            mTranslationTextView.setText("");
            mTranslationTextView.setVisibility(View.INVISIBLE);
            mPageTextView.setText(pageText);
            itemView.setOnClickListener(v -> {
                if (mTranslationTextView.getVisibility() == View.INVISIBLE) {
                    mViewModel.getTranslation(pageText).observe(mParentActivity, translation -> {
                        mTranslationTextView.setText(translation);
                        mTranslationTextView.setVisibility(View.VISIBLE);
                    });
                } else {
                    mTranslationTextView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
