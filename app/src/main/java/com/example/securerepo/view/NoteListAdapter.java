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

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListHolder>  {

    private final LayoutInflater layoutInflater;
    private List<Note> notes = Collections.emptyList();
  // private final OnItemClickListener onItemClickListener;
    private Context context;
    private List<Integer> selectedID = new ArrayList<>();

    class NoteListHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private final TextView recyclerviewItemTitle;
        private final View recyclerItem;

        public NoteListHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            recyclerviewItemTitle = itemView.findViewById(R.id.recyclerviewItemTitle);
            recyclerItem = itemView.findViewById(R.id.recyclerviewItem);
        }

       /* public void onBind (int noteId, NoteListAdapter.OnItemClickListener onItemClickListener){
            if (onItemClickListener != null ){
                itemView.setOnClickListener(v -> onItemClickListener.onItemClick(noteId));
            }
        }*/
    }

    public NoteListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
      // this.onItemClickListener = onItemClickListener;
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
       // holder.onBind(current.getId(), onItemClickListener);
        int id = current.getId();
        int color = holder.cardView.getCardBackgroundColor().getDefaultColor();
        Log.d("COLOR",color+"");
        if (selectedID.contains(id)){
            holder.cardView.setCardBackgroundColor(context.getResources()
                    .getColor(R.color.colorAccent));
        } else {
            holder.cardView.setCardBackgroundColor(context
                    .getResources().getColor(android.R.color.background_light));
        }
    }
    public List<Note> getNotesfromAdapter(){
        return notes;
    }

    public Note getNotefromAdapter (int position){
        return notes.get(position);
    }

    public void setNotes (List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }

    public void setSelectedID (List<Integer> id){
        this.selectedID = id;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    /*public interface OnItemClickListener {
        void onItemClick (int nodeId);
    }*/
}


