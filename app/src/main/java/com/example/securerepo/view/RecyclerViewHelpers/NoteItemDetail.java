package com.example.securerepo.view.RecyclerViewHelpers;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

import com.example.securerepo.model.Note;

public class NoteItemDetail extends ItemDetailsLookup.ItemDetails<Note> {

    private int adapterPosition;
    private Note selectionKey;

    public NoteItemDetail(int adapterPosition, Note selectionKey) {
        this.adapterPosition = adapterPosition;
        this.selectionKey = selectionKey;
    }

    @Override
    public int getPosition() {
        return adapterPosition;
    }

    @Nullable
    @Override
    public Note getSelectionKey() {
        return selectionKey;
    }
}
