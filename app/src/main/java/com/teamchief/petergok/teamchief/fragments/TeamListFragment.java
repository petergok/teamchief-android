package com.teamchief.petergok.teamchief.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.ListView;
import android.view.View;

import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.activities.LoginActivity;
import com.teamchief.petergok.teamchief.activities.TeamListActivity;
import com.teamchief.petergok.teamchief.activities.TeamViewActivity;
import com.teamchief.petergok.teamchief.adapters.MessageCursorAdapter;
import com.teamchief.petergok.teamchief.adapters.TeamListAdapter;
import com.teamchief.petergok.teamchief.activities.delegate.ActivityDelegate;
import com.teamchief.petergok.teamchief.model.providers.ConversationContentProvider;
import com.teamchief.petergok.teamchief.model.providers.TeamsContentProvider;
import com.teamchief.petergok.teamchief.model.tables.MessagesTable;
import com.teamchief.petergok.teamchief.model.tables.TeamsTable;


public class TeamListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private ActivityDelegate mDelegate;
    private TeamListActivity mActivity;
    private TeamListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (TeamListActivity)getActivity();
        mDelegate = mActivity.getDelegate();
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_team_list, container, false);

        refreshList();
        return rootView;
    }

    private void refreshList() {
        getLoaderManager().initLoader(0, null, this);
        mAdapter = new TeamListAdapter(mActivity, null, 0, mActivity.getDelegate());

        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor c = ((TeamListAdapter)l.getAdapter()).getCursor();
        c.moveToPosition(position);

        Intent intent = new Intent(mActivity, TeamViewActivity.class);
        intent.putExtra("teamId", c.getString(c.getColumnIndex(TeamsTable.COLUMN_TEAM_ID)));
        mActivity.startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = TeamsTable.getFullProjection();
        CursorLoader cursorLoader = new CursorLoader(mActivity,
                TeamsContentProvider.CONTENT_URI, projection, null, null,
                TeamsTable.COLUMN_LAST_ACTIVE + " ASC");
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
