package com.example.foreignebookreader;

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

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_book_title);
            mImageView = itemView.findViewById(R.id.img_book_cover);
        }

        void bindTo(EntityBook entityBook) {
            mTitle.setText(entityBook.getTitle());
            if (entityBook.getBook() != null) {
                Book book = entityBook.getBook();
                setCover(book);
            }
        }

        private void setCover(Book book) {
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
