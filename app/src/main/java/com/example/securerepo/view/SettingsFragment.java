package com.example.securerepo.view;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.example.securerepo.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

}
