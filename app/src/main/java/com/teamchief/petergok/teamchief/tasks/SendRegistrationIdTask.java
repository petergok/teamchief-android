package com.teamchief.petergok.teamchief.tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.teamchief.petergok.teamchief.Constants;
import com.teamchief.petergok.teamchief.activities.BaseActivity;
import com.teamchief.petergok.teamchief.activities.MessageListActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 2015-01-10.
 */
public class SendRegistrationIdTask extends BaseTask {
    private BaseActivity mActivity;
    private String mGcmId;
    private String mUserName;
    private String mPassword;

    public SendRegistrationIdTask(BaseActivity activity, String gcmId, String userName,
                                  String password, long delayTime) {
        super (delayTime, true);
        mGcmId = gcmId;
        mActivity = activity;
        mUserName = userName;
        mPassword = password;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "none";
        String uri = Constants.SERVER_URL + "/gcmId";
        try {
            HttpPost method = new HttpPost(uri);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("gcmId", mGcmId));
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
            mActivity.checkNetworkConnection();
        }
        return responseString;
    }

    @Override
    public Runnable getRetryRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                mActivity.sendRegistrationIdToBackend(getNewDelay());
            }
        };
    }
}
