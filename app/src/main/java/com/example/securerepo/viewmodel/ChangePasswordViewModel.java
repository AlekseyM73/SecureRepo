package com.example.securerepo.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.securerepo.application.App;
import com.example.securerepo.database.NotesDatabase;
import com.example.securerepo.model.Note;
import com.example.securerepo.repository.NotesSource;

import java.util.ArrayList;
import java.util.List;

public class ChangePasswordViewModel extends ViewModel {

    private NotesSource notesSource;
    private List<Note> notes = new ArrayList<>();

    public ChangePasswordViewModel(){
        this.notesSource = new NotesSource(App.notesDatabase.notesDAO());
    }



}
