package com.example.securerepo;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.lifecycle.ProcessLifecycleOwner;

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
        PreferenceManager.setDefaultValues(this,R.xml.preferences, false);
        ProcessLifecycleOwner.get().getLifecycle().addObserver( new AppLifeCycleListener());
        notesDatabase = NotesDatabase.getDataBase(this);
        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}