package com.example.securerepo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securerepo.R;
import com.example.securerepo.model.Note;

import java.util.Collections;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListHolder>  {

    private final LayoutInflater layoutInflater;
    private List<Note> notes = Collections.emptyList();

    class NoteListHolder extends RecyclerView.ViewHolder{

        private final TextView recyclerviewItemTitle;
        public NoteListHolder(@NonNull View itemView) {
            super(itemView);
            recyclerviewItemTitle = itemView.findViewById(R.id.recyclerviewItemTitle);
        }
    }

    public NoteListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NoteListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NoteListHolder(layoutInflater.inflate(R.layout.recyclerview_item,
                viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 0;
    }
}
