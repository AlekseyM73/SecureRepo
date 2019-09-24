package com.alekseymakarov.locknote.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alekseymakarov.locknote.application.App;
import com.alekseymakarov.locknote.R;
import com.alekseymakarov.locknote.crypto.NoteCipher;
import com.alekseymakarov.locknote.crypto.PasswordCheckerCipher;
import com.alekseymakarov.locknote.model.PasswordChecker;
import com.alekseymakarov.locknote.repository.PasswordCheckerSource;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class EnterPasswordActivity extends AppCompatActivity {

    private Button btDecrypt;
    private TextInputLayout textInputLayout;
    private EditText etEnterPassword;
    private Disposable disposable;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        textInputLayout = findViewById(R.id.enterPasswordActivityTextInputLayout);
        etEnterPassword = findViewById(R.id.enterPasswordActivityEditText);
        etEnterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textInputLayout.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btDecrypt = findViewById(R.id.enterPasswordActivityButtonEnter);
        btDecrypt.setOnClickListener(btDecryptListener);

    }

    View.OnClickListener btDecryptListener = v -> {
        char[] password = new char[etEnterPassword.length()];
        if (etEnterPassword.length() != 0) {
            etEnterPassword.getText().getChars(0, etEnterPassword.length(), password, 0);
        }
        App.secretKeySpec = NoteCipher.generateKey(password);
        checkPassword();
        etEnterPassword.setText("");
        Arrays.fill(password,'0');
    };

    private void checkPassword(){

            disposable = new PasswordCheckerSource(App.notesDatabase.passwordCheckerDAO()).getPasswordChecker(1)
                    .subscribeOn(Schedulers.io()).doOnSuccess((PasswordChecker passwordChecker)->{
                        PasswordCheckerCipher.decryptChecker
                                (App.secretKeySpec, App.cipher, passwordChecker);
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(checker -> {

                        Intent intent = new Intent(this, RecyclerViewNoteListActivity.class);
                        startActivity(intent);
                        finish();
                    }, throwable -> {
                        textInputLayout.setError(getString(R.string.incorrect_password));
                    });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null){
            disposable.dispose();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
        finish();
    }
}
