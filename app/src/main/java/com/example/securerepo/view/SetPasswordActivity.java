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
            char[] password1 = new char[etSetPass.length()];
            char[] password2 = new char[etSetPassRepeat.length()];
            etSetPass.getText().getChars(0, etSetPass.length(), password1, 0);
            etSetPassRepeat.getText().getChars(0, etSetPassRepeat.length(), password2, 0);

            if (isTwoPasswordEquals(password1, password2) && isPasswordLengthGood(password1)) {
                etSetPass.setText("");
                etSetPassRepeat.setText("");
                startNextScreen(password1);
               /* Arrays.fill(password1,'0');
                Arrays.fill(password2,'0');*/
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
        //  Arrays.fill(password,'0');
        startActivity(intent);
    }
}
