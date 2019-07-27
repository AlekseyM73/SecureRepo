package com.example.securerepo.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.securerepo.R;
import com.example.securerepo.Utils.BytesConverter;
import com.example.securerepo.model.Note;
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
    private Button btnOk;
    private Button btnCancel;
    private NewNoteViewModel newNoteViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        btnOk = findViewById(R.id.newNoteActivityButtonOK);
        btnOk.setOnClickListener(btnOklistener);
        btnCancel = findViewById(R.id.newNoteActivityButtonCancel);
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
            int titleLength = etTitle.length();
            int bodyLength = etBody.length();
            char[] titleChars = new char[titleLength];
            char[] bodyChars = new char[bodyLength];
            etTitle.getText().getChars(0, titleLength, titleChars, 0);
            etBody.getText().getChars(0, bodyLength, bodyChars, 0);
            byte[] title = BytesConverter.charToBytes(titleChars);
            byte[] body = BytesConverter.charToBytes(bodyChars);

            Completable.fromAction(new Action() {
                @Override
                public void run() {
                    newNoteViewModel.insertNote(new Note(title, body));
                    etTitle.setText("empty");
                    etBody.setText("empty");
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onComplete() {
                    Arrays.fill(title, (byte) 0);
                    Arrays.fill(body, (byte) 0);
                    Arrays.fill(titleChars, '0');
                    Arrays.fill(bodyChars, '0');
                }

                @Override
                public void onError(Throwable e) {

                }
            });
            super.onBackPressed();
            finish();
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
