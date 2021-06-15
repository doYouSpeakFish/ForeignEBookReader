package com.example.foreignebookreader;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foreignebookreader.DbEntities.EntityBook;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.service.MediatypeService;

public class AppViewModel extends AndroidViewModel {

    private static final String TAG = "AppViewModel";

    private final AppRepository mRepository;
    private final ExecutorService mExecutorService;

    private String mCurrentBookChecksum;
    private List<String> mPages;
    private String mCurrentBookLanguage;

    private EntityBook mCurrentBook;

    private MutableLiveData<List<String>> mPagesLiveData;

    public AppViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mExecutorService = Executors.newFixedThreadPool(4);
    }

    public LiveData<List<EntityBook>> getBooks() {
        return mRepository.getBooks();
    }

    public void AddNewBook(Uri uri, MainActivity.ToastHandler toastHandler) {
        mRepository.addBook(uri, toastHandler);
    }

    public LiveData<EntityBook> getBook(long id) {
        MutableLiveData<EntityBook> liveData = new MutableLiveData<>();
        mRepository.getBook(id, liveData::postValue);
        return liveData;
    }

    public LiveData<List<String>> getPages(EntityBook entityBook) {
        // load pages if no book, different book, or language option changed
        if (mCurrentBook == null || !mCurrentBook.getChecksum().equals(entityBook.getChecksum())
                || !mCurrentBook.getLanguageCode().equals(entityBook.getLanguageCode())) {
            mCurrentBook = entityBook;
            mPagesLiveData = new MutableLiveData<>();
            mExecutorService.execute(() -> {
                try {
                    Log.d(TAG, "getPages: extracting pages...");
                    List<String> pages = extractPages(entityBook.getBook(), entityBook.getLanguageCode());
                    mPagesLiveData.postValue(pages);
                    Log.d(TAG, "getPages: pages extracted successfully");
                } catch (IOException e) {
                    Log.e(TAG, "getPages: problem extracting pages");
                    e.printStackTrace();
                }
            });
        }
        return mPagesLiveData;
    }

    public List<String> extractPages(Book book, String language) throws IOException {
        ArrayList<String> pages = new ArrayList<>();
        Spine spine = book.getSpine();
        Locale locale;
        if (language == null) {
            locale = Locale.getDefault();
        } else {
            locale = new Locale(language);
        }
        BreakIterator iterator = BreakIterator.getSentenceInstance(locale);
        String line;
        InputStream inputStream;
        BufferedReader reader;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<spine.size(); i++) {
            Resource resource = spine.getResource(i);
            if (resource.getMediaType().equals(MediatypeService.XHTML)) {
                inputStream = resource.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    if (line.contains("<html>")) {
                        stringBuilder.delete(0, stringBuilder.length());
                    }

                    stringBuilder.append(line);

                    if (line.contains("</html>")) {
                        String html = stringBuilder.toString();
                        String textContent = Jsoup.parse(html).text();
                        iterator.setText(textContent);
                        int start = iterator.first();
                        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
                            String text = textContent.substring(start, end);
                            pages.add(text);
                        }
                    }
                }
            }
        }
        return pages;
    }

    public void updateEntityBook(EntityBook entityBook) {
        mRepository.updateEntityBook(entityBook);
    }

}
