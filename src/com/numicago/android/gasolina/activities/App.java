package com.numicago.android.gasolina.activities;

import com.numicago.android.gasolina.settings.ApplicationSettings;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ApplicationSettings.getInstance(this);
    }

    public static Context getContext(){
        return mContext;
    }
}