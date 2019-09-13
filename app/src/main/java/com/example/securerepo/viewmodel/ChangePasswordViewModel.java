package com.example.securerepo.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
import com.example.securerepo.utils.BytesConverter;
import com.example.securerepo.view.DetailNoteActivity;
import com.example.securerepo.view.RecyclerViewNoteListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordViewModel extends AndroidViewModel {

    private NotesSource notesSource;
    private PasswordCheckerSource passwordCheckerSource;
    private List<Note> decryptedNotes = new ArrayList<>();
    private PasswordChecker decryptedPasswordChecker;
    private SecretKeySpec oldSecretKeySpec;
    private char[] password;


    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        this.notesSource = new NotesSource(App.notesDatabase.notesDAO());
        this.passwordCheckerSource = new PasswordCheckerSource(App.notesDatabase.passwordCheckerDAO());
    }
     public void setOldSecretKeySpec (SecretKeySpec oldSecretKeySpec ){
        this.oldSecretKeySpec = oldSecretKeySpec;
     }

     public void setNewPassword(char[] password){
        this.password = password;
     }

    public void getDecryptedNotes (){



       /* CompositeDisposable disposable = new CompositeDisposable();

        disposable.add(notesSource.getAllNotes().subscribeOn(Schedulers.io())
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
                            getDecryptedPasswordChecker();
                            Log.d("DEBUG", "in getDecryptedNotes");

                        },
                        throwable -> throwable.printStackTrace()));*/





       /*disposable.add(notesSource.getAllNotes()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
                .subscribe((List<Note> notes) -> {
                    for (Note n : notes) {
                        try {
                            NoteCipher.decryptNote(oldSecretKeySpec, App.cipher, n);
                            decryptedNotes.add(n);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
              *//* .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> {

                })
                .doFinally(() -> {
                    getDecryptedPasswordChecker();
                    Log.d("DEBUG", "in getDecryptedNotes");
                })
                .subscribe(notes -> {

                },*//*
                        throwable -> {
                    Toast.makeText(getApplication(),
                            "Something went wrong.", Toast.LENGTH_LONG).show();
                }));*/

    }
    public void getDecryptedPasswordChecker(){

        Disposable disposable = passwordCheckerSource.getPasswordChecker(1)
                .subscribeOn(Schedulers.io())
                .doOnSuccess((PasswordChecker checker) -> {
                    PasswordCheckerCipher.decryptChecker
                            (oldSecretKeySpec, App.cipher, checker);
                    decryptedPasswordChecker = checker;
                }).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> {

                })
                .doFinally(() -> {

                    Log.d("DEBUG", "in getDecryptedPasswordChecker()");
                    App.secretKeySpec = NoteCipher.generateKey(password);
                    insertEncryptedPasswordChecker();
                })
                .subscribe(checker -> {

                }, throwable -> {
                    Toast.makeText(getApplication(),
                            "Something went wrong.", Toast.LENGTH_LONG).show();
                });

    }

    public void insertEncryptedPasswordChecker(){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                passwordCheckerSource.insertPasswordChecker
                                (PasswordCheckerCipher.encryptChecker(App.secretKeySpec,
                                        App.cipher, decryptedPasswordChecker.getBytesToCheck()));
            }
        }).doOnError(throwable -> {
            throwable.printStackTrace();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onComplete() {
                Log.d("DEBUG", "in insertEncryptedPasswordChecker()");
                insertEncryptedNotes();
            }
            @Override
            public void onError(Throwable e) {
            }
        });
    }




    public void insertEncryptedNotes (){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                notesSource.insertNotes(NoteCipher.encryptNotes
                        (App.secretKeySpec, App.cipher, decryptedNotes));
            }
        }).doOnError(throwable -> {
            throwable.printStackTrace();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onComplete() {
                Log.d("DEBUG", "in insertEncryptedNotes");
              /*  Intent intent = new Intent(getApplication(),RecyclerViewNoteListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(intent);*/
            }
            @Override
            public void onError(Throwable e) {
            }
        });

    }



}
