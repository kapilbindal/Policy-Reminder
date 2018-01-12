package com.example.kapil.policyreminder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KAPIL on 09-01-2018.
 */

public class RecordDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Record.db";
    public static final int DB_VER = 1;

    public RecordDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(RecordTable.CMD_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
