package com.teamchief.petergok.teamchief.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.teamchief.petergok.teamchief.model.ConversationContentProvider;
import com.teamchief.petergok.teamchief.adapters.MessageCursorAdapter;
import com.teamchief.petergok.teamchief.model.MessagesTable;
import com.teamchief.petergok.teamchief.R;


public class MessageListActivity extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private MessageCursorAdapter mAdapter;
    private Cursor mCursor;
    private ListView mListView;
    private String mTeamId = "54b1af3088a8f50200000004";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_list);
        mListView = (ListView) findViewById(android.R.id.list);
        login("peter", "pass");
        getNewMessages(mTeamId);
        refreshList();
    }

    private void refreshList() {
        getLoaderManager().initLoader(0, null, this);
        mAdapter = new MessageCursorAdapter(this, null, 0);

        setListAdapter(mAdapter);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = MessagesTable.getFullProjection();
        CursorLoader cursorLoader = new CursorLoader(this,
                ConversationContentProvider.CONTENT_URI, projection,
                MessagesTable.COLUMN_TEAM_ID + " = ?", new String[] {mTeamId},
                MessagesTable.COLUMN_SEND_TIME + " ASC");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
