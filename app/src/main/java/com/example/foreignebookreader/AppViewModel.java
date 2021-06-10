package com.example.foreignebookreader;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.foreignebookreader.DbEntities.EntityBook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class AppViewModel extends AndroidViewModel {

    private static final String TAG = "AppViewModel";

    private final AppRepository mRepository;

    private EntityBook mSelectedBook;

    public AppViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    public LiveData<List<EntityBook>> getBooks() {
        return mRepository.getBooks();
    }

    public void AddNewBook(Uri uri, MainActivity.ToastHandler toastHandler) {
        mRepository.addBook(uri, toastHandler);
    }

    public LiveData<EntityBook> getBook(long id) {
        return mRepository.getBook(id);
    }

}
