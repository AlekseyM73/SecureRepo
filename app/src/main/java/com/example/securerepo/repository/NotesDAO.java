package com.example.securerepo.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.securerepo.model.Note;

import java.util.List;

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
    List<Note> getAllNotes ();

    @Query("SELECT title from notes_table")
    List<Character[]> getAllTitles ();

}
