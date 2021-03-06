package com.example.foreignebookreader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class BookReaderAdapter extends ListAdapter<String, BookReaderAdapter.SentenceViewHolder> {
    
    private static final String TAG = "BookReaderAdapter";

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

        ConstraintLayout mPageLayout;
        TextView mPageTextView;
        TextView mTranslationTextView;
        AppViewModel mViewModel;
        BookReaderActivity mParentActivity;
        View mItemView;
        LiveData<String> mTranslationLiveData;

        public SentenceViewHolder(@NonNull View itemView, AppViewModel viewModel, BookReaderActivity parentActivity) {
            super(itemView);
            mPageTextView = itemView.findViewById(R.id.tv_page_text);
            mTranslationTextView = itemView.findViewById(R.id.tv_page_translation);
            mPageLayout = itemView.findViewById(R.id.cl_page);
            mViewModel = viewModel;
            mItemView = itemView;
            mParentActivity = parentActivity;
        }

        public void bindTo(String pageText) {
            mTranslationTextView.setVisibility(View.INVISIBLE);
            mTranslationTextView.setText("");
            mPageTextView.setText(pageText);
            mPageLayout.setOnClickListener(v -> {
                if (mTranslationTextView.getVisibility() == View.INVISIBLE) {
                    mTranslationLiveData = mViewModel.getTranslation(pageText);
                    mTranslationLiveData.observe(mParentActivity, translation -> {
                        mTranslationTextView.setText(translation);
                        mTranslationTextView.setVisibility(View.VISIBLE);
                        mTranslationLiveData.removeObservers(mParentActivity);
                    });
                } else {
                    mTranslationTextView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
