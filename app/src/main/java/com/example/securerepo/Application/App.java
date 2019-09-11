package com.example.securerepo.application;

import android.app.Application;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.securerepo.application.AppLifeCycleListener;
import com.example.securerepo.R;
import com.example.securerepo.database.NotesDatabase;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class App extends Application {

    public static NotesDatabase notesDatabase;
    public static Cipher cipher;
    public static SecretKeySpec secretKeySpec;
    public static Lifecycle lifecycle;
    public static AppLifeCycleListener appLifeCycleListener;

    @Override
    public void onCreate() {
        super.onCreate();

        lifecycle = ProcessLifecycleOwner.get().getLifecycle();
        appLifeCycleListener = new AppLifeCycleListener();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        boolean isDarkTheme = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_dark_theme", false);
        if (isDarkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        boolean isExitBackground = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_exit_background", false);

        if (isExitBackground){
            lifecycle.addObserver(appLifeCycleListener);
        }

        notesDatabase = NotesDatabase.getDataBase(this);
        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}