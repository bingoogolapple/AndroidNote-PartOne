package com.zq.dianzheng.util;


/**
 * 系统常量工具类
 * 
 * @author 郑强
 */
public final class Constants {
	
	
	private Constants() {
	}

	/**
	 * message code常量类
	 * 
	 * @author 郑强
	 */
	public static final class what {
		public static final int SUCCESS = 1;
		public static final int FAILURE = 2;
	}


	/**
	 * 数据库常量类
	 * 
	 * @author 郑强
	 */
	public static final class db {
		public static final String DB_NAME = "DianZheng.db";
		public static final int DB_VERSION = 1;
		
		/**
		 * 备忘录常量类
		 * 
		 * @author 郑强
		 */
		public static final class memo {
			public static final String TABLE_NAME = "Memo";
			public static final String COLUMN_NAME_ID = "_id";
			public static final String COLUMN_NAME_CONTENT = "content";
			public static final String COLUMN_NAME_TIME = "time";
		}
		
	}
	
	public static final class activity {
		public static final int SET_ALERM_REQUEST = 1;
		public static final int EDIT_MEMO_REQUEST = 2;
	}

}
