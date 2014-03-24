package com.bingoogol.mobilesafe.test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * @author bingoogol@sina.com 14-1-23.
 */
public class TestGetUpdateInfo extends AndroidTestCase {

    public void testGetInfo() throws Exception {
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.itheima.mobilesafe/files/address.db", null, SQLiteDatabase.OPEN_READONLY);


        Cursor cursor = db.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)", new String[]{"1361234"});

        if(cursor.moveToNext()){
            String address = cursor.getString(0);
            System.out.println(address);
        }
        cursor.close();
        db.close();
    }
}
