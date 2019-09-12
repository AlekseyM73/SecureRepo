package com.example.securerepo.application;

import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.securerepo.R;
import com.example.securerepo.database.NotesDatabase;
import com.example.securerepo.view.EnterPasswordActivity;

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
        boolean isDarkTheme = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_dark_theme", false);
        if (isDarkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        notesDatabase = NotesDatabase.getDataBase(this);
        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}