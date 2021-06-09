package com.example.foreignebookreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.util.IOUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView mTextView;
    private Button mButton;
    private ImageView mImageView;

    ActivityResultLauncher<String> mGetBookUri = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result == null){
                        return;
                    }
                    try {
                        EpubReader epubReader = new EpubReader();
                        Log.d("MainActivity", "onActivityResult: Uri path = " + result.getPath());
                        InputStream inputStream = getContentResolver().openInputStream(result);
                        Book book = epubReader.readEpub(inputStream);
                        if (book.getCoverImage() != null) {
                            Bitmap coverImage = BitmapFactory.decodeStream(book.getCoverImage().getInputStream());
                            mImageView.setImageBitmap(coverImage);
                        }
                        mTextView.setText(book.getTitle());
                        String filename = "test-save.epub";
                        FileOutputStream fos = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
                        byte[] fileBytes = IOUtil.toByteArray(inputStream);
                        String checksum = computeChecksum(fileBytes);
                        Log.d(TAG, "onActivityResult: file checksum: " + checksum);
                        fos.write(fileBytes);
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
        mImageView = findViewById(R.id.img_book_cover);
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

}