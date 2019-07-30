package com.example.securerepo.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.securerepo.R;
import com.example.securerepo.model.Note;
import com.example.securerepo.utils.BytesConverter;
import com.example.securerepo.viewmodel.DetailNoteViewModel;

import java.util.Arrays;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class DetailNoteActivity extends AppCompatActivity {

    private static final String IS_EDIT_BTN_PRESSED = "isEditBtnPressed";
    private final String NOTE_ID = "Id";
    private int noteId;
    private EditText etTitle;
    private EditText etBody;
    private Button btnOk;
    private Button btEdit;
    private Button btnCancel;
    private boolean isEditBtnPressed = false;
    private DetailNoteViewModel detailNoteViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);

        if (savedInstanceState != null) {
            isEditBtnPressed = savedInstanceState.getBoolean(IS_EDIT_BTN_PRESSED);
        }
        Intent intent = getIntent();
        noteId = intent.getIntExtra(NOTE_ID, -1);

        setViews();

        detailNoteViewModel = ViewModelProviders.of(this).get(DetailNoteViewModel.class);
        getNote();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_EDIT_BTN_PRESSED, isEditBtnPressed);
    }

    View.OnClickListener btnOkListener = v -> {
        setViewsAsText();
        isEditBtnPressed = false;
        updateNote();
    };

    View.OnClickListener btnEditListener = v -> {
        setViewsAsEditable();
        isEditBtnPressed = true;
    };

    View.OnClickListener btnCancelListener = v -> {
        getNote();
        setViewsAsText();
    };

    private void setViews() {
        etTitle = findViewById(R.id.detailNoteActivityTitleEditText);
        etBody = findViewById(R.id.detailNoteActivityBodyEditText);
        btnOk = findViewById(R.id.detailNoteActivityButtonOk);
        btEdit = findViewById(R.id.detailNoteActivityButtonEdit);
        btnCancel = findViewById(R.id.detailNoteActivityButtonCancel);
        btnOk.setOnClickListener(btnOkListener);
        btEdit.setOnClickListener(btnEditListener);
        btnCancel.setOnClickListener(btnCancelListener);

        if (isEditBtnPressed) {
            setViewsAsEditable();
        } else {
            setViewsAsText();
        }
    }

    private void setViewsAsText() {
        etBody.setEnabled(false);
        etBody.setCursorVisible(false);
        etBody.setBackgroundColor(Color.TRANSPARENT);
        etTitle.setEnabled(false);
        etTitle.setCursorVisible(false);
        etTitle.setBackgroundColor(Color.TRANSPARENT);
        btEdit.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
    }

    private void setViewsAsEditable() {
        etBody.setEnabled(true);
        etBody.setCursorVisible(true);
        etTitle.setEnabled(true);
        etTitle.setCursorVisible(true);
        btEdit.setVisibility(View.INVISIBLE);
        btnOk.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
    }

    private void getNote() {
        final Disposable subscribe = detailNoteViewModel.getNote(noteId)
                .subscribeOn(Schedulers.io())
                .doOnSuccess((Note note) -> {
                    etTitle.setText(BytesConverter.bytesToChar(note.getTitle()), 0, note.getTitle().length);
                    etBody.setText(BytesConverter.bytesToChar(note.getBody()), 0, note.getBody().length);
                }).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                })
                .doFinally(() -> {
                })
                .subscribe(note -> {
                });
    }

    private void updateNote() {
        char[] titleChars = new char[etTitle.length()];
        char[] bodyChars = new char[etBody.length()];
        Completable.fromAction(new Action() {

            @Override
            public void run() {

                etTitle.getText().getChars(0, etTitle.length(), titleChars, 0);
                etBody.getText().getChars(0, etBody.length(), bodyChars, 0);
                detailNoteViewModel.updateNote(new Note(noteId, BytesConverter.
                        charToBytes(titleChars), BytesConverter.charToBytes(bodyChars)));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Toast.makeText(DetailNoteActivity.this,
                        "Updated!", Toast.LENGTH_LONG).show();
                etTitle.setText("empty");
                etBody.setText("empty");
                Arrays.fill(titleChars, '0');
                Arrays.fill(bodyChars, '0');
                DetailNoteActivity.super.onBackPressed();
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(DetailNoteActivity.this,
                        "Update failed", Toast.LENGTH_LONG).show();
            }
        });
    }

}
