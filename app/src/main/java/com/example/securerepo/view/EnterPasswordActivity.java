package com.example.securerepo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.example.securerepo.R;


public class EnterPasswordActivity extends Activity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        // Pass
        Intent intent = new Intent(this, RecyclerViewNoteListActivity.class);
        startActivity(intent);

    }



}
