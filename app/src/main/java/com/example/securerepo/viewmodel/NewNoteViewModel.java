package com.example.securerepo.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.securerepo.application.App;
import com.example.securerepo.model.Note;
import com.example.securerepo.repository.NotesSource;

public class NewNoteViewModel extends ViewModel {

    private NotesSource notesSource;


    public NewNoteViewModel() {
        notesSource = new NotesSource(App.notesDatabase.notesDAO());
    }

    public void insertNote(Note note) {
        notesSource.insertNote(note);
    }

}
