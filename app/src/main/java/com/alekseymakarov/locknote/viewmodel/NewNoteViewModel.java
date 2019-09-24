package com.alekseymakarov.locknote.viewmodel;

import androidx.lifecycle.ViewModel;

import com.alekseymakarov.locknote.application.App;
import com.alekseymakarov.locknote.model.Note;
import com.alekseymakarov.locknote.repository.NotesSource;

public class NewNoteViewModel extends ViewModel {

    private NotesSource notesSource;


    public NewNoteViewModel() {
        notesSource = new NotesSource(App.notesDatabase.notesDAO());
    }

    public void insertNote(Note note) {
        notesSource.insertNote(note);
    }

}
