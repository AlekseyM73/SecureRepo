package com.example.securerepo.repository;

import com.example.securerepo.dao.NotesDAO;
import com.example.securerepo.model.Note;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class NotesSource {

    private NotesDAO notesDAO;

    public NotesSource(NotesDAO notesDAO) {
        this.notesDAO = notesDAO;
    }

    public void deleteNotes (List<Integer> id){ notesDAO.deleteNotes(id);}

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
        notesDAO.updateNotes(notes);
    }

    public Single<Note> getNote(int id) {
        return notesDAO.getNote(id);
    }

    public Observable<List<Note>> getAllNotes (){
       return notesDAO.getAllNotes();
    }

    public Single<List<Note>> getNotes() { return notesDAO.getNotes(); }

    public Observable <List<byte[]>> getAllTitles (){
       return notesDAO.getAllTitles();
    }
}
