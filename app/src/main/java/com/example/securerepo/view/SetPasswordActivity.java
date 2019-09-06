package com.example.securerepo.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.securerepo.Application.App;
import com.example.securerepo.R;
import com.example.securerepo.crypto.NoteCipher;
import com.example.securerepo.crypto.PasswordCheckerCipher;
import com.example.securerepo.repository.PasswordCheckerSource;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class SetPasswordActivity extends AppCompatActivity {

    private EditText etSetPass;
    private EditText etSetPassRepeat;
    private TextInputLayout tilPassword1;
    private TextInputLayout tilPassword2;
    private static final int PASSWORD_LENGTH = 8;
    private final String IS_PASSWORD_PRESENT = "isPasswordPresent";
    private final String IS_CAUTION_DIALOG_WAS_SHOWN = "isCautionDialogWasShown";
    private boolean isCautionDialogWasShown = false;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        if (savedInstanceState != null){
            isCautionDialogWasShown = savedInstanceState.getBoolean(IS_CAUTION_DIALOG_WAS_SHOWN);
        }

        etSetPass = findViewById(R.id.setPasswordActivityEditText);
        etSetPassRepeat = findViewById(R.id.setPasswordActivityEditTextRepeat);
        tilPassword1 = findViewById(R.id.setPasswordActivityTextInputLayoutPassword1);
        tilPassword2 = findViewById(R.id.setPasswordActivityTextInputLayoutPassword2);
        Button btnSave = findViewById(R.id.setPasswordActivityButtonSave);
        btnSave.setOnClickListener(listener);
        if (!isCautionDialogWasShown){
            showCautionDialog();
        }

        etSetPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPassword1.setError("");
                tilPassword2.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSetPassRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPassword1.setError("");
                tilPassword2.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                App.secretKeySpec = NoteCipher.generateKey(password1);
                addPasswordChecker();
                Arrays.fill(password1,'0');
                Arrays.fill(password2,'0');

                finish();
            }
            else if (!isTwoPasswordEquals(password1,password2)){
                tilPassword2.setError("Passwords do not match");
            }
            else if (!isPasswordLengthGood(password1)){
                tilPassword1.setError("Password length less than 8 characters");
            }

        }
    };

    private boolean isTwoPasswordEquals(char[] password1, char[] password2) {
        return Arrays.equals(password1, password2);
    }

    private boolean isPasswordLengthGood(char[] password1) {
        return password1.length >= PASSWORD_LENGTH;
    }

    private void addPasswordChecker() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                byte[] bytes = new byte[100];
                Random random = new Random();
                random.nextBytes(bytes);

                new PasswordCheckerSource(App.notesDatabase.passwordCheckerDAO())
                        .insertPasswordChecker
                                (PasswordCheckerCipher.encryptChecker(App.secretKeySpec,
                                        App.cipher, bytes));
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
                startNextScreen();
            }

            @Override
            public void onError(Throwable e) {

            }

        });
    }

    private void startNextScreen() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.alekseym73.securerepo", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(IS_PASSWORD_PRESENT, true).commit();
        Intent intent = new Intent(this, RecyclerViewNoteListActivity.class);
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
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null){
            alertDialog.dismiss();
        }
    }
}
