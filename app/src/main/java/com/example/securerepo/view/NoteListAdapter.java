package com.example.securerepo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securerepo.R;
import com.example.securerepo.Utils.BytesConverter;
import com.example.securerepo.model.Note;

import java.util.Collections;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListHolder>  {

    private final LayoutInflater layoutInflater;
    private List<Note> notes = Collections.emptyList();
    private final OnItemClickListener onItemClickListener;

    class NoteListHolder extends RecyclerView.ViewHolder {

        private final TextView recyclerviewItemTitle;

        public NoteListHolder(@NonNull View itemView) {
            super(itemView);
            recyclerviewItemTitle = itemView.findViewById(R.id.recyclerviewItemTitle);
        }

        public void onBind (int noteId, NoteListAdapter.OnItemClickListener onItemClickListener){
            if (onItemClickListener != null ){
                itemView.setOnClickListener(v -> onItemClickListener.onItemClick(noteId));
            }
        }
    }

    public NoteListAdapter(Context context, OnItemClickListener onItemClickListener) {
        layoutInflater = LayoutInflater.from(context);
       this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public NoteListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NoteListHolder(layoutInflater.inflate(R.layout.recyclerview_item,
                viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListHolder holder, int position) {
        Note current = notes.get(position);
        char[] text = BytesConverter.bytesToChar(current.getTitle());
        holder.recyclerviewItemTitle.setText(text,0,text.length);
        holder.onBind(current.getId(), onItemClickListener);
    }

    public void setNotes (List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public interface OnItemClickListener {
        void onItemClick (int nodeId);
    }
}


