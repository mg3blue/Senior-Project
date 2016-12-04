package com.app.seniorproject.mainseniorprojectpart.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.app.seniorproject.mainseniorprojectpart.R;

import layout.MyDialogCallbackContract;

/**
 * Created by Aerodrain on 11/28/2016.
 */

public class MapDialog extends DialogFragment {

    MyDialogCallbackContract mHost = (MyDialogCallbackContract)getTargetFragment();
    private EditText editMessageField;
    private String textForMarker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_map, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editMessageField = (EditText) getDialog().findViewById(R.id.mapMarkerText);
                String textForBlah = editMessageField.getText().toString();
                mHost.methodToPassDataBackToFragment(textForMarker);


            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return builder.create();
    }


}
