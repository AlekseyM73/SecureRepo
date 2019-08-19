package com.example.securerepo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.securerepo.App;
import com.example.securerepo.R;
import com.example.securerepo.crypto.PasswordCheckerCipher;
import com.example.securerepo.model.PasswordChecker;
import com.example.securerepo.repository.PasswordCheckerSource;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class EnterPasswordActivity extends Activity {

    private Button btDecrypt;
    private EditText etEnterPassword;
    private final String PASSWORD = "password";
    private Disposable disposable;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        etEnterPassword = findViewById(R.id.enterPasswordActivityEditText);
        btDecrypt = findViewById(R.id.enterPasswordActivityButtonEnter);
        btDecrypt.setOnClickListener(btDecryptListener);

    }

    View.OnClickListener btDecryptListener = v -> {
        char[] password = new char[etEnterPassword.length()];
        if (etEnterPassword.length() != 0) {
            etEnterPassword.getText().getChars(0, etEnterPassword.length(), password, 0);
        }
        checkPassword(password);
    };

    private void checkPassword (char [] password){
            disposable = (Disposable) new PasswordCheckerSource
                    (App.notesDatabase.passwordCheckerDAO()).getPasswordChecker(1)
                    .subscribeOn(Schedulers.io()).doOnSuccess((PasswordChecker passwordChecker)->{
                        PasswordCheckerCipher.decryptChecker(passwordChecker, password);
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(checker -> {
                        Intent intent = new Intent(this, RecyclerViewNoteListActivity.class);
                        intent.putExtra(PASSWORD, password);
                        startActivity(intent);
                        finish();
                    }, throwable -> {
                        Toast.makeText(getApplicationContext(),
                                "Incorrect password", Toast.LENGTH_LONG).show();

                    });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null){
            disposable.dispose();
        }

    }
}
