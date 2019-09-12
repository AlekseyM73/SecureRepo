package com.example.securerepo.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.securerepo.R;
import com.example.securerepo.application.App;
import com.example.securerepo.crypto.NoteCipher;
import com.example.securerepo.crypto.PasswordCheckerCipher;
import com.example.securerepo.repository.PasswordCheckerSource;
import com.example.securerepo.viewmodel.ChangePasswordViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etChangePass;
    private EditText etChangePassRepeat;
    private TextInputLayout tilPassword1;
    private TextInputLayout tilPassword2;
    private static final int PASSWORD_LENGTH = 8;
    private ViewModel viewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


    etChangePass = findViewById(R.id.changePasswordActivityEditText);
    etChangePassRepeat = findViewById(R.id.changePasswordActivityEditTextRepeat);
    tilPassword1 = findViewById(R.id.changePasswordActivityTextInputLayoutPassword1);
    tilPassword2 = findViewById(R.id.changePasswordActivityTextInputLayoutPassword2);
    Button btnChange = findViewById(R.id.changePasswordActivityButtonChange);
    btnChange.setOnClickListener(listener);

        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);

        etChangePass.addTextChangedListener(new TextWatcher() {
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

        etChangePassRepeat.addTextChangedListener(new TextWatcher() {
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


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            char[] password1 = new char[etChangePass.length()];
            char[] password2 = new char[etChangePassRepeat.length()];
            etChangePass.getText().getChars(0, etChangePass.length(), password1, 0);
            etChangePassRepeat.getText().getChars(0, etChangePassRepeat.length(), password2, 0);

            if (isTwoPasswordEquals(password1, password2) && isPasswordLengthGood(password1)) {
                etChangePass.setText("");
                etChangePassRepeat.setText("");
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
               // startNextScreen();
            }

            @Override
            public void onError(Throwable e) {

            }

        });
    }

    private void startNoteListScreen() {

        Intent intent = new Intent(this, RecyclerViewNoteListActivity.class);
        startActivity(intent);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
