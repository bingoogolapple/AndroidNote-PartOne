package com.bingoogol.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.bingoogol.mobilesafe.db.BlackNumberOpenHelper;
import com.bingoogol.mobilesafe.domain.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bingoogol@sina.com on 14-3-23.
 */
public class BlackNumberDao {
    private BlackNumberOpenHelper helper;

    /**
     * 构造方法初始化helper对象
     *
     * @param context
     */
    public BlackNumberDao(Context context) {
        helper = new BlackNumberOpenHelper(context);
    }

    /**
     * 查找黑名单号码
     *
     * @param number
     * @return
     */
    public boolean find(String number) {
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", null, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }


    /**
     * 查找全部黑名单号码
     *
     * @return
     */
    public List<BlackNumberInfo> findAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null, null);
        List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            String number = cursor.getString(0);
            String mode = cursor.getString(1);
            BlackNumberInfo info = new BlackNumberInfo();
            info.setNumber(number);
            info.setMode(mode);
            infos.add(info);
            info = null;
        }
        cursor.close();
        db.close();
        return infos;
    }

    /**
     * 分页的查找黑名单号码
     *
     * @param pagenumber
     *            页码 从第0页开始
     * @param maxSize
     *            一页最多少条数据
     * @return
     */
    public List<BlackNumberInfo> findBlackNumberByPage(int pagenumber,
                                                       int maxSize) {
        SQLiteDatabase db = helper.getReadableDatabase();
        // Cursor cursor = db.query("blacknumber", new
        // String[]{"number","mode"},null,null, null, null, null);
        Cursor cursor = db.rawQuery(
                "select number,mode from blacknumber order by _id desc limit ? offset ? ",
                new String[] { String.valueOf(maxSize),
                        String.valueOf(pagenumber * maxSize) });
        List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            String number = cursor.getString(0);
            String mode = cursor.getString(1);
            BlackNumberInfo info = new BlackNumberInfo();
            info.setNumber(number);
            info.setMode(mode);
            infos.add(info);
            info = null;
        }
        cursor.close();
        db.close();
        return infos;
    }

    /**
     * 获取一共有多少页的内容
     *
     * @param maxSize
     * @return
     */
    public int getTotalPageNumber(int maxSize) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from blacknumber ",null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if(count%maxSize==0){
            return count/maxSize;
        }else{
            return count/maxSize+1;
        }
    }

    /**
     * 添加一条黑名单号码
     *
     * @param number 号码
     * @param mode   模式 1.全部 2 电话  3短信
     * @return 新添加的条目在数据库的位置
     */
    public long add(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        long id = db.insert("blacknumber", null, values);
        db.close();
        return id;
    }

    /**
     * 修改黑名单号码的拦截模式
     *
     * @param number  黑名单号码
     * @param newmode 新的拦截模式
     * @return 是否更新成功
     */
    public boolean update(String number, String newmode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", newmode);
        int affectraw = db.update("blacknumber", values, "number=?", new String[]{number});
        db.close();
        if (affectraw == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除一条黑名单号码
     *
     * @param number
     * @return
     */
    public boolean delete(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int affectedrow = db.delete("blacknumber", "number=?", new String[]{number});
        db.close();
        if (affectedrow == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查找黑名单号码的拦截模式
     *
     * @param number
     * @return null 代表不是黑名单号码  1 全部拦截 2电话拦截 3短信拦截
     */
    public String findMode(String number) {
        String result = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?",
                new String[] { number }, null, null, null);
        if (cursor.moveToNext()) {
            result = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return result;
    }
}
