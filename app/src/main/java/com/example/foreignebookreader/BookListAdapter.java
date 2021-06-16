package com.example.foreignebookreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foreignebookreader.DbEntities.EntityBook;

import java.io.IOException;

import nl.siegmann.epublib.domain.Book;

public class BookListAdapter extends ListAdapter<EntityBook, BookListAdapter.BookViewHolder> {

    public BookListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    public static final DiffUtil.ItemCallback<EntityBook> DIFF_CALLBACK = new DiffUtil.ItemCallback<EntityBook>() {

        //TODO check DiffUtil subclass for BookListAdapter is correct

        @Override
        public boolean areItemsTheSame(@NonNull EntityBook oldItem, @NonNull EntityBook newItem) {
            return oldItem.getChecksum().equals(newItem.getChecksum());
        }

        @Override
        public boolean areContentsTheSame(@NonNull EntityBook oldItem, @NonNull EntityBook newItem) {
            return oldItem.equals(newItem);
        }
    };

    static class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mTitle;
        TextView mLanguage;
        EntityBook mEntityBook;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_book_title);
            mLanguage = itemView.findViewById(R.id.tv_book_language);
            mImageView = itemView.findViewById(R.id.img_book_cover);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), BookReaderActivity.class);
                intent.putExtra(MainActivity.EXTRA_ID, mEntityBook.getId());
                itemView.getContext().startActivity(intent);
            });
        }

        void bindTo(EntityBook entityBook) {
            mEntityBook = entityBook;
            mTitle.setText(entityBook.getTitle());
            String langCode = entityBook.getLanguageCode();
            if (EntityBook.UNKNOWN.equals(langCode)) {
                mLanguage.setText("Language: Unknown");
            } else {
                String language = Languages.getLanguageFromCode(langCode);
                mLanguage.setText("Language: " + language);
            }
            if (entityBook.getBook() != null) {
                Book book = entityBook.getBook();
                displayBookCover(book);
            }
        }

        private void displayBookCover(Book book) {
            if (book.getCoverImage() != null) {
                try {
                    Bitmap cover = BitmapFactory.decodeStream(book.getCoverImage().getInputStream());
                    mImageView.setImageBitmap(cover);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
