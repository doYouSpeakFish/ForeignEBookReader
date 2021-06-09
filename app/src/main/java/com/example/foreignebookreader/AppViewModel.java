package com.example.foreignebookreader;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AppViewModel extends AndroidViewModel {

    private AppRepository mRepository;

    public AppViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }
}
