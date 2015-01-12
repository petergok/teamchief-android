package com.teamchief.petergok.teamchief.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.teamchief.petergok.teamchief.model.tables.MessagesTable;
import com.teamchief.petergok.teamchief.model.tables.TeamsTable;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "teamchief.db";
    private static final int DATABASE_VERSION = 5;
    public static MySQLiteHelper _instance;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _instance = this;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        MessagesTable.onCreate(database);
        TeamsTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        MessagesTable.onUpgrade(database, oldVersion, newVersion);
        TeamsTable.onUpgrade(database, oldVersion, newVersion);
    }
}
