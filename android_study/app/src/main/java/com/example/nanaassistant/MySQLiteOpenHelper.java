package com.example.nanaassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "incident.db";
    private static final int VERSION = 1;

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //数据库创建
        db.execSQL("create table incident (_id integer primary key autoincrement, " +
                "title char(20), time char(20), detail char(300))");
        db.execSQL("create table bill (_id integer primary key autoincrement, " +
                "title char(20), time char(20), detail char(300),money char(20),month char(20),io char(20),ant char(20))");
        db.execSQL("create table monthcheck (_id integer primary key autoincrement, " +
                "normal char(20),month char(20),ant char(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库升级
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
