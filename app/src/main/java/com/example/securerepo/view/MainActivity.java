package com.example.securerepo.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.securerepo.R;

public class MainActivity extends Activity {

   private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("com.alekseym73.securerepo", MODE_PRIVATE);
        // Check first run if true launch SetPasswordFragment
        // else launch EnterPasswordFragment
        if (sharedPreferences.getBoolean("firstRun", true)&& !sharedPreferences.
                getBoolean("isPasswordPresent", false)){
            sharedPreferences.edit().putBoolean("firstRun", false).commit();
            Intent intent = new Intent(this, SetPasswordActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, EnterPasswordActivity.class);
            startActivity(intent);
        }


    }
}
