package com.example.foreignebookreader;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String EXTRA_ID = "id";

    private AppViewModel mViewModel;
    private ActivityResultLauncher<String> mGetBookUri;
    private ToastHandler mToastHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGetBookUri = registerForActivityResult(new ActivityResultContracts.GetContent(), this::addBook);
        mToastHandler = new ToastHandler(Looper.getMainLooper());

        ViewModelProvider.AndroidViewModelFactory viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(AppViewModel.class);

        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_open_book);
        button.setOnClickListener(v -> mGetBookUri.launch("application/epub+zip"));

        RecyclerView recyclerView = findViewById(R.id.rv_book_select);
        BookListAdapter adapter = new BookListAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mViewModel.getBooks().observe(this, adapter::submitList);

        Uri uri = getIntent().getData();
        addBook(uri);
    }

    private void addBook(Uri uri) {
        if (uri != null){
            mViewModel.AddNewBook(uri, mToastHandler);
        }
    }

    class ToastHandler extends Handler {

        public final int TOAST_TEXT = 1;

        ToastHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String text = (String) msg.obj;
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
        }
    }

}