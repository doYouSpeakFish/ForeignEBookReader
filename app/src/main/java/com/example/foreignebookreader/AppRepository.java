package com.example.foreignebookreader;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.foreignebookreader.DbEntities.EntityBook;
import com.example.foreignebookreader.DbEntities.EntityTranslation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.util.IOUtil;

public class AppRepository {

    private final static String TAG = "AppRepository";

    private final AppDao mDao;
    private final ExecutorService mExecutorService;
    private final Application mApplication;

    public AppRepository(Application application) {
        AppDB appDB = AppDB.getDatabase(application);
        mDao = appDB.appDao();
        mExecutorService = Executors.newFixedThreadPool(4);
        mApplication = application;
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
        MediatorLiveData<List<EntityBook>> liveData = new MediatorLiveData<>();
        liveData.addSource(mDao.getBooks(), entityBooks -> {
            mExecutorService.execute(() -> {
                EpubReader epubReader = new EpubReader();
                for (EntityBook entityBook : entityBooks) {
                    Book book = openBook(entityBook.getId(), epubReader);
                    entityBook.setBook(book);
                }
                liveData.postValue(entityBooks);
            });
        });
        return liveData;
    }

    public LiveData<EntityTranslation> getTranslation(String text, String sourceLang, String targetLang) {
        //TODO get translation from server if not in DB
        return mDao.getTranslation(text, sourceLang, targetLang);
    }

    public void addBook(Uri uri, MainActivity.ToastHandler toastHandler) {
        mExecutorService.execute(() -> {
            try {
                EpubReader epubReader = new EpubReader();
                InputStream inputStream = mApplication.getContentResolver().openInputStream(uri);
                byte[] fileBytes = IOUtil.toByteArray(inputStream);
                inputStream = mApplication.getContentResolver().openInputStream(uri);
                Book book = epubReader.readEpub(inputStream);
                String title = book.getTitle();
                String checksum = computeChecksum(fileBytes);
                EntityBook entityBook = new EntityBook(title, checksum);
                long id = mDao.insertBook(entityBook);
                String filename = Long.toString(id);
                FileOutputStream outputStream = mApplication.openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(fileBytes);
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                // TODO should check whether there is space before saving file
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                String text = "It looks like this book is already in your library";
                Message message = toastHandler.obtainMessage(toastHandler.TOAST_TEXT, text);
                message.sendToTarget();
            }
        });
    }

    private String computeChecksum(byte[] fileBytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(fileBytes);
            byte[] checksumBytes = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : checksumBytes) {
                String hex = Integer.toHexString(0xFF & b);
                while (hex.length() < 2){
                    hex = "0" + hex;
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<EntityBook> getBook(long id) {
        MediatorLiveData<EntityBook> liveData = new MediatorLiveData<>();
        liveData.addSource(mDao.getBook(id), entityBook -> {
            mExecutorService.execute(() -> {
                Book book = openBook(id, new EpubReader());
                entityBook.setBook(book);
                liveData.postValue(entityBook);
            });
        });
        return liveData;
    }

    private Book openBook(long id, EpubReader epubReader) {
        try {
            String filepath = Long.toString(id);
            InputStream fileInputStream = mApplication.openFileInput(filepath);
            return epubReader.readEpub(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
