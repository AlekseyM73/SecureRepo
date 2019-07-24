package com.example.securerepo.repository;

import com.example.securerepo.DAO.NotesDAO;
import com.example.securerepo.model.Note;
import java.util.List;
import io.reactivex.Observable;

public class NotesSource {

    private NotesDAO notesDAO;

    public NotesSource(NotesDAO notesDAO) {
        this.notesDAO = notesDAO;
    }

    public void insertNote (Note note){
        notesDAO.insertNote(note);
    }

    public void insertNotes (List<Note> notes){
        notesDAO.insertNotes(notes);
    }

    public void updateNote (Note note){
        notesDAO.updateNote(note);
    }

    public void updateNotes (List<Note> notes){
        notesDAO.insertNotes(notes);
    }

    public Observable<List<Note>> getAllNotes (){
       return notesDAO.getAllNotes();
    }

    public Observable <List<byte[]>> getAllTitles (){
       return notesDAO.getAllTitles();
    }
}