package com.app.seniorproject.mainseniorprojectpart.gcm;

/**
 * Created by Blue on 10/31/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;


import com.app.seniorproject.mainseniorprojectpart.helper.Constants;
import com.app.seniorproject.mainseniorprojectpart.helper.NotificationHandler;
import com.google.android.gms.gcm.GcmListenerService;

public class GCMPushReceiverService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String title = data.getString("title");
        String id = data.getString("id");
        sendNotification(message, title, id);
    }

    private void sendNotification(String message, String title, String id) {
        //Creating a broadcast intent
        Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
        //Adding notification data to the intent
        pushNotification.putExtra("message", message);
        pushNotification.putExtra("name", title);
        pushNotification.putExtra("id", id);

        //We will create this class to handle notifications
        NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());

        //If the app is in foreground
        if (!NotificationHandler.isAppIsInBackground(getApplicationContext())) {
            //Sending a broadcast to the chatroom to add the new message
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        } else {
            //If app is in foreground displaying push notification
            notificationHandler.showNotificationMessage(title, message);
        }
    }
}