package com.example.securerepo.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.securerepo.dao.NotesDAO;
import com.example.securerepo.dao.PasswordCheckerDAO;
import com.example.securerepo.model.Note;
import com.example.securerepo.model.PasswordChecker;

@Database(entities = {Note.class, PasswordChecker.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {

    private static volatile NotesDatabase INSTANCE;

    public abstract NotesDAO notesDAO();
    public abstract PasswordCheckerDAO passwordCheckerDAO();

    public static NotesDatabase getDataBase (final Context context){
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
