package com.teamchief.petergok.teamchief.model.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.teamchief.petergok.teamchief.model.tables.MessagesTable;

/**
 * Created by Peter on 2015-01-11.
 */
public class TeamsTable {
    public static final String TABLE_TEAMS = "teams";

    public static final Integer FALSE = 0;
    public static final Integer TRUE = 1;

    // MESSAGES Table - Columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEAM_ID = "teamId";
    public static final String COLUMN_LAST_MESSAGE = "lastMessage";
    public static final String COLUMN_TEAM_NAME = "teamName";
    public static final String COLUMN_LAST_ACTIVE = "lastActive";

    // Database creation sql statement
    private static final String CREATE_MESSAGES_TABLE = "create table "
            + TABLE_TEAMS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TEAM_ID + " text not null,"
            + COLUMN_LAST_ACTIVE + " integer not null, "
            + COLUMN_LAST_MESSAGE + " text, "
            + COLUMN_TEAM_NAME + " text not null"
            + ");";

    private static String[] FULL_PROJECTION = { COLUMN_ID,
            COLUMN_TEAM_ID, COLUMN_LAST_ACTIVE, COLUMN_LAST_MESSAGE,
            COLUMN_TEAM_NAME };

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_MESSAGES_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(MessagesTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
        onCreate(database);
    }

    public static String[] getFullProjection() {
        return FULL_PROJECTION;
    }
}
