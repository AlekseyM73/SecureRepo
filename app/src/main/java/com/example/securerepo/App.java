package com.example.securerepo;

import android.app.Application;
import com.example.securerepo.database.NotesDatabase;

public class App extends Application {

    public static NotesDatabase notesDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        notesDatabase = NotesDatabase.getDataBase(this);
    }
}