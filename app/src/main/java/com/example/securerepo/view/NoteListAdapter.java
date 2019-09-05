package com.example.securerepo.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securerepo.R;
import com.example.securerepo.model.Note;
import com.example.securerepo.utils.BytesConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListHolder> {

    private final LayoutInflater layoutInflater;
    private List<Note> notes = Collections.emptyList();
    private Context context;
    private static List<Integer> selectedID = new ArrayList<>();

    class NoteListHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private final TextView recyclerviewItemTitle;
        private final TextView recyclerviewItemDate;
        private final View recyclerItem;

         NoteListHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            recyclerviewItemTitle = itemView.findViewById(R.id.recyclerviewItemTitle);
            recyclerItem = itemView.findViewById(R.id.recyclerviewItem);
            recyclerviewItemDate = itemView.findViewById(R.id.recyclerviewItemDate);
        }

    }

     NoteListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

    }

    @NonNull
    @Override
    public NoteListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NoteListHolder(layoutInflater.inflate(R.layout.recyclerview_item,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListHolder holder, int position) {
        Note current = notes.get(position);
        char[] text = BytesConverter.bytesToChar(current.getTitle());
        holder.recyclerviewItemTitle.setText(text, 0, text.length);
        holder.recyclerviewItemDate.setText(current.getDate());
        int id = current.getId();
        if (selectedID.contains(id)) {
            holder.cardView.setCardBackgroundColor(context.getResources()
                    .getColor(R.color.secondaryLightColor));
        } else {
            holder.cardView.setCardBackgroundColor(context
                    .getResources().getColor(R.color.primaryLightColor));
        }
    }

     List<Note> getNotesfromAdapter() {
        return notes;
    }

     Note getNotefromAdapter(int position) {
        return notes.get(position);
    }

     void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

     void setSelectedID(List<Integer> id) {
        selectedID = id;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

}


