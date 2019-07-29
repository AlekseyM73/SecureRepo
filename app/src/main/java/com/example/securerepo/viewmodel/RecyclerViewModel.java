package com.example.securerepo.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securerepo.App;
import com.example.securerepo.model.Note;
import com.example.securerepo.repository.NotesSource;
import com.example.securerepo.view.RecyclerViewNoteListActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RecyclerViewModel extends ViewModel {

    private static final String TAG = RecyclerViewNoteListActivity.class.getSimpleName();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private NotesSource notesSource;
    private MutableLiveData<List<Note>> notes;

    public RecyclerViewModel() {
        notesSource = new NotesSource(App.notesDatabase.notesDAO());
    }

    public LiveData<List<Note>> getNotes() {
        if (notes == null) {
            notes = new MutableLiveData<>();
        }
        disposable.add(notesSource.getAllNotes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(n -> notes.setValue(n),
                        throwable -> Log.e(TAG, "Unable to load", throwable)));
        return notes;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
