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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.seniorproject.mainseniorprojectpart.LoginActivity;
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


    /////////////////////////test code
    //Views
    private EditText editTextEmail;
    private EditText editTextName;

    ///////////////////////end test code


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();

        v = inflater.inflate(R.layout.dialog_login,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());




        /////////////////////////////////////////////////////////////////Test
        super.onCreate(savedInstanceState);

        //Initializing views

        //If the user is already logged in
        //Starting chat room
        if(AppController.getInstance().isLoggedIn()){
            getActivity().finish();

            startActivity(new Intent(getActivity(), MainActivity.class));
        }
        ///////////////////////////////////////////////////////////////end test code

        //For the sign in button
        builder.setView(v).setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                editTextEmail = (EditText) getDialog().findViewById(R.id.loginEmailTextField);
                editTextName = (EditText) getDialog().findViewById(R.id.passwordTextField);
                registerUser();
                // we need to add checker instead here when we add the register functionality
                startActivity(new Intent(getActivity(), MainActivity.class));

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }

    //Method to register user
    private void registerUser() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Entering chat room");
        progressDialog.show();

        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        try {
                            JSONObject obj = new JSONObject(response);
                            int id = obj.getInt("id");
                            String name = obj.getString("name");
                            String email = obj.getString("email");

                            //Login user
                            AppController.getInstance().loginUser(id,name,email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    @Override
    public void onResume() {
        super.onResume();
        //Checking if user is logged in
        if(AppController.getInstance().isLoggedIn()){
            getActivity().finish();
            startActivity(new Intent(getActivity(), MainActivity.class));

        }
    }


}
