package com.example.securerepo.view.RecyclerViewHelpers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import com.example.securerepo.model.Note;

import java.util.List;

public class NoteItemKeyProvider extends ItemKeyProvider {

    private List<Note> noteList;

    public NoteItemKeyProvider(int scope, List<Note> noteList) {
        super(scope);
        this.noteList = noteList;
    }

    @Nullable
    @Override
    public Object getKey(int position) {
        return noteList.get(position);
    }

    @Override
    public int getPosition(@NonNull Object key) {
        return noteList.indexOf(key);
    }
}
