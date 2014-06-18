package com.zq.dianzheng.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zq.dianzheng.db.DBOpenHelper;
import com.zq.dianzheng.model.Memo;
import com.zq.dianzheng.util.Constants;
import com.zq.dianzheng.util.Logger;

/**
 * 备忘录数据访问Dao
 * 
 * @author 郑强
 */
public class MemoDao {
	private static final String tag = "MemoDao";
	private DBOpenHelper dbOpenHelper;

	/**
	 * 创建一个MemoDao,用于对备忘录数据进行增删改查
	 * 
	 * @param context
	 *            应用程序上下文
	 */
	public MemoDao(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
	}

	/**
	 * 添加备忘录
	 * 
	 * @param memo
	 *            备忘录对象
	 * @return 添加成功返回true，否则返回false
	 */
	public boolean addMemo(Memo memo) {
		SQLiteDatabase db = null;
		boolean result = false;
		try {
			db = dbOpenHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Constants.db.memo.COLUMN_NAME_CONTENT, memo.getContent());
			values.put(Constants.db.memo.COLUMN_NAME_TIME, memo.getTime());
			result = db.insert(Constants.db.memo.TABLE_NAME, null, values) == -1 ? false : true;
			Logger.d(tag, "添加备忘录 >> " + memo.toString());
		} finally {
			DBOpenHelper.close(db, null);
		}
		return result;
	}

	/**
	 * 刪除指定备忘录
	 * 
	 * @param id
	 *           备忘录id
	 * @return 刪除成功返回true，否则返回false
	 */
	public boolean deleteMemo(int id) {
		SQLiteDatabase db = null;
		boolean result = false;
		try {
			db = dbOpenHelper.getReadableDatabase();
			int count = db.delete(Constants.db.memo.TABLE_NAME, Constants.db.memo.COLUMN_NAME_ID + "=?", new String[] { id + "" });
			if (count == 1) {
				Logger.d(tag, "删除备忘录 >> " + id);
				result = true;
			}
		} finally {
			DBOpenHelper.close(db, null);
		}
		return result;
	}

	/**
	 * 修改备忘录
	 * 
	 * @param memo
	 *            备忘录
	 * @return 修改成功返回true，否则返回false
	 */
	public boolean updateMemo(Memo memo) {
		SQLiteDatabase db = null;
		boolean result = false;
		try {
			db = dbOpenHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Constants.db.memo.COLUMN_NAME_CONTENT, memo.getContent());
			values.put(Constants.db.memo.COLUMN_NAME_TIME, memo.getTime());
			int count = db.update(Constants.db.memo.TABLE_NAME, values, Constants.db.memo.COLUMN_NAME_ID + "=?", new String[] { memo.getId() + "" });
			if (count == 1) {
				result = true;
				Logger.d(tag, "修改备忘录 >> " + memo.toString());
			}
		} finally {
			DBOpenHelper.close(db, null);
		}
		return result;
	}

	/**
	 * 获取指定备忘录
	 * 
	 * @param id
	 *            备忘录id
	 * @return 备忘录
	 */
	public Memo getMemo(int id) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		Memo memo = null;
		try {
			db = dbOpenHelper.getWritableDatabase();
			cursor = db.query(Constants.db.memo.TABLE_NAME, null, Constants.db.memo.COLUMN_NAME_ID + "=?", new String[] { id + "" }, null, null, null);
			if (cursor.moveToFirst()) {
				String content = cursor.getString(cursor.getColumnIndex(Constants.db.memo.COLUMN_NAME_CONTENT));
				long time = cursor.getLong(cursor.getColumnIndex(Constants.db.memo.COLUMN_NAME_TIME));
				memo = new Memo(id, content, time);
				Logger.d(tag, "查询单个备忘录 >> " + memo.toString());
			}
		} finally {
			DBOpenHelper.close(db, cursor);
		}
		return memo;
	}

	/**
	 * 分页获取备忘录数据
	 * 
	 * @param offset
	 *            偏移量
	 * @param maxResult
	 *            每页显示的数量
	 * @return 备忘录分页数据
	 */
	public List<Memo> getScrollData(int offset, int maxResult) {
		List<Memo> datas = new ArrayList<Memo>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = dbOpenHelper.getReadableDatabase();
			cursor = db.query(Constants.db.memo.TABLE_NAME, null, null, null, null, null, null, offset + "," + maxResult);
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex(Constants.db.memo.COLUMN_NAME_ID));
				String content = cursor.getString(cursor.getColumnIndex(Constants.db.memo.COLUMN_NAME_CONTENT));
				long time = cursor.getLong(cursor.getColumnIndex(Constants.db.memo.COLUMN_NAME_TIME));
				datas.add(new Memo(id, content, time));
			}
			Logger.d(tag, "分页获取备忘录 >> datas.size():" + datas.size());
		} finally {
			DBOpenHelper.close(db, cursor);
		}
		return datas;
	}

	/**
	 * 备忘录数量
	 * 
	 * @return 备忘录总数
	 */
	public long getCount() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		long result = 0L;
		try {
			db = dbOpenHelper.getReadableDatabase();
			cursor = db.rawQuery("select count(*) from " + Constants.db.memo.TABLE_NAME, null);
			cursor.moveToFirst();
			result = cursor.getLong(0);
			Logger.d(tag, "备忘录总数 >> " + result);
		} finally {
			DBOpenHelper.close(db, cursor);
		}
		return result;
	}
}
