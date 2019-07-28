package com.example.securerepo.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.securerepo.R;
import com.example.securerepo.Utils.BytesConverter;
import com.example.securerepo.model.Note;
import com.example.securerepo.viewmodel.DetailNoteViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailNoteActivity extends AppCompatActivity {

    private static final String TAG = DetailNoteActivity.class.getSimpleName();
    private final String NOTE_ID = "Id";
    private int noteId;
    private EditText etTitle;
    private EditText etBody;
    private Button btnOk;
    private Button btEdit;
    private Button btnCancel;
    private DetailNoteViewModel detailNoteViewModel;
    private Note note;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        Intent intent = getIntent();
        noteId = intent.getIntExtra(NOTE_ID, -1);

        etTitle = findViewById(R.id.detailNoteActivityTitleEditText);
        etBody = findViewById(R.id.detailNoteActivityBodyEditText);
        btnOk = findViewById(R.id.detailNoteActivityButtonOk);
        btEdit = findViewById(R.id.detailNoteActivityButtonEdit);
        btnCancel = findViewById(R.id.detailNoteActivityButtonCancel);
        btnOk.setOnClickListener(btnOkListener);
        btEdit.setOnClickListener(btnEditListener);
        btnCancel.setOnClickListener(btnCancelListener);

        detailNoteViewModel = ViewModelProviders.of(this).get(DetailNoteViewModel.class);

        getNote();
    }

    View.OnClickListener btnOkListener = v -> {

    };

    View.OnClickListener btnEditListener = v -> {

    };

    View.OnClickListener btnCancelListener = v -> {

    };

    private void getNote() {
        final Disposable subscribe = detailNoteViewModel.getNote(noteId)
                .subscribeOn(Schedulers.io())
                .doOnSuccess(new Consumer<Note>() {
                    @Override
                    public void accept(Note note) throws Exception {
                        etTitle.setText(BytesConverter.bytesToChar(note.getTitle()), 0, note.getTitle().length);
                        etBody.setText(BytesConverter.bytesToChar(note.getBody()), 0, note.getBody().length);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new Consumer<Note>() {
                    @Override
                    public void accept(Note note) throws Exception {
                    }
                });
    }

}