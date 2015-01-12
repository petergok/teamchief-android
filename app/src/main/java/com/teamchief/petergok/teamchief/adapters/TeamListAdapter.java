package com.teamchief.petergok.teamchief.adapters;

/**
 * Created by KevinMichael on 2015-01-10.
 */

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.activities.delegate.ActivityDelegate;
import com.teamchief.petergok.teamchief.model.tables.MessagesTable;
import com.teamchief.petergok.teamchief.model.tables.TeamsTable;

import java.util.Date;

public class TeamListAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    private ActivityDelegate mDelegate;

    //private TextView mTeamNameView, mRecentMessageView, mTimestampView;
    private Typeface latoBlackTypeFace, regularLatoTypeFace;

    public TeamListAdapter(Context context, Cursor cursor, int flags, ActivityDelegate delegate) {
        super(context, cursor, flags);

        //Get Lato Fonts
        latoBlackTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/lato-black.ttf");
        regularLatoTypeFace = Typeface.createFromAsset(context.getAssets(),"fonts/lato-regular.ttf");

        /*//Get Text fields requiring font changes
        mTeamNameView =(TextView)mContext.findViewById(R.id.team_name);
        mRecentMessageView =(TextView)mContext.findViewById(R.id.recent_message);
        mTimestampView =(TextView) mContext.findViewById(R.id.timestamp_message);

        mTeamNameView.setTypeface(latoBlackTypeFace);
        mRecentMessageView.setTypeface(regularLatoTypeFace);
        mTimestampView.setTypeface(regularLatoTypeFace);*/

        mDelegate = delegate;
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.team_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView teamNameView = (TextView) view.findViewById(R.id.team_name);
        teamNameView.setTypeface(latoBlackTypeFace);
        TextView recentMessageView = (TextView) view.findViewById(R.id.recent_message);
        TextView timestampMessage = (TextView) view.findViewById(R.id.timestamp_message);

        String teamName = cursor.getString(cursor.getColumnIndex(TeamsTable.COLUMN_TEAM_NAME));
        String recentMessage = cursor.getString(cursor.getColumnIndex(TeamsTable.COLUMN_LAST_MESSAGE));
        if (TextUtils.isEmpty(recentMessage)) {
            recentMessage = "No messages to show";
        }
        String date = new Date(cursor.getLong(cursor.getColumnIndex(TeamsTable.COLUMN_LAST_ACTIVE))).toString();

        teamNameView.setText(teamName);
        recentMessageView.setText(recentMessage);
        timestampMessage.setText(date);
    }
}