package com.example.securerepo.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securerepo.R;
import com.example.securerepo.model.Note;
import com.example.securerepo.viewmodel.RecyclerViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewNoteListActivity extends AppCompatActivity
        implements ActionMode.Callback {

    private final String NOTE_ID = "Id";
    private NoteListAdapter adapter;
    private final String PASSWORD = "password";
    private RecyclerViewModel recyclerViewModel;
    private char[] password;
    private BottomNavigationDrawerFragment bottomNavigationDrawerFragment;
    private RecyclerView recyclerView;
    private ActionMode actionMode;
    private boolean isMultiSelect;
    private List<Integer> selectedID = new ArrayList<>();


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

        adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewModel = ViewModelProviders.of(this).get(RecyclerViewModel.class);

        updateView();
    }

    View.OnClickListener fabListener = v -> {
        Intent intent = new Intent(this, NewNoteActivity.class);
        intent.putExtra(PASSWORD, password);
        startActivity(intent);
    };

    private void updateView() {

        recyclerViewModel.getNotes(password).observe(this, notes -> {
            adapter.setNotes(notes);
            if (actionMode != null) {
                actionMode.finish();
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerClickListener
                (this, recyclerView,
                        new RecyclerClickListener.OnItemClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                if (isMultiSelect) {
                                    multiSelect(position);
                                } else {
                                    Intent intent = new Intent(RecyclerViewNoteListActivity.this, DetailNoteActivity.class);
                                    intent.putExtra(NOTE_ID, adapter.getNotefromAdapter(position).getId());
                                    intent.putExtra(PASSWORD, password);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                if (!isMultiSelect) {
                                    selectedID = new ArrayList<>();
                                    isMultiSelect = true;

                                    if (actionMode == null) {
                                        actionMode = startActionMode
                                                (RecyclerViewNoteListActivity.this);
                                    }
                                }
                                multiSelect(position);
                            }
                        }));
    }

    private void multiSelect(int position) {
        Note note = adapter.getNotefromAdapter(position);
        if (note != null) {
            if (actionMode != null) {
                if (selectedID.contains(note.getId()))
                    selectedID.remove(Integer.valueOf(note.getId()));
                else
                    selectedID.add(note.getId());

                if (selectedID.size() > 0)
                    actionMode.setTitle(String.valueOf(selectedID.size()));
                else {
                    actionMode.setTitle("");
                    actionMode.finish();
                }
                adapter.setSelectedID(selectedID);

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case (android.R.id.home): {
                bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
                bottomNavigationDrawerFragment
                        .show(getSupportFragmentManager()
                                , bottomNavigationDrawerFragment.getTag());
                break;
            }
            default:
                break;
        }
        return true;
    }

    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.action_mode_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_mode_menu_delete: {
                showConfirmDeleteDialog();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        isMultiSelect = false;
        selectedID.clear();
        adapter.setSelectedID(new ArrayList<Integer>());
    }

    private void showConfirmDeleteDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getString(R.string.confirm_delete_dialog))
                .setMessage("Are you sure to delete " + selectedID.size() + " notes?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        actionMode.finish();
                        return;
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recyclerViewModel.deleteNotes(selectedID);
                    }
                })
                .show();

    }
}
