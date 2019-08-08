package com.example.securerepo.view.RecyclerViewHelpers;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securerepo.model.Note;

public class NoteItemLookup extends ItemDetailsLookup <Note>{

    private RecyclerView recyclerView;

    public NoteItemLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Note> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(),e.getY());
        if (view != null){
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof NoteListAdapter.NoteListHolder){
                return ((NoteListAdapter.NoteListHolder)viewHolder).getItemDetails();
            }
        }
        return null;
    }
}
