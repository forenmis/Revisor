package com.example.revizor.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database";
    private static final int DATABASE_VERSION = 1;

    public OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Reviewes(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " caption TEXT NOT NULL," +
                " title TEXT NOT NULL," +
                " photoUrl TEXT," +
                " date TEXT," +
                " sendStatus TEXT," +
                " location TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
