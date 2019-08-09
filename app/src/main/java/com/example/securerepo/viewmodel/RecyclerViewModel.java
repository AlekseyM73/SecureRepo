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
import com.example.securerepo.crypto.NoteCipher;
import com.example.securerepo.model.Note;
import com.example.securerepo.repository.NotesSource;
import com.example.securerepo.view.EnterPasswordActivity;
import com.example.securerepo.view.RecyclerViewNoteListActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RecyclerViewModel extends AndroidViewModel {

    private static final String TAG = RecyclerViewNoteListActivity.class.getSimpleName();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private NotesSource notesSource;
    private MutableLiveData<List<Note>> notes;

    public RecyclerViewModel(@NonNull Application application) {
        super(application);
        notesSource = new NotesSource(App.notesDatabase.notesDAO());
    }

    public LiveData<List<Note>> getNotes(char [] password) {
        if (notes == null) {
            notes = new MutableLiveData<>();
        }
        disposable.add(notesSource.getAllNotes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    List<Note> decryptNotes = new ArrayList<>();

                    for (Note n: value){
                        try {
                            NoteCipher.decryptNote(n, password);
                            decryptNotes.add(n);
                            Log.d("ID", ""+n.getId());
                        } catch (Exception e){
                            Toast.makeText(getApplication(), "Incorrect password", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplication(),EnterPasswordActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(intent);
                        }

                    }
                            notes.postValue(decryptNotes);

                        },
                        throwable -> Log.e(TAG, "Unable to load", throwable)));
        return notes;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
