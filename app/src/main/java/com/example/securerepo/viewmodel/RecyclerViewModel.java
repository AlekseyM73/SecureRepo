package com.example.securerepo.viewmodel;

import androidx.lifecycle.ViewModel;
import com.example.securerepo.App;
import com.example.securerepo.model.Note;
import com.example.securerepo.repository.NotesSource;
import java.util.List;
import io.reactivex.Observable;

public class RecyclerViewModel extends ViewModel {

   private NotesSource notesSource;

   public RecyclerViewModel (){
       notesSource = new NotesSource(App.notesDatabase.notesDAO());
   }


   public Observable<List<Note>> getAllNotes (){
        return notesSource.getAllNotes();
   }

}
