package com.example.securerepo.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.securerepo.R;
import com.example.securerepo.viewmodel.RecyclerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RecyclerViewNoteListActivity extends AppCompatActivity {


    private static final String TAG = RecyclerViewNoteListActivity.class.getSimpleName();
    NoteListAdapter adapter;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private RecyclerViewModel recyclerViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_notes);

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);
        adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewModel = ViewModelProviders.of(this).get(RecyclerViewModel.class);

        refresh();
    }

    View.OnClickListener fabListener = v -> {
        Intent intent = new Intent(this, NewNoteActivity.class);
        startActivity(intent);
    };

    private void refresh(){
        disposable.add(recyclerViewModel.getAllNotes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notes -> adapter.setNotes(notes),
                        throwable -> Log.e(TAG, "Unable to load", throwable)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
