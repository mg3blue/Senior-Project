package layout;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.seniorproject.mainseniorprojectpart.R;
import com.app.seniorproject.mainseniorprojectpart.gcm.GCMRegistrationIntentService;
import com.app.seniorproject.mainseniorprojectpart.helper.AppController;
import com.app.seniorproject.mainseniorprojectpart.helper.Constants;
import com.app.seniorproject.mainseniorprojectpart.helper.Message;
import com.app.seniorproject.mainseniorprojectpart.helper.ThreadAdapter;
import com.app.seniorproject.mainseniorprojectpart.helper.URLs;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ChatPage extends Fragment {

    //Broadcast receiver to receive broadcasts
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    //Progress dialog
    private ProgressDialog dialog;

    //Recyclerview objects
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //ArrayList of messages to store the thread messages
    private ArrayList<Message> messages;

    //Button to send new message on the thread
    private Button buttonSend;

    //EditText to send new message on the thread
    private EditText editTextMessage;

    public ChatPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //Displaying dialog while the chat room is being ready
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Opening chat room");
        dialog.show();

        //Initializing recyclerview
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //Initializing message arraylist
        messages = new ArrayList<>();

        //Calling function to fetch the existing messages on the thread
        fetchMessages();

        //initializing button and edittext
        buttonSend = (Button) getActivity().findViewById(R.id.buttonSend);
        editTextMessage = (EditText) getActivity().findViewById(R.id.editTextMessage);

        //buttonSend.setOnClickListener();
        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (view == buttonSend)
                    sendMessage();
            }
        });

        //Adding listener to button
        //buttonSend.setOnClickListener(getActivity());

        //Creating broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {

                    //When gcm registration is success do something here if you need

                }  else if (intent.getAction().equals(Constants.PUSH_NOTIFICATION)) {
                    //Getting message data
                    String name = intent.getStringExtra("name");
                    String message = intent.getStringExtra("message");
                    String id = intent.getStringExtra("id");

                    //processing the message to add it in current thread
                    processMessage(name, message, id);
                }
            }
        };

        //if the google play service is not in the device app won't work
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());

        if (ConnectionResult.SUCCESS != resultCode) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getActivity().getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getActivity().getApplicationContext());

            } else {
                Toast.makeText(getActivity().getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            Intent itent = new Intent(getActivity(), GCMRegistrationIntentService.class);
            getActivity().startService(itent);
        }
    }


    ///////////////////////////////////////////
    //This method will fetch all the messages of the thread
    private void fetchMessages() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_FETCH_MESSAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray thread = res.getJSONArray("messages");
                            for (int i = 0; i < thread.length(); i++) {
                                JSONObject obj = thread.getJSONObject(i);
                                int userId = obj.getInt("userid");
                                String message = obj.getString("message");
                                String name = obj.getString("name");
                                String sentAt = obj.getString("sentat");
                                Message messagObject = new Message(userId, message, sentAt, name);
                                messages.add(messagObject);
                            }

                            adapter = new ThreadAdapter(getActivity(), messages, AppController.getInstance().getUserId());
                            recyclerView.setAdapter(adapter);
                            scrollToBottom();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    //Processing message to add on the thread
    private void processMessage(String name, String message, String id) {
        Message m = new Message(Integer.parseInt(id), message, getTimeStamp(), name);
        messages.add(m);
        scrollToBottom();
    }

    //This method will send the new message to the thread
    private void sendMessage() {
        final String message = editTextMessage.getText().toString().trim();
        if (message.equalsIgnoreCase(""))
            return;
        int userId = AppController.getInstance().getUserId();
        String name = AppController.getInstance().getUserName();
        String sentAt = getTimeStamp();

        Message m = new Message(userId, message, sentAt, name);
        messages.add(m);
        adapter.notifyDataSetChanged();

        scrollToBottom();

        editTextMessage.setText("");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SEND_MESSAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                params.put("id", String.valueOf(AppController.getInstance().getUserId()));
                params.put("message", message);
                params.put("name", AppController.getInstance().getUserName());
                return params;
            }
        };

        //Disabling retry to prevent duplicate messages
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    //method to scroll the recyclerview to bottom
    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1)
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
    }

    //This method will return current timestamp
    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    //Registering broadcast receivers
    @Override
    public void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_TOKEN_SENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.PUSH_NOTIFICATION));
    }


    //Unregistering receivers
    @Override
    public void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


}
