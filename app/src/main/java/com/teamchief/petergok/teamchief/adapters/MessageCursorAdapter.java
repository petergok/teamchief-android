package com.teamchief.petergok.teamchief.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.teamchief.petergok.teamchief.R;
import com.teamchief.petergok.teamchief.model.MessagesTable;

/**
 * Created by Peter on 2015-01-07.
 */
public class MessageCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    // Default constructor
    public MessageCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewTitle = (TextView) view.findViewById(R.id.message_row_text);
        String title = cursor.getString(cursor.getColumnIndex(MessagesTable.COLUMN_TEXT));
        textViewTitle.setText(title);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.message_row, parent, false);
    }
}
