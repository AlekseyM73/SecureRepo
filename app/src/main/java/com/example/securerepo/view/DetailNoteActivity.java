package com.example.securerepo.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.securerepo.application.App;
import com.example.securerepo.R;
import com.example.securerepo.crypto.NoteCipher;
import com.example.securerepo.model.Note;
import com.example.securerepo.utils.BytesConverter;
import com.example.securerepo.utils.MenuIconPainter;
import com.example.securerepo.viewmodel.DetailNoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

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
    private int noteId;
    private EditText etTitle;
    private EditText etBody;
    private TextInputLayout textInputLayout;
    private FloatingActionButton fab;
    private MenuItem menuOk;
    private MenuItem menuCancel;
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
        textInputLayout = findViewById(R.id.detailNoteActivityTitleTextInputLayout);
        fab = findViewById(R.id.detail_note_fab);
        fab.setOnClickListener(fabListener);
        Intent intent = getIntent();
        noteId = intent.getIntExtra(NOTE_ID, -1);
        Toolbar toolbar = findViewById(R.id.detailNoteActivityToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        detailNoteViewModel = ViewModelProviders.of(this).get(DetailNoteViewModel.class);
        if (!isEditBtnPressed){
            getNote();
        }

        etTitle.addTextChangedListener(new TextWatcher() {
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

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_EDIT_BTN_PRESSED, isEditBtnPressed);
    }

    View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setViewsAsEditable();
            isEditBtnPressed = true;
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_note_menu, menu);
        menuOk = menu.findItem(R.id.detail_note_menu_ok);
        menuCancel = menu.findItem(R.id.detail_note_menu_cancel);

        MenuIconPainter.tintMenuItem(this,menuOk);
        MenuIconPainter.tintMenuItem(this,menuCancel);

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
            case R.id.detail_note_menu_ok:{
                if (!etTitle.getText().toString().isEmpty()){
                    setViewsAsText();
                    isEditBtnPressed = false;
                    updateNote();
                } else {
                    textInputLayout.setError("The title cannot be empty");
                }
                return true;
            }
            case R.id.detail_note_menu_cancel:{
                getNote();
                setViewsAsText();
                Toast.makeText(DetailNoteActivity.this,
                        "Canceled!", Toast.LENGTH_LONG).show();
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void setViewsAsText() {
        etTitle.setTextIsSelectable(true);
        etTitle.setKeyListener(null);
        etTitle.setCursorVisible(true);

        etBody.setTextIsSelectable(true);
        etBody.setKeyListener(null);
        etBody.setCursorVisible(true);

        fab.show();
        menuOk.setVisible(false);
        menuCancel.setVisible(false);
    }

    private void setViewsAsEditable() {
        etTitle.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_MULTI_LINE);
        etTitle.setMovementMethod(new ScrollingMovementMethod());

        etBody.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_MULTI_LINE);
        etBody.setMovementMethod(new ScrollingMovementMethod());

        fab.hide();
        menuOk.setVisible(true);
        menuCancel.setVisible(true);
    }

    private void getNote() {
        subscribe = detailNoteViewModel.getNote(noteId)
                .subscribeOn(Schedulers.io())
                .doOnSuccess((Note note) -> {
                    NoteCipher.decryptNote(App.secretKeySpec, App.cipher,note);
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
                    note.eraseNoteFields();
                }, throwable -> {
                    Toast.makeText(DetailNoteActivity.this,
                            "Something went wrong.", Toast.LENGTH_LONG).show();
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
                        charToBytes(titleChars),
                        BytesConverter.charToBytes(bodyChars),System.currentTimeMillis());
                NoteCipher.encryptNote(App.secretKeySpec, App.cipher, note);
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
        if (subscribe != null){
            subscribe.dispose();
        }

    }
}
