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

    public AppViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    public LiveData<List<EntityBook>> getBooks() {
        MediatorLiveData<List<EntityBook>> liveData = new MediatorLiveData<>();
        liveData.addSource(mRepository.getBooks(), entityBooks -> {
            EpubReader epubReader = new EpubReader();
            for (EntityBook entityBook : entityBooks) {
                try {
                    for (String bookFileName : getApplication().fileList()) {
                        Log.d(TAG, "getBooks: book file: " + bookFileName);
                    }
                    String filepath = Long.toString(entityBook.getId());
                    Log.d(TAG, "getBooks: attempted filepath: " + filepath);
                    InputStream fileInputStream = getApplication().getApplicationContext().openFileInput(filepath);
                    Log.d(TAG, "getBooks: file stream: " + fileInputStream.toString());
                    Log.d(TAG, "getBooks: file available? " + fileInputStream.available());
                    Book book = epubReader.readEpub(fileInputStream);
                    entityBook.setBook(book);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            liveData.postValue(entityBooks);
        });
        return liveData;
    }

    public void AddNewBook(Uri uri) {
        mRepository.addBook(uri);
    }

}
