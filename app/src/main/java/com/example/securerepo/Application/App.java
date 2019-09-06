package com.example.securerepo.Application;

import android.app.Application;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.securerepo.R;
import com.example.securerepo.database.NotesDatabase;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class App extends Application {

    public static NotesDatabase notesDatabase;
    public static Cipher cipher;
    public static SecretKeySpec secretKeySpec;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        boolean theme = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_dark_theme", false);
        if (theme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        ProcessLifecycleOwner.get().getLifecycle().addObserver( new AppLifeCycleListener());
        notesDatabase = NotesDatabase.getDataBase(this);
        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}