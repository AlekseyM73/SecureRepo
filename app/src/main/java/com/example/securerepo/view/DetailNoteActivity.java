package com.example.securerepo.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.securerepo.R;
import com.example.securerepo.crypto.NoteCipher;
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

import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;

public class DetailNoteActivity extends AppCompatActivity {

    private final String IS_EDIT_BTN_PRESSED = "isEditBtnPressed";
    private final String NOTE_ID = "Id";
    private final String PASSWORD = "password";
    private char[] password;
    private int noteId;
    private EditText etTitle;
    private EditText etBody;
    private MenuItem ok;
    private MenuItem edit;
    private MenuItem cancel;
    private boolean isEditBtnPressed = false;
    private DetailNoteViewModel detailNoteViewModel;
    private Disposable subscribe;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);

        if (savedInstanceState != null) {
            isEditBtnPressed = savedInstanceState.getBoolean(IS_EDIT_BTN_PRESSED);
        }
        etTitle = findViewById(R.id.detailNoteActivityTitleEditText);
        etBody = findViewById(R.id.detailNoteActivityBodyEditText);
        Intent intent = getIntent();
        noteId = intent.getIntExtra(NOTE_ID, -1);
        password = intent.getCharArrayExtra(PASSWORD);
        Toolbar toolbar = findViewById(R.id.detailNoteActivityToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        detailNoteViewModel = ViewModelProviders.of(this).get(DetailNoteViewModel.class);
        getNote();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_EDIT_BTN_PRESSED, isEditBtnPressed);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_note_menu, menu);
        ok = menu.findItem(R.id.detail_note_menu_ok);
        edit = menu.findItem(R.id.detail_note_menu_edit);
        cancel = menu.findItem(R.id.detail_note_menu_cancel);
        if (isEditBtnPressed) {
            setViewsAsEditable();
        } else {
            setViewsAsText();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.detail_note_menu_edit:{
                setViewsAsEditable();
                isEditBtnPressed = true;
                break;
            }
            case R.id.detail_note_menu_ok:{
                setViewsAsText();
                isEditBtnPressed = false;
                updateNote();
                break;
            }
            case R.id.detail_note_menu_cancel:{
                getNote();
                setViewsAsText();
                Toast.makeText(DetailNoteActivity.this,
                        "Canceled!", Toast.LENGTH_LONG).show();
                break;
            }
            default: break;
        }

        return true;
    }

    private void setViewsAsText() {
        etTitle.setMovementMethod(new ScrollingMovementMethod());
        etTitle.setVerticalScrollBarEnabled(true);
        etTitle.setTextIsSelectable(true);
        etTitle.setKeyListener(null);
        etTitle.setCursorVisible(true);

        etBody.setScroller(new Scroller(this));
        etBody.setMovementMethod(new ScrollingMovementMethod());
        etBody.setVerticalScrollBarEnabled(true);
        etBody.setTextIsSelectable(true);
        etBody.setKeyListener(null);
        etBody.setCursorVisible(true);

        edit.setVisible(true);
        ok.setVisible(false);
        cancel.setVisible(false);
    }

    private void setViewsAsEditable() {
        etTitle.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_MULTI_LINE);
        etTitle.setVerticalScrollBarEnabled(true);
        etTitle.setMovementMethod(new ScrollingMovementMethod());

        etBody.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_MULTI_LINE);
        etBody.setVerticalScrollBarEnabled(true);
        etBody.setMovementMethod(new ScrollingMovementMethod());

        edit.setVisible(false);
        ok.setVisible(true);
        cancel.setVisible(true);
    }

    private void getNote() {
        subscribe = detailNoteViewModel.getNote(noteId)
                .subscribeOn(Schedulers.io())
                .doOnSuccess((Note note) -> {
                    NoteCipher.decryptNote(note, password);
                }).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {

                })
                .doFinally(() -> {

                })
                .subscribe(note -> {
                    etTitle.setText(BytesConverter.bytesToChar(note.getTitle()),
                            0, BytesConverter.bytesToChar(note.getTitle()).length);
                    etBody.setText(BytesConverter.bytesToChar(note.getBody()),
                            0, BytesConverter.bytesToChar(note.getBody()).length);
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
                Note note = new Note(noteId, BytesConverter.
                        charToBytes(titleChars), BytesConverter.charToBytes(bodyChars));
                NoteCipher.encryptNote(note, password);
                detailNoteViewModel.updateNote(note);
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
                etTitle.setText("");
                etBody.setText("");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscribe.dispose();
    }
}
