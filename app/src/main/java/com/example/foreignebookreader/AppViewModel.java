package com.example.foreignebookreader;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

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

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.service.MediatypeService;

public class AppViewModel extends AndroidViewModel {

    private static final String TAG = "AppViewModel";

    private final AppRepository mRepository;

    private String mCurrentBookChecksum;
    private List<String> mPages;
    private String mCurrentBookLanguage;

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

    public LiveData<List<String>> getPages(long id) {
        MediatorLiveData<List<String>> liveData = new MediatorLiveData<>();
        liveData.addSource(mRepository.getBook(id), entityBook -> {
            // store checksum and compare to entity checksum to ensure same book isn't processed unnecessarily
            if (!mCurrentBookChecksum.equals(entityBook.getChecksum()) || mPages == null || !mCurrentBookLanguage.equals(entityBook.getLanguageCode())) {
                mCurrentBookChecksum = entityBook.getChecksum();
                mCurrentBookLanguage = entityBook.getLanguageCode();
                try {
                    mPages = extractPages(entityBook.getBook(), entityBook.getLanguageCode());
                } catch (IOException e) {
                    Log.e(TAG, "getPages: problem extracting pages");
                    e.printStackTrace();
                }
            }
            liveData.postValue(mPages);
        });
        return liveData;
    }

    public List<String> extractPages(Book book, String language) throws IOException {
        ArrayList<String> pages = new ArrayList<>();
        Spine spine = book.getSpine();
        BreakIterator iterator = BreakIterator.getSentenceInstance(new Locale(language));
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

}
