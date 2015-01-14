package com.teamchief.petergok.teamchief.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.activities.delegate.ActivityDelegate;
import com.teamchief.petergok.teamchief.gson.GsonTeam;
import com.teamchief.petergok.teamchief.model.providers.ConversationContentProvider;
import com.teamchief.petergok.teamchief.model.providers.TeamsContentProvider;
import com.teamchief.petergok.teamchief.model.tables.MessagesTable;
import com.teamchief.petergok.teamchief.model.tables.TeamsTable;
import com.teamchief.petergok.teamchief.tasks.ChiefRestClient;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Peter on 2015-01-11.
 */
public class TeamListActivity extends ActionBarActivity {
    ActivityDelegate mDelegate = new ActivityDelegate(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);

        RequestParams params = new RequestParams();
        params.add("username", mDelegate.getUsername());
        params.add("password", mDelegate.getPassword());

        final Activity activity = this;

        ChiefRestClient.get("/teams", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(activity, "An error occurred, please login again", Toast.LENGTH_LONG);
                Log.d("Error", throwable.getMessage());
                logout();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("Response", responseString);
                syncTeams(responseString);
            }
        });

        setContentView(R.layout.activity_team_list);
    }

    public void syncTeams(String responseString) {
        HashSet<String> teamIds = getAllTeamIds();

        Gson gson = new Gson();
        GsonTeam[] teams = null;
        try {
            teams = gson.fromJson(responseString, GsonTeam[].class);
        } catch (Exception e) {
            Log.d("RESPONSE", e.toString());
        }

        if (teams != null) {
            List<ContentValues> insertTeams = new ArrayList<>();
            for (GsonTeam team : teams) {
                if (!teamIds.contains(team.id)) {
                    ContentValues newValues = new ContentValues();
                    newValues.put(TeamsTable.COLUMN_LAST_ACTIVE, team.latestActive);
                    if (team.messages.size() > 0) {
                        newValues.put(TeamsTable.COLUMN_LAST_MESSAGE, team.messages.get(0).text);
                    }
                    newValues.put(TeamsTable.COLUMN_TEAM_ID, team.id);
                    newValues.put(TeamsTable.COLUMN_TEAM_NAME, team.name);
                    insertTeams.add(newValues);
                }
            }

            ContentValues[] contentValues = new ContentValues[insertTeams.size()];
            mDelegate.getActivity().getContentResolver()
                    .bulkInsert(TeamsContentProvider.CONTENT_URI, insertTeams.toArray(contentValues));
        }
    }

    public HashSet<String> getAllTeamIds() {
        HashSet<String> teamIds = new HashSet<>();
        ContentResolver cr = mDelegate.getActivity().getContentResolver();
        Cursor c = cr.query(TeamsContentProvider.CONTENT_URI, new String[] {TeamsTable.COLUMN_TEAM_ID}, null, null, null);

        c.moveToFirst();
        while(!c.isAfterLast()) {
            teamIds.add(c.getString(0));
            c.moveToNext();
        }

        c.close();

        return teamIds;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    protected void onDestroy () {
        mDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        mDelegate.logoutLocal();
        mDelegate.clearAllDatabase();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public ActivityDelegate getDelegate() {
        return mDelegate;
    }
}
