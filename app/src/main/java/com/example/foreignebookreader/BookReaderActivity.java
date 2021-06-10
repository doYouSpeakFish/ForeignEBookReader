package com.example.foreignebookreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.foreignebookreader.DbEntities.EntityBook;

public class BookReaderActivity extends AppCompatActivity {

    AppViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);

        ViewModelProvider.AndroidViewModelFactory viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);

        Intent intent = getIntent();
        long id = intent.getLongExtra(MainActivity.EXTRA_ID, -1L);
        if (id == -1L) {
            finish();
        }
        LiveData<EntityBook> entityBookLiveData = mViewModel.getBook(id);

        RecyclerView recyclerView = findViewById(R.id.rv_book_reader);
    }
}