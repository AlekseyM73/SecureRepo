package com.alekseymakarov.locknote.viewmodel;

import androidx.lifecycle.ViewModel;

import com.alekseymakarov.locknote.application.App;
import com.alekseymakarov.locknote.model.Note;
import com.alekseymakarov.locknote.repository.NotesSource;

import io.reactivex.Single;

public class DetailNoteViewModel extends ViewModel {

    NotesSource notesSource;

    public DetailNoteViewModel() {
        this.notesSource = new NotesSource(App.notesDatabase.notesDAO());
    }

    public Single<Note> getNote(int id) {
        return notesSource.getNote(id);
    }

    public void updateNote(Note note) {
        notesSource.updateNote(note);
    }
}
