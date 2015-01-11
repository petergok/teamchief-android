package com.teamchief.petergok.teamchief.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.util.Log;

import com.google.gson.Gson;
import com.teamchief.petergok.teamchief.Constants;
import com.teamchief.petergok.teamchief.activities.delegate.ActivityDelegate;
import com.teamchief.petergok.teamchief.fragments.ConversationPageFragment;
import com.teamchief.petergok.teamchief.gson.GsonMessage;
import com.teamchief.petergok.teamchief.gson.GsonTeam;
import com.teamchief.petergok.teamchief.model.ConversationContentProvider;
import com.teamchief.petergok.teamchief.model.MessagesTable;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 2015-01-11.
 */
public class SendMessageTask extends BaseTask {

    private ActivityDelegate mDelegate;
    private String mText;
    private String mUserName;
    private String mPassword;
    private String mTeamId;
    private boolean mAdded;

    public SendMessageTask(ActivityDelegate delegate, String text, String userName,
                                  String password, String teamId, long delayTime, boolean added) {
        super (delayTime, true);
        mAdded = added;
        mDelegate = delegate;
        mText = text;
        mUserName = userName;
        mPassword = password;
        mTeamId = teamId;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (!mAdded) {
            createNewMessage();
        }

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "none";
        String uri = Constants.SERVER_URL + "/message";
        try {
            HttpPost method = new HttpPost(uri);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("text", mText));
            nameValuePairs.add(new BasicNameValuePair("teamId", mTeamId));
            nameValuePairs.add(new BasicNameValuePair("password", "" + mPassword));
            nameValuePairs.add(new BasicNameValuePair("username", mUserName));
            method.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            response = httpclient.execute(method);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
            mDelegate.checkNetworkConnection();
        }
        return responseString;
    }

    public void createNewMessage() {
        ContentResolver cr = mDelegate.getActivity().getContentResolver();

        ContentValues newValues = new ContentValues();
        newValues.put(MessagesTable.COLUMN_MESSAGE_ID, "N/A");
        newValues.put(MessagesTable.COLUMN_SENDER, mUserName);
        newValues.put(MessagesTable.COLUMN_SEND_TIME, System.currentTimeMillis());
        newValues.put(MessagesTable.COLUMN_TEXT, mText);
        newValues.put(MessagesTable.COLUMN_TEAM_ID, mTeamId);
        newValues.put(MessagesTable.COLUMN_LOCAL, MessagesTable.TRUE);

        cr.insert(ConversationContentProvider.CONTENT_URI, newValues);
    }

    @Override
    public Runnable getRetryRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                new SendMessageTask(mDelegate, mText, mUserName, mPassword,
                        mTeamId, getNewDelay(), true).execute();
            }
        };
    }
}
