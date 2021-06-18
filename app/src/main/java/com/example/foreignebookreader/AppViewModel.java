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
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
            // TODO cache pages for quick loading of recently read books
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
            Log.d(TAG, "extractPages: page: " + pages.size());
            Log.d(TAG, "extractPages: resource href: " + resource.getHref());
            if (resource.getMediaType().equals(MediatypeService.XHTML)) {
                // TODO handle different media types
                // TODO keep html so book formatting can be preserved. Either change to full pages of text, or split html into sentences whilst keeping tags for each sentence correct.
                // Consider using BreakIterator with custom CharacterIterator for html that skips tags.
                // May not work, depends on whether BreakIterator tracks index itself, or asks CharacterIterato
                inputStream = resource.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    if (line.contains("<html")) {
                        stringBuilder.delete(0, stringBuilder.length());
                    }

                    stringBuilder.append(line);

                    if (line.contains("</html>")) {
                        String html = stringBuilder.toString();

                        // Add new lines that Jsoup text() method removes or fails to add
                        html = html.replace("<br>", "addnewlineaddnewline");
                        String[] textTags = {"address", "article", "aside", "blockquote", "body", "dd", "details", "div", "dl", "dt", "fieldset",
                                "figcaption", "figure", "footer", "form", "h1", "h2", "h3", "h4", "h5", "h6", "header", "hr", "html", "legend",
                                "li", "menu", "nav", "ol", "p", "pre", "section", "summary", "ul", "table", "tbody", "tfoot", "thead", "tr", "caption"};
                        for (String tagName : textTags) {
                            String tag = "<" + tagName + ">";
                            html = html.replace(tag, tag + "addnewlineaddnewline");
                        }

                        String textContent = Jsoup.parse(html).text();
                        textContent = textContent.replace("addnewlineaddnewline", "\n");

                        // Break text into sentences
                        iterator.setText(textContent);
                        int start = iterator.first();
                        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
                            String text = textContent.substring(start, end);
                            text = text.trim();
                            if (!text.equals("")) {
                                pages.add(text);
                            }
                        }
                    }
                }
                reader.close();
            }
        }
        return pages;
    }

    public void updateEntityBook(EntityBook entityBook) {
        mRepository.updateEntityBook(entityBook);
    }

    public LiveData<String> getTranslation(String pageText) {
        return mRepository.getTranslation(pageText, mCurrentBook.getLanguageCode(), "en"); // TODO allow user to select target language
    }
}
