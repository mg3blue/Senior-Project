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
import android.widget.Toast;

import com.app.seniorproject.mainseniorprojectpart.LoginDialogComm;
import com.app.seniorproject.mainseniorprojectpart.R;

/**
 * Created by Aerodrain on 11/15/2016.
 */

public class RegisterDialog extends DialogFragment {
    LayoutInflater inflater;
    View v;


    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextPW;
    private EditText editTextRPW;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_register, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        super.onCreate(savedInstanceState);

        builder.setView(v).setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                editTextEmail = (EditText) getDialog().findViewById(R.id.registerEmailTextField);
                editTextName = (EditText) getDialog().findViewById(R.id.registerUsernameTextField);
                editTextPW = (EditText) getDialog().findViewById(R.id.registerPasswordTextField);
                editTextRPW = (EditText) getDialog().findViewById(R.id.reEnterRegisterPasswordTextField);

                if(editTextPW.getText().toString().equals(editTextRPW.getText().toString())){
                    final String name = editTextName.getText().toString().trim();
                    final String email = editTextEmail.getText().toString().trim();
                    final String password = editTextPW.getText().toString().trim();

                    LoginDialogComm comm = (LoginDialogComm) getActivity();
                    comm.registerUser(name, email, password, getContext());
                }
                else
                {
                    Toast.makeText(getActivity(), "Passwords not matched ", Toast.LENGTH_SHORT)
                            .show();
                }


            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }
}
