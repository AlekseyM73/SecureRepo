package com.example.securerepo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.securerepo.R;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListHolder>  {

    private final LayoutInflater layoutInflater;
    private List<byte []> titles;

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

    public void setTitles (List<byte []> titles){
        this.titles = titles;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
