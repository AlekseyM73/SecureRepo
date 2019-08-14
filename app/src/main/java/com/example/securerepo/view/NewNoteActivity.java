package com.example.securerepo.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.securerepo.R;
import com.example.securerepo.crypto.NoteCipher;
import com.example.securerepo.utils.BytesConverter;
import com.example.securerepo.viewmodel.NewNoteViewModel;

import java.util.Arrays;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class NewNoteActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etBody;
    private NewNoteViewModel newNoteViewModel;
    private char[] password;
    private final String PASSWORD = "password";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        password = getIntent().getCharArrayExtra(PASSWORD);

        Button btnOk = findViewById(R.id.newNoteActivityButtonOK);
        btnOk.setOnClickListener(btnOklistener);
        Button btnCancel = findViewById(R.id.newNoteActivityButtonCancel);
        btnCancel.setOnClickListener(btnCancelListener);
        etTitle = findViewById(R.id.newNoteActivityTitleEditText);
        etBody = findViewById(R.id.newNoteActivityBodyEditText);

        newNoteViewModel = ViewModelProviders.of(this).get(NewNoteViewModel.class);

    }

    View.OnClickListener btnCancelListener = v -> {
        super.onBackPressed();
        finish();
    };

    View.OnClickListener btnOklistener = v -> {
        if (!etTitle.getText().toString().isEmpty()) {

            char[] titleChars = new char[etTitle.length()];
            char[] bodyChars = new char[etBody.length()];
            etTitle.getText().getChars(0, etTitle.length(), titleChars, 0);
            etBody.getText().getChars(0, etBody.length(), bodyChars, 0);

            Completable.fromAction(new Action() {
                @Override
                public void run() {
                    newNoteViewModel.insertNote(NoteCipher.encryptNote(BytesConverter.charToBytes(titleChars), BytesConverter.charToBytes(bodyChars), password));
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onComplete() {
                    Toast.makeText(NewNoteActivity.this,
                            "Saved!", Toast.LENGTH_LONG).show();
                    etTitle.setText("");
                    etBody.setText("");
                    Arrays.fill(titleChars, '0');
                    Arrays.fill(bodyChars, '0');
                    NewNoteActivity.super.onBackPressed();
                    finish();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(NewNoteActivity.this,
                            "Save failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
