package com.example.securerepo.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.securerepo.model.Note;
import java.util.List;
import io.reactivex.Observable;

@Dao
public interface NotesDAO {

    @Insert
    void insertNote (Note note);

    @Insert
    void insertNotes (List<Note> notes);

    @Update
    void updateNote (Note note);

    @Update
    void updateNotes (List<Note> notes);

    @Query("SELECT * from notes_table")
    Observable <List<Note>> getAllNotes ();

    @Query("SELECT title from notes_table")
    Observable <List<byte[]>> getAllTitles ();

}
