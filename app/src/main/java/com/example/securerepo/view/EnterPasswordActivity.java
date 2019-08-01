package com.example.securerepo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.securerepo.R;


public class EnterPasswordActivity extends Activity {

    private Button btDecrypt;
    private EditText etEnterPassword;
    private final String PASSWORD = "password";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        etEnterPassword = findViewById(R.id.enterPasswordActivityEditText);
        btDecrypt = findViewById(R.id.enterPasswordActivityButtonEnter);
        btDecrypt.setOnClickListener(btDecryptListener);


    }

    View.OnClickListener btDecryptListener = v -> {
        char [] password = new char[etEnterPassword.length()];
        if (etEnterPassword.length() != 0){
            etEnterPassword.getText().getChars(0, etEnterPassword.length(), password, 0 );
        }
        Intent intent = new Intent(this, RecyclerViewNoteListActivity.class);
        intent.putExtra(PASSWORD, password);
      //  Arrays.fill(password,'0');
        startActivity(intent);
    };



}
