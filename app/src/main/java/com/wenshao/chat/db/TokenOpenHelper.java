package com.wenshao.chat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wenshao on 2017/3/15.
 */

public class TokenOpenHelper extends SQLiteOpenHelper {
    public TokenOpenHelper(Context context) {
        super(context, "token", null, 0);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
