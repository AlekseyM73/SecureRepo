package com.alekseymakarov.locknote.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alekseymakarov.locknote.R;
import com.alekseymakarov.locknote.model.Note;
import com.alekseymakarov.locknote.utils.MenuIconPainter;
import com.alekseymakarov.locknote.viewmodel.RecyclerViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class RecyclerViewNoteListActivity extends AppCompatActivity
        implements ActionMode.Callback {

    private final String NOTE_ID = "Id";
    private final String IS_DELETE_DIALOG_WAS_SHOWN = "isDeleteDialogWasShown";
    private final String IS_EXIT_DIALOG_WAS_SHOWN = "isExitDialogWasShown";
    private final String SELECTED_ID = "selectedID";
    private final String IS_MULTI_SELECT = "isMultiSelect";
    private final String IS_IN_ACTION_MODE = "isInActionMode";
    private MenuItem menuDelete;
    private NoteListAdapter adapter;
    private RecyclerViewModel recyclerViewModel;
    private BottomNavigationDrawerFragment bottomNavigationDrawerFragment;
    private RecyclerView recyclerView;
    private boolean isDeleteDialogWasShown;
    private boolean isExitDialogWasShown;
    private ArrayList<Integer> selectedID = new ArrayList<>();
    private ActionMode actionMode;
    private boolean isMultiSelect;
    private boolean isInActionMode;
    private AlertDialog alertDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_notes);

        if (savedInstanceState != null){
            isDeleteDialogWasShown = savedInstanceState.getBoolean(IS_DELETE_DIALOG_WAS_SHOWN);
            isExitDialogWasShown = savedInstanceState.getBoolean(IS_EXIT_DIALOG_WAS_SHOWN);
            isMultiSelect = savedInstanceState.getBoolean(IS_MULTI_SELECT);
            selectedID = savedInstanceState.getIntegerArrayList(SELECTED_ID);
            isInActionMode = savedInstanceState.getBoolean(IS_IN_ACTION_MODE,false);
            if (isInActionMode){
                actionMode = startActionMode
                        (RecyclerViewNoteListActivity.this);
            }
        }

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

        if(actionMode != null){
            actionMode.finish();
        }
        startActivity(intent);

    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_DELETE_DIALOG_WAS_SHOWN,isDeleteDialogWasShown);
        outState.putBoolean(IS_EXIT_DIALOG_WAS_SHOWN, isExitDialogWasShown);
        outState.putBoolean(IS_MULTI_SELECT,isMultiSelect);
        outState.putIntegerArrayList(SELECTED_ID,selectedID);
        outState.putBoolean(IS_IN_ACTION_MODE,isInActionMode);
    }

    private void updateView() {

        recyclerViewModel.getNotes().observe(this, notes -> {
            adapter.setNotes(notes);
        });

        if (isDeleteDialogWasShown){
            showConfirmDeleteDialog();
        }
        if (isExitDialogWasShown){
            showConfirmExitDialog();
        }
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
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        isInActionMode = true;
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.action_mode_menu, menu);
        menuDelete = menu.findItem(R.id.action_mode_menu_delete);
        MenuIconPainter.tintMenuItem(this, menuDelete);
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
        isInActionMode = false;
        adapter.setSelectedID(new ArrayList<Integer>());
    }

    private void showConfirmDeleteDialog() {
        isDeleteDialogWasShown = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirm_delete_dialog))
                .setMessage(getString(R.string.are_you_sure_to_delete) + " " + getResources().getQuantityString(R.plurals.plurals, selectedID.size(), selectedID.size()))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        actionMode.finish();
                        isDeleteDialogWasShown = false;
                        return;
                    }
                })
                .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recyclerViewModel.deleteNotes(selectedID, actionMode);
                        isDeleteDialogWasShown = false;
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();

    }

    private void showConfirmExitDialog(){
        isExitDialogWasShown = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirm_exit_dialog))
                .setMessage(getString(R.string.confirm_exit_dialog_title))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isExitDialogWasShown = false;
                        return;
                    }
                })
                .setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (Note note:adapter.getNotesfromAdapter()){
                            note.eraseNoteFields();
                        }
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null){
            alertDialog.dismiss();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        //Recreate the activity because API21 not switch the theme
                recreate();
    }

    @Override
    public void onBackPressed() {
        showConfirmExitDialog();
    }
}
