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

import java.util.Calendar;
import java.util.List;

public class BookReaderActivity extends AppCompatActivity {

    private final String TAG = "BookReaderActivity";

    AppViewModel mViewModel;
    EntityBook mEntityBook;
    BookReaderAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);

        ViewModelProvider.AndroidViewModelFactory viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.rv_book_reader);
        mAdapter = new BookReaderAdapter(mViewModel, this);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = mLayoutManager.findFirstVisibleItemPosition();
                    mEntityBook.setCurrentLocation(position);
                    mViewModel.updateEntityBook(mEntityBook);
                    // TODO set current location in the viewModel
                }

            }
        });
        new PagerSnapHelper().attachToRecyclerView(recyclerView);

        Intent intent = getIntent();
        long id = intent.getLongExtra(MainActivity.EXTRA_ID, -1L);
        if (id == -1L) {
            finish();
        }

        loadBook(id);
    }

    private void loadBook(long id) {
        AppViewModel.BookData bookData = mViewModel.getBook(id);

        bookData.getBookLiveData().observe(this, entityBook -> {
            if (entityBook != null) {
                mEntityBook = entityBook;
                if (entityBook.getLanguageCode().equals(EntityBook.UNKNOWN)) {
                    launchLanguageSelectDialog();
                } else {
                    Log.d(TAG, "loadBook: language code = " + entityBook.getLanguageCode());
                }
            }
        });

        bookData.getPagesLiveData().observe(this, pages -> {
            mAdapter.submitList(pages);
            mLayoutManager.scrollToPosition(mEntityBook.getCurrentLocation());
        });
    }

    private void launchLanguageSelectDialog() {
        Log.d(TAG, "loadBook: language unknown. launching language select dialog");
        String[] languages = Languages.getLanguages().keySet().toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What language is this book?");
        builder.setItems(languages, (dialog, which) -> {
            Log.d(TAG, "loadBook: setting language to " + languages[which]);
            mEntityBook.setLanguageCode(Languages.getLanguages().get(languages[which]));
            mViewModel.updateEntityBook(mEntityBook);
            Log.d(TAG, "loadBook: language selected. reloading pages");
        });
        builder.show();
    }

}