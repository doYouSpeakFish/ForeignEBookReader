package com.example.foreignebookreader;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AppViewModel mViewModel;

    ActivityResultLauncher<String> mGetBookUri = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null){
                        mViewModel.AddNewBook(result, mToastHandler);
                    }
                }
            });

    ToastHandler mToastHandler = new ToastHandler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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