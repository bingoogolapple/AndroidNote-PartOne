package com.bingoogol.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.bingoogol.mobilesafe.db.ApplockOpenHelper;

/**
 * 实现数据库增删改查
 * 
 * @author Administrator
 * 
 */
public class AppLockDao {
	private ApplockOpenHelper helper;
	private Context context;
	
	public static Uri uri = Uri.parse("content://com.itheima.applock");

	/**
	 * 构造方法初始化helper对象
	 * 
	 * @param context
	 */
	public AppLockDao(Context context) {
		helper = new ApplockOpenHelper(context);
		this.context = context;
	}

	/**
	 * 查找
	 * 
	 * @param packname
	 * @return
	 */
	public boolean find(String packname) {
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("applock", null, "packname=?",
				new String[] { packname }, null, null, null);
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}

	
	/**
	 * 查询全部
	 */
	public List<String> findAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("applock", new String[]{"packname"}, null,
				null, null, null, null);
		ArrayList<String> packNames = new ArrayList<String>();
		while(cursor.moveToNext()){
			packNames.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return packNames;
	}

	
	/**
	 * 添加一条
	 * 
	 * @param packname
	 *            包名

	 * @return 新添加的条目在数据库的位置
	 */
	public long add(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		long id = db.insert("applock", null, values);
		db.close();
		//发送一个消息 通知内容观察者 数据变化了..
		context.getContentResolver().notifyChange(uri, null);
		return id;
	}

	

	/**
	 * 删除
	 * 
	 * @param packname
	 * @return
	 */
	public boolean delete(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int affectedrow = db.delete("applock", "packname=?",
				new String[] { packname });
		db.close();
		if (affectedrow == 1) {
			//发送一个消息 通知内容观察者 数据变化了..
			context.getContentResolver().notifyChange(uri, null);
			return true;
		} else {
			return false;
		}
	}

}
