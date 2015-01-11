package com.teamchief.petergok.teamchief.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.adapters.MessageCursorAdapter;
import com.teamchief.petergok.teamchief.model.ConversationContentProvider;
import com.teamchief.petergok.teamchief.model.MessagesTable;

/**
 * Created by Peter on 2015-01-10.
 */

public class ConversationPageFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private Activity mActivity;
    private MessageCursorAdapter mAdapter;
    private ListView mListView;
    private String mTeamId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTeamId = getArguments().getString("teamId");
        mActivity = getActivity();

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_messages_list, container, false);
        mListView = (ListView) container.findViewById(android.R.id.list);

        refreshList();

        return rootView;
    }

    private void refreshList() {
        getLoaderManager().initLoader(0, null, this);
        mAdapter = new MessageCursorAdapter(mActivity, null, 0);

        setListAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = MessagesTable.getFullProjection();
        CursorLoader cursorLoader = new CursorLoader(mActivity,
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
