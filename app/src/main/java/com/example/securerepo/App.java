package com.example.securerepo;

import android.app.Application;
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
        notesDatabase = NotesDatabase.getDataBase(this);
        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}