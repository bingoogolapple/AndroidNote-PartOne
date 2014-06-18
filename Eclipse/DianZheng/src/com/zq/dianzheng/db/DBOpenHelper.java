package com.zq.dianzheng.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zq.dianzheng.util.Constants;
import com.zq.dianzheng.util.Logger;

/**
 * 管理数据库的创建和版本
 * 
 * @author 郑强
 */
public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = "DBOpenHelper";

	/**
	 * 在SQLiteOpenHelper的子类当中，必须有该构造函数
	 * 
	 * @param context 应用程序上下文
	 */
	public DBOpenHelper(Context context) {
		/**
		 * 必须通过调用父类当中的构造函数，null,采用系统默认游标工厂
		 */
		super(context, Constants.db.DB_NAME, null, Constants.db.DB_VERSION);
	}

	/**
	 * 当第一次创建数据库时调用。表格的创建和初始化表格的个数在这里完成
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Logger.i(TAG, "初始化数据库");
		createMemo(db);
	}
	
	/**
	 * 创建Template表
	 * 
	 * @param db
	 *            SQLiteDatabase对象
	 */
	private void createMemo(SQLiteDatabase db) {
		Logger.i(TAG, "创建Memo表");
		StringBuilder sql = new StringBuilder();
		sql.append("create table " + Constants.db.memo.TABLE_NAME + " (");
		sql.append(Constants.db.memo.COLUMN_NAME_ID + " integer primary key autoincrement,");
		sql.append(Constants.db.memo.COLUMN_NAME_CONTENT + " varchar(50) null,");
		sql.append(Constants.db.memo.COLUMN_NAME_TIME + " varchar(20) null");
		sql.append(")");
		db.execSQL(sql.toString());
	}

	/**
	 * 数据库文件的版本号发生改变的时候调用的 如果目前数据库文件并不存在，这个方法不会被调用
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Logger.i(TAG, "升级数据库");
	}

	/**
	 * 关闭数据链接
	 * 
	 * @param db
	 *            SQLiteDatabase对象
	 * @param cursor
	 *            Cursor对象
	 */
	public static void close(SQLiteDatabase db, Cursor cursor) {
		try {
			if (cursor != null) {
				cursor.close();
			}
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
