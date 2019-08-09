package com.example.securerepo.view.RecyclerViewHelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securerepo.R;
import com.example.securerepo.model.Note;
import com.example.securerepo.utils.BytesConverter;

import java.util.Collections;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListHolder>  {

    private final LayoutInflater layoutInflater;
    private List<Note> notes = Collections.emptyList();
    private final OnItemClickListener onItemClickListener;
    private SelectionTracker selectionTracker;

    class NoteListHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private final TextView recyclerviewItemTitle;

        public NoteListHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            recyclerviewItemTitle = itemView.findViewById(R.id.recyclerviewItemTitle);
        }

        public ItemDetailsLookup.ItemDetails getItemDetails(){
            return new NoteItemDetail(getAdapterPosition(),notes.get(getAdapterPosition()));
        }

        public void onBind (int noteId, NoteListAdapter.OnItemClickListener onItemClickListener,
                            boolean isActive){
            itemView.setActivated(isActive);
            if (onItemClickListener != null ){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(noteId);

                    }
                });
            }
        }
    }

    public NoteListAdapter(Context context, OnItemClickListener onItemClickListener) {
        layoutInflater = LayoutInflater.from(context);
       this.onItemClickListener = onItemClickListener;
    }

    public void setSelectionTracker (SelectionTracker selectionTracker){
        this.selectionTracker = selectionTracker;
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
        holder.onBind(current.getId(), onItemClickListener,
                selectionTracker.isSelected(current));
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


