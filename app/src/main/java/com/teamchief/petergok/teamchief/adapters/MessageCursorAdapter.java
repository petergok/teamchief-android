package com.teamchief.petergok.teamchief.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.activities.delegate.ActivityDelegate;
import com.teamchief.petergok.teamchief.model.tables.MessagesTable;

import java.util.Date;

/**
 * Created by Peter on 2015-01-07.
 */
public class MessageCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    private ActivityDelegate mDelegate;
    private String mUsername;

    // Default constructor
    public MessageCursorAdapter(Context context, Cursor cursor, int flags, ActivityDelegate delegate) {
        super(context, cursor, flags);
        mDelegate = delegate;
        mUsername = delegate.getUsername();
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.message_text);
        TextView senderTextView = (TextView) view.findViewById(R.id.sender_text);
        TextView dateTextView = (TextView) view.findViewById(R.id.date_text);

        String text = cursor.getString(cursor.getColumnIndex(MessagesTable.COLUMN_TEXT));
        String sender = cursor.getString(cursor.getColumnIndex(MessagesTable.COLUMN_SENDER));
        String date = new Date(cursor.getLong(cursor.getColumnIndex(MessagesTable.COLUMN_SEND_TIME))).toString();

        textView.setText(text);
        senderTextView.setText(sender);
        dateTextView.setText(date);
    }

    private int getItemViewType(Cursor cursor) {
        String username = cursor.getString(cursor.getColumnIndex(MessagesTable.COLUMN_SENDER));
        if (username.equals(mUsername)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = (Cursor) getItem(position);
        return getItemViewType(cursor);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int res = 0;
        if (getItemViewType(cursor) == 0) {
            res = R.layout.message_right;
        } else {
            res = R.layout.message_left;
        }
        return cursorInflater.inflate(res, parent, false);
    }
}
