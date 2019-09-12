package com.example.securerepo.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.securerepo.BuildConfig;
import com.example.securerepo.R;
import com.example.securerepo.application.App;


public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPref;
    private Preference prefChangePassword;
    private Preference prefDeleteNotes;
    private Preference prefAbout;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        prefChangePassword = findPreference("pref_change_password");
        prefDeleteNotes = findPreference("pref_delete_notes");
        prefAbout = findPreference("pref_about");

        setPrefListeners();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        return super.onPreferenceTreeClick(preference);
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

        prefDeleteNotes.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                return true;
            }
        });

        prefAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                return true;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPref.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
