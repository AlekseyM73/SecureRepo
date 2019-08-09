package com.example.securerepo.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.view.ActionMode;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.OnDragInitiatedListener;
import androidx.recyclerview.selection.OnItemActivatedListener;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securerepo.R;
import com.example.securerepo.model.Note;
import com.example.securerepo.view.RecyclerViewHelpers.ActionModeController;
import com.example.securerepo.view.RecyclerViewHelpers.NoteItemKeyProvider;
import com.example.securerepo.view.RecyclerViewHelpers.NoteItemLookup;
import com.example.securerepo.view.RecyclerViewHelpers.NoteListAdapter;
import com.example.securerepo.viewmodel.RecyclerViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RecyclerViewNoteListActivity extends AppCompatActivity {

    private final String NOTE_ID = "Id";
    private NoteListAdapter adapter;
    private final String PASSWORD = "password";
    private RecyclerViewModel recyclerViewModel;
    private char [] password;
    private BottomNavigationDrawerFragment bottomNavigationDrawerFragment;
    private SelectionTracker selectionTracker;
    private RecyclerView recyclerView;
    private ActionMode actionMode;

    MenuItem selectedItemCount;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_notes);

        password = getIntent().getCharArrayExtra(PASSWORD);

        recyclerView = findViewById(R.id.recyclerview);



        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);
        setSupportActionBar(bottomAppBar);
        adapter = new NoteListAdapter(this, new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int noteId) {
                Intent intent = new Intent(RecyclerViewNoteListActivity.this, DetailNoteActivity.class);
                intent.putExtra(NOTE_ID, noteId);
                intent.putExtra(PASSWORD, password);
                RecyclerViewNoteListActivity.this.startActivity(intent);
            }
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

        recyclerViewModel.getNotes(password).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {

                adapter.setNotes(notes);

                selectionTracker = new SelectionTracker.Builder<Long>(
                        "note-selection-id",

                        recyclerView,
                        new NoteItemKeyProvider(1, notes),
                        new NoteItemLookup(recyclerView),
                        StorageStrategy.createLongStorage()
                )
                        .withOnItemActivatedListener(new OnItemActivatedListener<Long>() {
                            @Override
                            public boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails<Long> item, @NonNull MotionEvent e) {
                                Toast.makeText(RecyclerViewNoteListActivity.this, "Selected ItemId: " + item.toString(),
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        })
                        .withOnDragInitiatedListener(new OnDragInitiatedListener() {
                            @Override
                            public boolean onDragInitiated(@NonNull MotionEvent e) {
                                //  Log.d(TAG, "onDragInitiated");
                                return true;
                            }

                        })
                        .build();


                adapter.setSelectionTracker(selectionTracker);

                selectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
                    @Override
                    public void onItemStateChanged(@NonNull Object key, boolean selected) {
                        super.onItemStateChanged(key, selected);
                    }

                    @Override
                    public void onSelectionRefresh() {
                        super.onSelectionRefresh();
                    }

                    @Override
                    public void onSelectionChanged() {
                        super.onSelectionChanged();
                        if (selectionTracker.hasSelection() && actionMode == null) {
                            actionMode = startSupportActionMode(new ActionModeController(RecyclerViewNoteListActivity.this, selectionTracker));
                            setMenuItemTitle(selectionTracker.getSelection().size());
                        } else if (!selectionTracker.hasSelection() && actionMode != null) {
                            actionMode.finish();
                            actionMode = null;
                        } else {
                            setMenuItemTitle(selectionTracker.getSelection().size());
                        }
              /*  Iterator<Note> itemIterable = selectionTracker.getSelection().iterator();
                while (itemIterable.hasNext()) {
                    //   Log.i(TAG, itemIterable.next().getItemName());
                }*/
                    }

                    @Override
                    public void onSelectionRestored() {
                        super.onSelectionRestored();
                    }
                });


            }
        });
    }

    public void setMenuItemTitle(int selectedItemSize) {
        selectedItemCount.setTitle("" + selectedItemSize);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        selectedItemCount = menu.findItem(R.id.action_item_count);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                break;
            case R.id.action_clear:
                selectionTracker.clearSelection();
                break;
        }

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
    protected void onRestart() {
        super.onRestart();


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
