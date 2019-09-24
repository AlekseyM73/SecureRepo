package com.alekseymakarov.locknote.view;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import com.alekseymakarov.locknote.R;



public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPref;
    private Preference prefChangePassword;
    private Preference prefAbout;
    private Preference prefPrivacy;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.preferences);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        prefChangePassword = findPreference("pref_change_password");
        prefAbout = findPreference("pref_about");
        prefPrivacy = findPreference("pref_privacy");

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

        prefAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showAboutDialog();
                return true;
            }
        });

        prefPrivacy.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/locknote/privacy"));
            startActivity(intent);
            return true;
        });
    }

    private void showAboutDialog() {
        new AboutDialogFragment().show(getFragmentManager(),"tag");
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPref.unregisterOnSharedPreferenceChangeListener(listener);
    }

}
