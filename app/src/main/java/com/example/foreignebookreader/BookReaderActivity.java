package com.example.foreignebookreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.foreignebookreader.DbEntities.EntityBook;

import java.util.List;

public class BookReaderActivity extends AppCompatActivity {

    private final String TAG = "BookReaderActivity";

    AppViewModel mViewModel;
    EntityBook mEntityBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);

        ViewModelProvider.AndroidViewModelFactory viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.rv_book_reader);
        BookReaderAdapter adapter = new BookReaderAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = layoutManager.findFirstVisibleItemPosition();
                    mEntityBook.setCurrentLocation(position);
                    mViewModel.updateEntityBook(mEntityBook);
                }

            }
        });
        new PagerSnapHelper().attachToRecyclerView(recyclerView);

        Intent intent = getIntent();
        long id = intent.getLongExtra(MainActivity.EXTRA_ID, -1L);
        if (id == -1L) {
            finish();
        }

        mViewModel.getBook(id).observe(this, entityBook -> {
            mEntityBook = entityBook;
            if (entityBook.getLanguageCode().equals(EntityBook.UNKNOWN)) {
                // TODO ask user for language
            }
            mViewModel.getPages(entityBook).observe(this, pages -> {
                adapter.submitList(pages);
                layoutManager.scrollToPosition(entityBook.getCurrentLocation());
            });
        });
    }
}