package com.klemar.android.factorytask;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class NewsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
