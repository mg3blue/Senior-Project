package com.app.seniorproject.mainseniorprojectpart.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.seniorproject.mainseniorprojectpart.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import layout.MyDialogCallbackContract;

/**
 * Created by Aerodrain on 11/28/2016.
 */

public class MapDialog extends DialogFragment {



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList<String> colorsList = new ArrayList<>();

        colorsList.add("Red");
        colorsList.add("Blue");
        colorsList.add("Green");

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_map, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                EditText editMessageField = (EditText) getDialog().findViewById(R.id.mapMarkerText);
                String textForBlah = editMessageField.getText().toString();

                ListView colors = (ListView) getDialog().findViewById(R.id.colorListView);
//
//                SimpleAdapter adapter = new SimpleAdapter(this, colorsList, android.R.layout.)
//
//                colors.setAdapter(colors.getAdapter());
////                String colorString = new String();
//
//                colors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//                        String colorString = (String) colors.getItemAtPosition(position);
//                        System.out.println(colorString);
//                        Toast.makeText(getActivity(), colorString,
//                                Toast.LENGTH_SHORT).show();
//
//                    }});



//                System.out.println(colorString[0]);

                MyDialogCallbackContract mHost = (MyDialogCallbackContract)getTargetFragment();
                mHost.methodToPassDataBackToFragment(textForBlah);


            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return builder.create();
    }


}
