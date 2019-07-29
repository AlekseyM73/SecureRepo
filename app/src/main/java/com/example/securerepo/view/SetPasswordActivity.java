package com.example.securerepo.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.securerepo.R;

import java.util.Arrays;

public class SetPasswordActivity extends Activity {

    private EditText etSetPass;
    private EditText etSetPassRepeat;
    private static final int PASSWORD_LENGTH = 8;
    private final String IS_PASSWORD_PRESENT = "isPasswordPresent";
    private final String PASSWORD = "password";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        etSetPass = findViewById(R.id.setPasswordActivityEditText);
        etSetPassRepeat = findViewById(R.id.setPasswordActivityEditTextRepeat);
        Button btnSave = findViewById(R.id.setPasswordActivityButtonSave);
        btnSave.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int passLength1 = etSetPass.length();
            int passLength2 = etSetPassRepeat.length();
            char[] password1 = new char[passLength1];
            char[] password2 = new char[passLength2];
            etSetPass.getText().getChars(0, passLength1, password1, 0);
            etSetPassRepeat.getText().getChars(0, passLength2, password2, 0);

            if (isTwoPasswordEquals(password1, password2) && isPasswordLengthGood(password1)) {
                etSetPass.setText("empty");
                etSetPassRepeat.setText("empty");
                startNextScreen(password1);
                finish();
            }

        }
    };

    private boolean isTwoPasswordEquals(char[] password1, char[] password2) {
        return Arrays.equals(password1, password2);
    }

    private boolean isPasswordLengthGood(char[] password1) {
        return password1.length >= PASSWORD_LENGTH;
    }

    private void startNextScreen(char[] password) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.alekseym73.securerepo", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(IS_PASSWORD_PRESENT, true).commit();
        Intent intent = new Intent(this, RecyclerViewNoteListActivity.class);
        intent.putExtra(PASSWORD, password);
        startActivity(intent);
    }
}
