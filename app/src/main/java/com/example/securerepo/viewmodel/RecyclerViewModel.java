package com.example.securerepo.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.securerepo.App;
import com.example.securerepo.R;
import com.example.securerepo.crypto.NoteCipher;
import com.example.securerepo.model.Note;
import com.example.securerepo.repository.NotesSource;
import com.example.securerepo.view.EnterPasswordActivity;
import com.example.securerepo.view.RecyclerViewNoteListActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class RecyclerViewModel extends AndroidViewModel {

    private final CompositeDisposable disposable = new CompositeDisposable();
    private NotesSource notesSource;
    private MutableLiveData<List<Note>> notes;

    public RecyclerViewModel(@NonNull Application application) {
        super(application);
        notesSource = new NotesSource(App.notesDatabase.notesDAO());
    }

    public LiveData<List<Note>> getNotes() {
        if (notes == null) {
            notes = new MutableLiveData<>();
        }
        disposable.add(notesSource.getAllNotes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                            List<Note> decryptNotes = new ArrayList<>();

                            for (Note n : value) {
                                try {
                                    NoteCipher.decryptNote(App.secretKeySpec, App.cipher, n);
                                    decryptNotes.add(n);

                                } catch (Exception e) {
                                  e.printStackTrace();
                                }

                            }
                            notes.setValue(decryptNotes);

                        },
                        throwable -> throwable.printStackTrace()));
        return notes;
    }

    public void deleteNotes(List<Integer> idToDelete) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                notesSource.deleteNotes(idToDelete);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Toast.makeText(getApplication(), "Deleted!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplication(), getApplication().getString(R.string.delete_failed_toast), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null){
            disposable.dispose();
        }
    }
}
