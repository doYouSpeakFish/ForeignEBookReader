package com.example.foreignebookreader;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    Button mButton;

    ActivityResultLauncher<String> mGetBookUri = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result == null){
                        return;
                    }
                    try {
                        EpubReader epubReader = new EpubReader();
                        File bookFile = new File(result.getPath());
                        Book book = epubReader.readEpub(new FileInputStream(bookFile));
                        mTextView.setText(book.getTitle());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        mTextView.setText("Sorry, the file was not found");
                    } catch (IOException e) {
                        e.printStackTrace();
                        mTextView.setText("Sorry, there was a problem opening that book");
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.tv_book_title_test);
        mButton = findViewById(R.id.btn_open_book);
        mButton.setOnClickListener(v -> mGetBookUri.launch("application/epub+zip"));
    }

}