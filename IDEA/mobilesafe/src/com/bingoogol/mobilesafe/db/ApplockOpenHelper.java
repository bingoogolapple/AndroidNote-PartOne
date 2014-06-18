package com.bingoogol.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ApplockOpenHelper extends SQLiteOpenHelper {

	public ApplockOpenHelper(Context context) {
		super(context, "applock.db", null, 1);
	}

	/**
	 * 数据库第一次创建的时候 调用的方法.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table applock (_id integer primary key autoincrement , packname varchar(20)) ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
