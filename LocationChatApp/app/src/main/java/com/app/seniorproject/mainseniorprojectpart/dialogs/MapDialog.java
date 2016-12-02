package com.app.seniorproject.mainseniorprojectpart.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.app.seniorproject.mainseniorprojectpart.MainActivity;
import com.app.seniorproject.mainseniorprojectpart.R;

/**
 * Created by Aerodrain on 11/28/2016.
 */

public class MapDialog extends DialogFragment {

    LayoutInflater inflater;
    View v;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();

        v = inflater.inflate(R.layout.dialog_map, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(v).setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            //get text from box and send to map activity and place in marker

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }
}
