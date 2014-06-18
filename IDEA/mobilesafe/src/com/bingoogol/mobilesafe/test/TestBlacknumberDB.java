package com.bingoogol.mobilesafe.test;

import android.test.AndroidTestCase;
import com.bingoogol.mobilesafe.db.BlackNumberOpenHelper;
import com.bingoogol.mobilesafe.db.dao.BlackNumberDao;

import java.util.Random;

/**
 * Created by bingoogol@sina.com on 14-3-23.
 */
public class TestBlacknumberDB extends AndroidTestCase {
    public void testCreateDB() throws Exception{
        BlackNumberOpenHelper helper = new BlackNumberOpenHelper(getContext());
        helper.getWritableDatabase();
    }


    public void testAddBlacknumber() throws Exception{
        BlackNumberDao dao = new BlackNumberDao(getContext());
        long basenumber = 13500000000l;
        Random random = new Random();
        for(int i = 0;i<100;i++){
            long number = (long) (basenumber+i);
            dao.add(String.valueOf(number), String.valueOf((random.nextInt(3)+1)));
        }
    }

    public void testFind() throws Exception{
        BlackNumberDao dao = new BlackNumberDao(getContext());
        assertEquals(true, dao.find("13500000008"));
    }

    public void testupdate() throws Exception{
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.update("13500000099", "3");
    }

    public void testDelete() throws Exception{
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.delete("13500000099");
    }
}
