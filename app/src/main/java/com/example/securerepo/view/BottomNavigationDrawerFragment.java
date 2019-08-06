package com.example.securerepo.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.securerepo.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       NavigationView navigationView =  view.findViewById(R.id.fragment_bottom_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case (R.id.bottom_app_bar_menu_settings):{
                        Toast.makeText(getContext(), "settings", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case (R.id.bottom_app_bar_menu_block):{
                        Toast.makeText(getContext(), "block", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case (R.id.bottom_app_bar_menu_exit):{
                        Toast.makeText(getContext(), "exit", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:break;
                }
                return true;
            }
        });
    }
}
