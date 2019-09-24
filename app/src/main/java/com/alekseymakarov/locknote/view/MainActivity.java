package com.alekseymakarov.locknote.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alekseymakarov.locknote.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("com.alekseym73.locknote", MODE_PRIVATE);
        // Check first run if true launch SetPasswordFragment
        // else launch EnterPasswordFragment
        String IS_FIRST_RUN = "isFirstRun";
        String IS_PASSWORD_PRESENT = "isPasswordPresent";
        if (sharedPreferences.getBoolean(IS_FIRST_RUN, true) || !sharedPreferences.
                getBoolean(IS_PASSWORD_PRESENT, false)) {
            sharedPreferences.edit().putBoolean(IS_FIRST_RUN, false).commit();
            Intent intent = new Intent(this, SetPasswordActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, EnterPasswordActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
