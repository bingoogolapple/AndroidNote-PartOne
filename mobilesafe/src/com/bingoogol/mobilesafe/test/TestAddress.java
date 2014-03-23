package com.bingoogol.mobilesafe.test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.test.AndroidTestCase;
import com.bingoogol.mobilesafe.db.dao.AddressDao;
import com.bingoogol.mobilesafe.util.Logger;

/**
 * @author bingoogol@sina.com 14-1-23.
 */
public class TestAddress extends AndroidTestCase {
    private static final String TAG = "TestAddress";

    public void testGetAddress() throws Exception {
        Logger.i(TAG, AddressDao.getAddress("15216661163"));
    }
}
