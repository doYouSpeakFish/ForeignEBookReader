package com.example.foreignebookreader;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.foreignebookreader.DbEntities.EntityBook;
import com.example.foreignebookreader.DbEntities.EntityTranslation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRepository {

    private final AppDao mDao;
    private final ExecutorService mExecutorService;

    public AppRepository(Application application) {
        AppDB appDB = AppDB.getDatabase(application);
        mDao = appDB.appDao();
        mExecutorService = Executors.newFixedThreadPool(4);
    }

    public void insertBook(EntityBook entityBook) {
        mExecutorService.execute(() -> {
            mDao.insertBook(entityBook);
        });
    }

    public void insertTranslation(EntityTranslation entityTranslation) {
        mExecutorService.execute(() -> {
            mDao.insertTranslation(entityTranslation);
        });
    }

    public LiveData<List<EntityBook>> getBooks() {
        return mDao.getBooks();
    }

    public LiveData<EntityTranslation> getTranslation(String text, String sourceLang, String targetLang) {
        //TODO get translation from server if not in DB
        return mDao.getTranslation(text, sourceLang, targetLang);
    }

}
