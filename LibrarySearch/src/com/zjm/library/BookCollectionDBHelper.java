package com.zjm.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zero on 2015/7/23.
 */
public class BookCollectionDBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "BookCollectionData";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "BookCollection";

    public BookCollectionDBHelper(Context context) {
        // TODO Auto-generated constructor stub
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(_id INTEGER PRIMARY KEY,"
                + " BookName VARCHAR(50)  NOT NULL,"
                + " BookMsg VARCHAR(100) NOT NULL,"
                + " href VARCHAR(450) NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert(String bookName,String bookMsg,String href){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("BookName", bookName);
        cv.put("BookMsg", bookMsg);
        cv.put("href", href);
        db.insert(TABLE_NAME, null, cv);
        //System.out.println(">>>>>>>>>>>>>>>>>插入数据成功!!<<<<<<<<<<<");
    }

    public Cursor RetAllCollectBook(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM BookCollection",null);
        return cursor;
    }

    public void Delete(String bookName){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {String.valueOf(bookName)};
        db.delete(TABLE_NAME, "BookName=?",args);
    }

    public boolean Check(String bookName){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {String.valueOf(bookName)};
        Cursor cursor = db.query(TABLE_NAME, new String[]{"BookName"}, "BookName=?", args, null, null, null,null);
        if(cursor.moveToFirst()){
            String bookname = cursor.getString(cursor.getColumnIndex("BookName"));
            if(!bookname.equals("")){
                //System.out.println(">>>>>>>>>>>>>>存在!<<<<<<<<<<<<<<<<<");
                return true;
            }
        }
        return false;

    }
}
