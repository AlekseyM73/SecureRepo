package com.alekseymakarov.locknote.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.alekseymakarov.locknote.R;

import java.util.Calendar;
import java.util.Date;

public class AboutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return  new AlertDialog.Builder(getActivity())
                .setTitle("SECURE REPO")
                .setMessage("ver. 1.0\nAleksey Makarov "+ getYear() )
                .setCancelable(false)
                .setPositiveButton(getString(R.string.button_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        return;
                    }
                })
        .create();

    }

    private String getYear (){
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

}
