package com.example.securerepo.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.securerepo.R;
import com.example.securerepo.viewmodel.RecyclerViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RecyclerViewNoteListActivity extends AppCompatActivity {

    private static final String TAG = RecyclerViewNoteListActivity.class.getSimpleName();
    private Button fab;
    RecyclerView recyclerView;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private RecyclerViewModel recyclerViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_item);

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        final NoteListAdapter adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewModel = ViewModelProviders.of (this).get(RecyclerViewModel.class);

        disposable.add(recyclerViewModel.getAllTitles().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> adapter.setTitles(bytes),
                throwable -> Log.e(TAG, "Unable to load", throwable)));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
