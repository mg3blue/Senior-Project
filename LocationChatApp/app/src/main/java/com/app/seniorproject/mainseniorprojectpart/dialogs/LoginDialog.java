package com.app.seniorproject.mainseniorprojectpart.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.seniorproject.mainseniorprojectpart.LoginDialogComm;
import com.app.seniorproject.mainseniorprojectpart.MainActivity;


import com.app.seniorproject.mainseniorprojectpart.helper.AppController;
import com.app.seniorproject.mainseniorprojectpart.R;
import com.app.seniorproject.mainseniorprojectpart.helper.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aerodrain on 11/15/2016.
 */

public class LoginDialog extends DialogFragment {

    LayoutInflater inflater;
    View v;

    private EditText editTextEmail;
    private EditText editTextPW;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_login,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        super.onCreate(savedInstanceState);

        //For the sign in button
        builder.setView(v).setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent intent = new Intent(getActivity(), MainActivity.class);


                editTextEmail = (EditText) getDialog().findViewById(R.id.loginEmailTextField);
                editTextPW = (EditText) getDialog().findViewById(R.id.loginPasswordTextField);

                final String email = editTextEmail.getText().toString().trim();
                final String password = editTextPW.getText().toString().trim();

                LoginDialogComm comm = (LoginDialogComm) getActivity();
                comm.loginUser(email, password, getContext());


            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }
}
