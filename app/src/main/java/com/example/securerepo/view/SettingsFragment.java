package com.example.securerepo.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.securerepo.BuildConfig;
import com.example.securerepo.R;
import com.example.securerepo.application.App;

import java.util.Calendar;
import java.util.Date;


public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPref;
    private Preference prefChangePassword;
    private Preference prefAbout;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private AlertDialog alertDialog;
    private boolean isAboutDialogWasShown;
    private final String IS_ABOUT_DIALOG_WAS_SHOWN = "isAboutDialogWasShown";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        if (savedInstanceState != null){
            isAboutDialogWasShown = savedInstanceState.getBoolean(IS_ABOUT_DIALOG_WAS_SHOWN);
        }

        addPreferencesFromResource(R.xml.preferences);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        prefChangePassword = findPreference("pref_change_password");
        prefAbout = findPreference("pref_about");

        setPrefListeners();
        if (isAboutDialogWasShown){
            showAboutDialog();
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_ABOUT_DIALOG_WAS_SHOWN, isAboutDialogWasShown);

    }



    @Override
    public void onResume() {
        super.onResume();
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {
                    case "pref_dark_theme": {
                        if (sharedPref.getBoolean("pref_dark_theme", false)) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        }
                        break;
                    }
                    default:break;
                }

            }
        };
        sharedPref.registerOnSharedPreferenceChangeListener(listener);
    }

    private void setPrefListeners (){
        prefChangePassword.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(SettingsFragment.this.getContext(), ChangePasswordActivity.class);
            SettingsFragment.this.startActivity(intent);
            SettingsFragment.this.getActivity().finish();
            return true;
        });

        prefAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showAboutDialog();
                return true;
            }
        });
    }

    private void showAboutDialog() {
        isAboutDialogWasShown = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("SECURE REPO")
                .setMessage("ver. 1.0\nAleksey Makarov "+ getYear() )
                .setCancelable(false)
                .setPositiveButton(getString(R.string.button_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isAboutDialogWasShown = false;
                        return;
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPref.unregisterOnSharedPreferenceChangeListener(listener);
    }

    private String getYear (){
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null){
            alertDialog.dismiss();
        }

    }
}
