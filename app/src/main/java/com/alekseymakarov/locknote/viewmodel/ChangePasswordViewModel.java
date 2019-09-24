package com.alekseymakarov.locknote.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.alekseymakarov.locknote.R;
import com.alekseymakarov.locknote.application.App;
import com.alekseymakarov.locknote.crypto.NoteCipher;
import com.alekseymakarov.locknote.crypto.PasswordCheckerCipher;
import com.alekseymakarov.locknote.model.Note;
import com.alekseymakarov.locknote.model.PasswordChecker;
import com.alekseymakarov.locknote.repository.NotesSource;
import com.alekseymakarov.locknote.repository.PasswordCheckerSource;
import com.alekseymakarov.locknote.view.RecyclerViewNoteListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

                passwordCheckerSource.updatePasswordChecker
                                (PasswordCheckerCipher.encryptChecker(App.secretKeySpec,
                                        App.cipher, decryptedPasswordChecker.getBytesToCheck()));
            }
        }).doOnError(throwable -> throwable.printStackTrace()).observeOn(AndroidSchedulers.mainThread())
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

                notesSource.updateNotes(NoteCipher.encryptNotes
                        (App.secretKeySpec, App.cipher, decryptedNotes));
            }
        }).doOnError(throwable -> throwable.printStackTrace()).observeOn(AndroidSchedulers.mainThread())
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
