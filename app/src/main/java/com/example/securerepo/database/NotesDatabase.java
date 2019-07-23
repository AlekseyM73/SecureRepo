package com.example.securerepo.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.securerepo.DAO.NotesDAO;
import com.example.securerepo.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {

    private static volatile NotesDatabase INSTANCE;

    public abstract NotesDAO notesDAO();

    static NotesDatabase getDataBase (final Context context){
        if (INSTANCE == null) {
            synchronized (NotesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                            , NotesDatabase.class, "notes_database").build();
                }
            }
        }
       return INSTANCE;
    }


}
