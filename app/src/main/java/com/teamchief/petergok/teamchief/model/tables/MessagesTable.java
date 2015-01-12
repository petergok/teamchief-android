package com.teamchief.petergok.teamchief.model.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Peter on 2015-01-07.
 */
public class MessagesTable {
    public static final String TABLE_MESSAGES = "messages";

    public static final Integer FALSE = 0;
    public static final Integer TRUE = 1;

    // MESSAGES Table - Columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEAM_ID = "teamId";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_MESSAGE_ID = "messageId";
    public static final String COLUMN_LOCAL = "local";
    public static final String COLUMN_SEND_TIME = "sendTime";
    public static final String COLUMN_SENDER = "sender";

    // Database creation sql statement
    private static final String CREATE_MESSAGES_TABLE = "create table "
            + TABLE_MESSAGES
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MESSAGE_ID + " text not null,"
            + COLUMN_LOCAL + " integer not null, "
            + COLUMN_TEXT + " text not null, "
            + COLUMN_TEAM_ID + " text not null, "
            + COLUMN_SEND_TIME + " integer not null, "
            + COLUMN_SENDER + " text not null"
            + ");";

    private static String[] FULL_PROJECTION = { COLUMN_SENDER,
            COLUMN_SEND_TIME, COLUMN_TEAM_ID, COLUMN_MESSAGE_ID,
            COLUMN_TEXT, COLUMN_ID, COLUMN_LOCAL };

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_MESSAGES_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(MessagesTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(database);
    }

    public static String[] getFullProjection() {
        return FULL_PROJECTION;
    }
}
