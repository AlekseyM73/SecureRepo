package com.example.securerepo.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.securerepo.App;
import com.example.securerepo.R;
import com.example.securerepo.crypto.NoteCipher;
import com.example.securerepo.model.Note;
import com.example.securerepo.utils.BytesConverter;
import com.example.securerepo.viewmodel.NewNoteViewModel;
import com.google.android.material.textfield.TextInputLayout;

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
    private TextInputLayout textInputLayout;
    private NewNoteViewModel newNoteViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Toolbar toolbar = findViewById(R.id.newNoteActivityToolbar);
        toolbar.setTitle(this.getString(R.string.add_note));
        setSupportActionBar(toolbar);

        etTitle = findViewById(R.id.newNoteActivityTitleEditText);
        etBody = findViewById(R.id.newNoteActivityBodyEditText);
        textInputLayout = findViewById(R.id.newNoteActivityTitleTextInputLayout);

        newNoteViewModel = ViewModelProviders.of(this).get(NewNoteViewModel.class);

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_note_menu_ok:{
                if (!etTitle.getText().toString().isEmpty()) {

                    char[] titleChars = new char[etTitle.length()];
                    char[] bodyChars = new char[etBody.length()];
                    etTitle.getText().getChars(0, etTitle.length(), titleChars, 0);
                    etBody.getText().getChars(0, etBody.length(), bodyChars, 0);
                    long date = System.currentTimeMillis();

                    Completable.fromAction(new Action() {
                        @Override
                        public void run() {
                            newNoteViewModel.insertNote(NoteCipher.encryptNote(App.secretKeySpec,
                                    App.cipher, BytesConverter.charToBytes(titleChars),
                                    BytesConverter.charToBytes(bodyChars),date));
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
                else {
                    textInputLayout.setError("The title cannot be empty");
                }
                return true;
            }
            case R.id.new_note_menu_cancel:{
                super.onBackPressed();
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showConfirmWithoutSavingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getString(R.string.confirm_back_dialog_title))
                .setMessage(getString(R.string.confirm_back_dialog))
                .setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NewNoteActivity.super.onBackPressed();
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        if(!etTitle.getText().toString().isEmpty()
                || !etBody.getText().toString().isEmpty() ){
            showConfirmWithoutSavingDialog();
        } else super.onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
