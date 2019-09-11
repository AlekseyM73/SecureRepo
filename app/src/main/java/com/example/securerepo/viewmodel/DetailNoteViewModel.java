package com.example.securerepo.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.securerepo.application.App;
import com.example.securerepo.model.Note;
import com.example.securerepo.repository.NotesSource;

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
