package com.teamchief.petergok.teamchief.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.activities.TeamViewActivity;
import com.teamchief.petergok.teamchief.adapters.MessageCursorAdapter;
import com.teamchief.petergok.teamchief.model.ConversationContentProvider;
import com.teamchief.petergok.teamchief.model.MessagesTable;

/**
 * Created by Peter on 2015-01-10.
 */

public class ConversationPageFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private TeamViewActivity mActivity;
    private MessageCursorAdapter mAdapter;
    private ListView mListView;
    private String mTeamId;
    private EditText messageBodyField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTeamId = getArguments().getString("teamId");
        mActivity = (TeamViewActivity)getActivity();

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_conversation, container, false);
        mListView = (ListView) container.findViewById(android.R.id.list);

        refreshList();

        messageBodyField = (EditText) rootView.findViewById(R.id.messageBodyField);
        rootView.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        return rootView;
    }

    private void sendMessage() {
        String messageBody = messageBodyField.getText().toString();
        if (messageBody.isEmpty()) {
            Toast.makeText(mActivity, "Please enter a message", Toast.LENGTH_LONG).show();
            return;
        }

        messageBodyField.setText("");
    }

    private void refreshList() {
        getLoaderManager().initLoader(0, null, this);
        mAdapter = new MessageCursorAdapter(mActivity, null, 0, mActivity.getDelegate());

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
