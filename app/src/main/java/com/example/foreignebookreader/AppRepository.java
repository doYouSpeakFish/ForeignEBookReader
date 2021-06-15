package com.example.foreignebookreader;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.foreignebookreader.DbEntities.EntityBook;
import com.example.foreignebookreader.DbEntities.EntityTranslation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.util.IOUtil;

public class AppRepository {

    private final static String TAG = "AppRepository";
    private final static String NO_INTERNET = "No internet connection! Cannot be translated!";

    private final AppDao mDao;
    private final ExecutorService mExecutorService;
    private final Application mApplication;

    public AppRepository(Application application) {
        AppDB appDB = AppDB.getDatabase(application);
        mDao = appDB.appDao();
        mExecutorService = Executors.newFixedThreadPool(4);
        mApplication = application;
        AndroidNetworking.initialize(application);
    }

    public LiveData<List<EntityBook>> getBooks() {
        MediatorLiveData<List<EntityBook>> liveData = new MediatorLiveData<>();
        liveData.addSource(mDao.getBooks(), entityBooks -> mExecutorService.execute(() -> {
            EpubReader epubReader = new EpubReader();
            for (EntityBook entityBook : entityBooks) {
                Book book = openBook(entityBook.getId(), epubReader);
                entityBook.setBook(book);
            }
            liveData.postValue(entityBooks);
        }));
        return liveData;
    }

    public LiveData<String> getTranslation(String text, String sourceLang, String targetLang) {
        MediatorLiveData<String> liveData = new MediatorLiveData<>();
        liveData.addSource(mDao.getTranslation(text, sourceLang, targetLang), entityTranslation -> {
            if (entityTranslation == null) {
                mExecutorService.execute(() -> {
                    translate(text, sourceLang, targetLang, (translations, responseState) -> {
                        if (responseState == TranslateCallback.SUCCESSFUL && translations.size() > 0) {
                            String translation = translations.get(0);
                            EntityTranslation newEntityTranslation = new EntityTranslation(text, sourceLang, targetLang, translation);
                            liveData.postValue(translation);
                            mExecutorService.execute(() -> mDao.insertTranslation(newEntityTranslation));
                        } else if (responseState == TranslateCallback.NO_INTERNET) {
                            liveData.postValue("No internet connection");
                        } else {
                            liveData.postValue("Problem retrieving translation");
                            Log.d(TAG, "getTranslation: could not get translation: " + responseState);
                        }
                    });
                });
            } else {
                liveData.postValue(entityTranslation.getTranslation());
            }
        });
        return liveData;
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
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                String text = "Sorry, there was a problem adding that book :(";
                Message message = toastHandler.obtainMessage(toastHandler.TOAST_TEXT, text);
                message.sendToTarget();
            }
        });
    }

    private String computeChecksum(byte[] fileBytes) throws NoSuchAlgorithmException {
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
    }

    public void getBook(long id, GetBookCallback callback) {
        mExecutorService.execute(() -> {
            EntityBook entityBook = mDao.getBook(id);
            Book book = openBook(id, new EpubReader());
            entityBook.setBook(book);
            callback.run(entityBook);
        });
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

    public void updateEntityBook(EntityBook entityBook) {
        mExecutorService.execute(() -> mDao.updateEntityBook(entityBook));
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    public void translate(String text, String sourceLanguageCode, String targetLanguageCode, TranslateCallback callback) {
        if (checkInternetConnection()) {
            AndroidNetworking.post("https://translation.googleapis.com/language/translate/v2")
                    .addQueryParameter("q", text)
                    .addQueryParameter("target", targetLanguageCode)
                    .addQueryParameter("format", "text")
                    .addQueryParameter("source", sourceLanguageCode)
                    .addQueryParameter("model", "nmt")
                    .addQueryParameter("key", "AIzaSyChzslY7Oy8eP-B1xLm_IulN7f2vyLZZfo") // TODO must not share key. look into more secure way to access google APIs
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ArrayList<String> translations = new ArrayList<>();
                            try {
                                JSONObject data = response.getJSONObject("data");
                                JSONArray translationsJSONArray = (JSONArray) data.get("translations");
                                for (int i=0; i<translationsJSONArray.length(); i++) {
                                    JSONObject translationObject = translationsJSONArray.getJSONObject(i);
                                    String translation = translationObject.getString("translatedText");
                                    translations.add(translation);
                                    callback.run(translations, TranslateCallback.SUCCESSFUL);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "onResponse: JSONException from response: " + response.toString());
                                callback.run(null, TranslateCallback.JSON_ERROR);
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            callback.run(null, TranslateCallback.API_REQUEST_ERROR);
                        }
                    });
        } else {
            callback.run(null, TranslateCallback.NO_INTERNET);
        }
    }

    public interface GetBookCallback {
        void run(EntityBook entityBook);
    }

    public interface TranslateCallback {
        int SUCCESSFUL = 1;
        int JSON_ERROR = 2;
        int API_REQUEST_ERROR = 3;
        int NO_INTERNET = 4;
        void run(List<String> translations, int responseState);
    }

}
