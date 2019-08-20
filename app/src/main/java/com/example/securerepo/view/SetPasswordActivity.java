package com.example.securerepo.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.securerepo.App;
import com.example.securerepo.R;
import com.example.securerepo.crypto.PasswordCheckerCipher;
import com.example.securerepo.model.PasswordChecker;
import com.example.securerepo.repository.PasswordCheckerSource;

import java.util.Arrays;
import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class SetPasswordActivity extends Activity {

    private EditText etSetPass;
    private EditText etSetPassRepeat;
    private static final int PASSWORD_LENGTH = 8;
    private final String IS_PASSWORD_PRESENT = "isPasswordPresent";
    private final String PASSWORD = "password";
    private final String IS_CAUTION_DIALOG_WAS_SHOWN = "isCautionDialogWasShown";
    private boolean isCautionDialogWasShown = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        if (savedInstanceState != null){
            isCautionDialogWasShown = savedInstanceState.getBoolean(IS_CAUTION_DIALOG_WAS_SHOWN);
        }

        etSetPass = findViewById(R.id.setPasswordActivityEditText);
        etSetPassRepeat = findViewById(R.id.setPasswordActivityEditTextRepeat);
        Button btnSave = findViewById(R.id.setPasswordActivityButtonSave);
        btnSave.setOnClickListener(listener);
        if (!isCautionDialogWasShown){
            showCautionDialog();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_CAUTION_DIALOG_WAS_SHOWN, isCautionDialogWasShown);

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            char[] password1 = new char[etSetPass.length()];
            char[] password2 = new char[etSetPassRepeat.length()];
            etSetPass.getText().getChars(0, etSetPass.length(), password1, 0);
            etSetPassRepeat.getText().getChars(0, etSetPassRepeat.length(), password2, 0);

            if (isTwoPasswordEquals(password1, password2) && isPasswordLengthGood(password1)) {
                etSetPass.setText("");
                etSetPassRepeat.setText("");

               /* Arrays.fill(password1,'0');
                Arrays.fill(password2,'0');*/
                addPasswordChecker(password1);
                finish();
            }

        }
    };

    private boolean isTwoPasswordEquals(char[] password1, char[] password2) {
        return Arrays.equals(password1, password2);
    }

    private boolean isPasswordLengthGood(char[] password1) {
        return password1.length >= PASSWORD_LENGTH;
    }

    private void addPasswordChecker(char[] password) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                byte[] bytes = new byte[10];
                Arrays.fill(bytes, (byte) 23);
                new PasswordCheckerSource(App.notesDatabase.passwordCheckerDAO())
                        .insertPasswordChecker
                                (PasswordCheckerCipher.encryptChecker(bytes, password));
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
                startNextScreen(password);
            }

            @Override
            public void onError(Throwable e) {

            }

        });
    }

    private void startNextScreen(char[] password) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.alekseym73.securerepo", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(IS_PASSWORD_PRESENT, true).commit();
        Intent intent = new Intent(this, RecyclerViewNoteListActivity.class);
        intent.putExtra(PASSWORD, password);
        //  Arrays.fill(password,'0');
        startActivity(intent);
    }

    private void showCautionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CAUTION!")
                .setMessage("If you forget the password, you cannot restore notes.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isCautionDialogWasShown = true;
                    }
                })
                .show();
    }
}
