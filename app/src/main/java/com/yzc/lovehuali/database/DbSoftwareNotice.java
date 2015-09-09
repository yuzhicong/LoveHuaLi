package com.yzc.lovehuali.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015-09-03.
 */
public class DbSoftwareNotice extends SQLiteOpenHelper{
    public DbSoftwareNotice(Context context) {
        super(context, "dbSoftwareNotice", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE software_notice("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT DEFALUT \"\"," +
                "publishDate DATE DEFALUT \"\"," +
                "content TEXT DEFALUT \"\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
