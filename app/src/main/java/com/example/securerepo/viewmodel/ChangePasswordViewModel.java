package com.example.securerepo.viewmodel;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securerepo.application.App;
import com.example.securerepo.crypto.NoteCipher;
import com.example.securerepo.crypto.PasswordCheckerCipher;
import com.example.securerepo.database.NotesDatabase;
import com.example.securerepo.model.Note;
import com.example.securerepo.model.PasswordChecker;
import com.example.securerepo.repository.NotesSource;
import com.example.securerepo.repository.PasswordCheckerSource;
import com.example.securerepo.view.RecyclerViewNoteListActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordViewModel extends AndroidViewModel {

    private NotesSource notesSource;
    private PasswordCheckerSource passwordCheckerSource;
    private List<Note> decryptedNotes = new ArrayList<>();
    private PasswordChecker decryptedPasswordChecker;


    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        this.notesSource = new NotesSource(App.notesDatabase.notesDAO());
        this.passwordCheckerSource = new PasswordCheckerSource(App.notesDatabase.passwordCheckerDAO());
    }

    private void getDecryptedNotes (){

       CompositeDisposable disposable = new CompositeDisposable(notesSource.getAllNotes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                            for (Note n : value) {
                                try {
                                    NoteCipher.decryptNote(App.secretKeySpec, App.cipher, n);
                                    decryptedNotes.add(n);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        throwable -> throwable.printStackTrace()));
    }
    private void getDecryptedPasswordChecker(){

        Disposable disposable = passwordCheckerSource.getPasswordChecker(1)
                .subscribeOn(Schedulers.io()).doOnSuccess((PasswordChecker passwordChecker) -> {
                    PasswordCheckerCipher.decryptChecker
                            (App.secretKeySpec, App.cipher, passwordChecker);
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(checker -> {
                    decryptedPasswordChecker = checker;
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    private void insertEncryptedPasswordChecker(){

    }




    private void insertEncryptedNotes (){


    }



}
