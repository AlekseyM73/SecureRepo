package com.example.securerepo.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securerepo.R;
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
import java.util.Arrays;
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
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordViewModel extends AndroidViewModel {

    private NotesSource notesSource;
    private PasswordCheckerSource passwordCheckerSource;
    private List<Note> decryptedNotes = new ArrayList<>();
    private PasswordChecker decryptedPasswordChecker;
    private char[] password;


    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        this.notesSource = new NotesSource(App.notesDatabase.notesDAO());
        this.passwordCheckerSource = new PasswordCheckerSource(App.notesDatabase.passwordCheckerDAO());
    }

     public void setNewPassword(char[] password){
        this.password = password;
     }

    public void getDecryptedNotes () {

        notesSource.getNotes().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Note>>() {
                    @Override
                    public void onSuccess(List<Note> notes) {
                        for (Note n : notes) {
                            try {
                                NoteCipher.decryptNote(App.secretKeySpec, App.cipher, n);
                                decryptedNotes.add(n);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        getDecryptedPasswordChecker();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplication(),
                                getApplication().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void getDecryptedPasswordChecker(){

        passwordCheckerSource.getPasswordChecker(1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<PasswordChecker>() {
                    @Override
                    public void onSuccess(PasswordChecker passwordChecker) {
                        try {
                            PasswordCheckerCipher.decryptChecker
                                    (App.secretKeySpec, App.cipher, passwordChecker);
                            decryptedPasswordChecker = passwordChecker;
                            App.secretKeySpec = NoteCipher.generateKey(password);
                            insertEncryptedPasswordChecker();
                        } catch (Exception e){

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplication(),
                                getApplication().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();
                    }
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

                insertEncryptedNotes();
            }
            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplication(),
                        getApplication().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();
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

                for (Note n:decryptedNotes){
                    n.eraseNoteFields();
                }
                Arrays.fill(password,'0');

                Intent intent = new Intent(getApplication(),RecyclerViewNoteListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getApplication().startActivity(intent);
            }
            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplication(),
                        getApplication().getString(R.string.something_went_wrong) , Toast.LENGTH_LONG).show();
            }
        });

    }



}
