package com.example.securerepo.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securerepo.R;
import com.example.securerepo.viewmodel.RecyclerViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class RecyclerViewNoteListActivity extends AppCompatActivity {

    private final String NOTE_ID = "Id";
    private NoteListAdapter adapter;
    private final String PASSWORD = "password";
    private RecyclerViewModel recyclerViewModel;
    private char [] password;
    private BottomNavigationDrawerFragment bottomNavigationDrawerFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_notes);

        password = getIntent().getCharArrayExtra(PASSWORD);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);
        setSupportActionBar(bottomAppBar);
        adapter = new NoteListAdapter(this, noteId -> {
            Intent intent = new Intent(RecyclerViewNoteListActivity.this, DetailNoteActivity.class);
            intent.putExtra(NOTE_ID, noteId);
            intent.putExtra(PASSWORD, password);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewModel = ViewModelProviders.of(this).get(RecyclerViewModel.class);

        updateView();
    }

    View.OnClickListener fabListener = v -> {
        Intent intent = new Intent(this, NewNoteActivity.class);
        intent.putExtra(PASSWORD,password);
        startActivity(intent);
    };

    private void updateView() {

       recyclerViewModel.getNotes(password).observe(this, notes -> {
            adapter.setNotes(notes);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

      switch (item.getItemId()){
            case (android.R.id.home):{
      bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
                bottomNavigationDrawerFragment
                        .show(getSupportFragmentManager()
                                ,bottomNavigationDrawerFragment.getTag());
                break;
            }
            default:break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    }
}
