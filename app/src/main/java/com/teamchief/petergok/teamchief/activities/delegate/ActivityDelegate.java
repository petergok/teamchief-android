package com.teamchief.petergok.teamchief.activities.delegate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.teamchief.petergok.teamchief.Constants;
import com.teamchief.petergok.teamchief.model.ConversationContentProvider;
import com.teamchief.petergok.teamchief.model.MessagesTable;
import com.teamchief.petergok.teamchief.tasks.GetTeamTask;
import com.teamchief.petergok.teamchief.tasks.SendRegistrationIdTask;

import java.io.IOException;

/**
 * Created by Peter on 2015-01-10.
 */
public class ActivityDelegate {
    protected final static String TAG = "MessageListActivity";

    private static final String PROPERTY_REG_ID = "registrationId";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_USERNAME = "username";
    private static final String PROPERTY_PASSWORD = "password";

    public final static String NEW_MESSAGE = "new_message";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private GoogleCloudMessaging gcm;
    private String regid;

    protected Context mContext;

    protected AlertDialog mAlertDialog;

    private Activity mActivity;

    public ActivityDelegate(Activity activity) {
        mActivity = activity;
    }

    /**
     * A broadcast reciever that is used to recieve broadcasts from the GCM reciever
     */
    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(NEW_MESSAGE)) {
                if (!loggedIn()) {
                    return;
                }

                //String text = intent.getStringExtra("text");
                //long sendTime = intent.getLongExtra("sendTime", System.currentTimeMillis());
                //String senderName = intent.getStringExtra("senderName");
                String teamId = intent.getStringExtra("teamId");

                getNewMessages(teamId);
            }
        }
    };

    public Activity getActivity() {
        return mActivity;
    }

    public void getNewMessages(String teamId) {
        cleanTeam(teamId);

        String where = MessagesTable.COLUMN_TEAM_ID + " = ? AND " + MessagesTable.COLUMN_SEND_TIME
                + " >= (select max(" + MessagesTable.COLUMN_SEND_TIME + ") from "
                + MessagesTable.TABLE_MESSAGES + " where " + MessagesTable.COLUMN_TEAM_ID + " = ? )";

        Cursor cursor = mActivity.getContentResolver().query(ConversationContentProvider.CONTENT_URI,
                new String[] {MessagesTable.COLUMN_SEND_TIME, MessagesTable.COLUMN_MESSAGE_ID}, where,
                new String[] {teamId, teamId}, null);

        cursor.moveToFirst();
        if (cursor.isAfterLast()) {
            new GetTeamTask(this, getUsername(), getPassword(), teamId, 0, 0, "").execute();
        } else {
            long sendTime = cursor.getLong(0);
            String messageId = cursor.getString(1);
            new GetTeamTask(this, getUsername(), getPassword(), teamId, sendTime, 0,
                    messageId).execute();

        }

        cursor.close();
    }

    public void cleanTeam(String teamId) {
        mActivity.getContentResolver().delete(ConversationContentProvider.CONTENT_URI,
                MessagesTable.COLUMN_TEAM_ID + " = ? AND " + MessagesTable.COLUMN_LOCAL + " = ?",
                new String[]{teamId, "" + MessagesTable.TRUE});
    }

    public void onCreate(Bundle savedInstanceState) {
        // Register the broadcast reciever
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(mActivity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NEW_MESSAGE);
        bManager.registerReceiver(bReceiver, intentFilter);

        mContext = mActivity.getApplicationContext();
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(mActivity);
            regid = getRegistrationId(mContext);

            if (regid.isEmpty()) {
                registerInBackground();
            } else {
                sendRegistrationIdToBackend(10);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    public void onResume() {
        checkPlayServices();
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, mActivity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                mActivity.finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    public void registerInBackground() {
        new AsyncTask() {
            @Override
            protected String doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    regid = gcm.register(Constants.SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    if (loggedIn()){
                        sendRegistrationIdToBackend(10);
                    }

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(mContext, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(Object result) {
                Log.i(TAG, result + "\n");
            }
        }.execute(null, null, null);
    }

    public boolean loggedIn() {
        return !getUsername().equals("") && !getPassword().equals("");
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    public void sendRegistrationIdToBackend(long delayTime) {
        new SendRegistrationIdTask(this, regid, getUsername(), getPassword(), delayTime).execute();
    }

    public String getUsername() {
        final SharedPreferences prefs = getGCMPreferences(mContext);
        return prefs.getString(PROPERTY_USERNAME, "");
    }

    public String getPassword() {
        final SharedPreferences prefs = getGCMPreferences(mContext);
        return prefs.getString(PROPERTY_PASSWORD, "");
    }

    public void login(String username, String password) {
        final SharedPreferences prefs = getGCMPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_USERNAME, username);
        editor.putString(PROPERTY_PASSWORD, password);
        editor.commit();
    }

    public void logout() {
        login("", "");
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    public void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    public SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return mActivity.getSharedPreferences(mActivity.getClass().getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * Checks the network connection and if it isn't available, notify the user that they need to connect to the internet
     */
    public void checkNetworkConnection() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Check if the network is available and notify the user of the device state accordingly
                if (isNetworkAvailable()) {
                    Toast.makeText(mActivity, "An error occured while connecting", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_DARK);
                    mAlertDialog = builder.setTitle("Network Error").setMessage("Please make sure that you are connected to the internet.")
                            .setPositiveButton("OK", null).create();
                    mAlertDialog.show();
                }
            }
        });
    }

    /**
     * Checks if the network is available
     *
     * @return if the network is available
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
