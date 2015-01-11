package com.teamchief.petergok.teamchief.gcm;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.teamchief.petergok.teamchief.Constants;
import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.activities.delegate.ActivityDelegate;

/**
 * Created by Peter on 2015-01-08.
 */
public class GcmIntentService extends IntentService {

    private static String TAG = "GcmIntentService";

    private static String TYPE_TEXT_MESSAGE = "TEXT_MESSAGE";

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private String mUsername;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public GcmIntentService() {
        super(Constants.SENDER_ID);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mUsername = getUsername();
    }

    public String getUsername() {
        final SharedPreferences prefs = getGCMPreferences(this);
        return prefs.getString(ActivityDelegate.PROPERTY_USERNAME, "");
    }

    public SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(Activity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Get message data from the intent
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            // Filter messages based on message type
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.d(TAG, "Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.d(TAG, "Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.d(TAG, "Received: " + extras.toString());
                if (!extras.getString("senderName").equals(mUsername)) {
                    sendNotification(extras.getString("text"));
                }
                parseAndReportMessage(extras);
            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Activity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Called when a message is successfully received, it parses and reports the message to the main activity
     *
     * @param data the message data
     */
    private void parseAndReportMessage(Bundle data) {
        try {

            // Retrieve the message type
            String messageType = data.getString("messageType");
            if (messageType == null) {
                return;
            }

            // Based on the message type, retrieve the data associated with it and send a broadcast to the main activity
            if (messageType.equals(TYPE_TEXT_MESSAGE)) {
                String text = data.getString("text");
                long sendTime = Long.parseLong(data.getString("sendTime"));
                String senderName = data.getString("senderName");
                String teamId = data.getString("teamId");

                Intent intent = new Intent(ActivityDelegate.NEW_MESSAGE);
                intent.putExtra("text", text);
                intent.putExtra("sendTime", sendTime);
                intent.putExtra("senderName", senderName);
                intent.putExtra("teamId", teamId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
        } catch (Exception e) {
            Log.e("EXCEPTION", "exception: ", e);
        }
    }
}
