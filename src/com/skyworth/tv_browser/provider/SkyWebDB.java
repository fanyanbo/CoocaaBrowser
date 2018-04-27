///**
// * Copyright (C) 2012 The SkyTvOS Project
// *
// * Version     Date           Author
// * 鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹�攢鈹� *           2013-4-13         Root.Lu
// *
// */
//
//package com.skyworth.tv_browser.provider;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Formatter;
//import java.util.List;
//import java.util.Locale;
//
//import com.skyworth.framework.skysdk.android.SkyDBUtil;
//import com.skyworth.framework.skysdk.android.SkyDBUtil.QueryHandler;
//import com.skyworth.framework.skysdk.android.SkySystemUtil;
//import com.skyworth.framework.skysdk.android.SkySystemUtil.LINUX_CMD;
//import com.skyworth.framework.skysdk.util.MD5;
//import com.skyworth.framework.skysdk.util.SkyDB;
//import com.skyworth.tv_browser.data.BookmarkData;
//
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Bitmap;
//import android.graphics.Matrix;
//import android.util.Log;
//
//public class SkyWebDB1 {
//	private static SkyWebDB instance = null;
//	private Context mContext=null;
//
//	public static SkyWebDB getInstance(Context mContext) {
//		if (instance == null)
//			instance = new SkyWebDB(mContext);
//		return instance;
//	}
//
//	private   String SKYWEB_PATH ;
//	private   String DB_FILE ;
//	private   String BOOKMARK_THUMB_PATH ;
//	private   String BOOKMARK_FAVCION ;
////	private   String WEBSITETHUMB ;
////	private   String HOTVIDEOTHUMB;
//	public static final String BOOKMARK_TABLE = "tv_bookmark";
////	public static final String VIDEOHISTORY_TABLE = "video_history";
////	public static final String HOTSEARCH_TABLE = "hot_search";
////	public static final String HOTSITES_TABLE = "hot_sites";
////	public static final String HOTVIDEO_TABLE = "hot_video";
////	public static final String ALLSITES_TABLE = "all_sites";
//	public static final String PAGEHISTORY_TABLE = "page_history";
//
//	private static final String[][] BOOKMARK_TABLE_COLUMNSANDTYPES = {
//			{ "id", "INTEGER PRIMARY KEY" }, { "title", "TEXT unique" },
//			{ "url", SkyDB.FILED_TYPE_STRING },
//			{ "date", SkyDB.FILED_TYPE_INT },
//			{ "isBookmark", SkyDB.FILED_TYPE_INT },
//			{ "isHistory", SkyDB.FILED_TYPE_INT },
//			{ "thumb", SkyDB.FILED_TYPE_STRING },
//			{ "favcion", SkyDB.FILED_TYPE_STRING } };
//
////	private static final String[][] VIDEOHISTORY_TABLE_COLUMNSANDTYPES = {
////			{ "id", "INTEGER PRIMARY KEY" }, { "title", "TEXT unique" },
////			{ "url", SkyDB.FILED_TYPE_STRING },
////			{ "date", SkyDB.FILED_TYPE_INT } };
////
////	private static final String[][] HOTSEARCH_TABLE_COLUMNSANDTYPES = {
////			{ "id", "INTEGER PRIMARY KEY" }, { "text", "TEXT unique" },
////			{ "url", SkyDB.FILED_TYPE_STRING } };
////
////	private static final String[][] HOTSITES_TABLE_COLUMNSANDTYPES = {
////			{ "id", "INTEGER PRIMARY KEY" }, { "title", "TEXT " },
////			{ "url", SkyDB.FILED_TYPE_STRING + " unique " },
////			{ "thumb", SkyDB.FILED_TYPE_STRING }, { "thumbpath", "STRING " } };
////
////	private static final String[][] HOTVIDEO_TABLE_COLUMNSANDTYPES = {
////			{ "id", "INTEGER PRIMARY KEY" }, { "title", "TEXT unique" },
////			{ "detail", "TEXT" }, { "url", SkyDB.FILED_TYPE_STRING },
////			{ "thumb", SkyDB.FILED_TYPE_STRING }, { "thumbpath", "STRING " } };
////
////	private static final String[][] ALLSITES_TABLE_COLUMNSANDTYPES = {
////			{ "id", "INTEGER PRIMARY KEY" }, { "category", "TEXT " },
////			{ "title", "TEXT unique" }, { "url", SkyDB.FILED_TYPE_STRING } };
//	
//	private static final String[][] PAGEHISTORY_TABLE_COLUMNSANDTYPES = {
//		{ "id", "INTEGER PRIMARY KEY" }, { "title", "TEXT unique" },
//		{ "url", SkyDB.FILED_TYPE_STRING },
//		{ "date", SkyDB.FILED_TYPE_INT },
//		{ "favcion", SkyDB.FILED_TYPE_STRING } };
//	
//	private static final int BOOKMARK_THUMB_WIDTH = 1920;
//	private static final int BOOKMARK_THUMB_HEIGHT = 1080;
//
//	private SkyDBUtil dbUtil = null;
//
//	private String tag = "BrowserActivity";
//	
//	public void initString(Context c){
//		 mContext=c;
//		 SKYWEB_PATH=mContext.getFilesDir()+"/browser_data"+getVersionCode(mContext)+"/";
//		 Log.d(tag, "initString----"+SKYWEB_PATH);
//		 DB_FILE = SKYWEB_PATH + "skyweb.db";
//		 BOOKMARK_THUMB_PATH = SKYWEB_PATH + "BookmarkData/";
//		 BOOKMARK_FAVCION = SKYWEB_PATH + "favcion/";
////		 WEBSITETHUMB = SKYWEB_PATH + "hotsites/";
////		 HOTVIDEOTHUMB = SKYWEB_PATH + "hotvideos/";
//	}
//	
//	private static int getVersionCode(Context context) {
//        PackageManager mPackageManager = context.getPackageManager();
//        try {
//            return mPackageManager.getPackageInfo(context.getPackageName(), 0).versionCode;
//        } catch (Exception e) {
//            return -1;
//        }
//    }
//
//	private SkyWebDB(Context c) {
//		initString(c);
//		if (!(new File(SKYWEB_PATH).exists()))
//			new File(SKYWEB_PATH).mkdir();
//		dbUtil = new SkyDBUtil(DB_FILE);
//		if (!(new File(DB_FILE).exists())) {
//			dbUtil.createTable(BOOKMARK_TABLE, BOOKMARK_TABLE_COLUMNSANDTYPES);
////			dbUtil.createTable(VIDEOHISTORY_TABLE,
////					VIDEOHISTORY_TABLE_COLUMNSANDTYPES);
////			dbUtil.createTable(HOTSEARCH_TABLE, HOTSEARCH_TABLE_COLUMNSANDTYPES);
////			dbUtil.createTable(HOTSITES_TABLE, HOTSITES_TABLE_COLUMNSANDTYPES);
////			dbUtil.createTable(HOTVIDEO_TABLE, HOTVIDEO_TABLE_COLUMNSANDTYPES);
////			dbUtil.createTable(ALLSITES_TABLE, ALLSITES_TABLE_COLUMNSANDTYPES);
//			dbUtil.createTable(PAGEHISTORY_TABLE, PAGEHISTORY_TABLE_COLUMNSANDTYPES);
//		}
//
//		File dirFile = new File(BOOKMARK_THUMB_PATH);
//		if (!dirFile.exists())
//			dirFile.mkdir();
//		File dirFile2 = new File(BOOKMARK_FAVCION);
//		if (!dirFile2.exists())
//			dirFile2.mkdir();
////		File dirFile3 = new File(WEBSITETHUMB);
////		if (!dirFile3.exists())
////			dirFile3.mkdir();
////		File dirFile4 = new File(HOTVIDEOTHUMB);
////		if (!dirFile4.exists())
////			dirFile4.mkdir();
//		Log.d(tag, "LINUX_CMD_CHMOD---------");
//		SkySystemUtil.execCmd(LINUX_CMD.LINUX_CMD_CHMOD, " -R 777 "
//				+ SKYWEB_PATH);
//
//	}
//
//	public boolean addBookmark(String title, String url, long time,
//			int isBookmark, int isHistory, Bitmap thumb, Bitmap fav) {
//		String thumb_path = "", fav_path = "";
//		boolean flag_a = true, flag_b = true;
//		if (thumb != null && !thumb.isRecycled()) {
//			thumb_path = BOOKMARK_THUMB_PATH + MD5.md5s(title + url);
//			File myCaptureFile = new File(thumb_path);
//			flag_a = resizedBitmap(thumb, myCaptureFile);
//			Log.d(tag, "thumb_path----" + thumb_path);
//		}
//		if (fav != null && !fav.isRecycled()) {
//			fav_path = BOOKMARK_FAVCION + MD5.md5s(title + url);
//			File myCaptureFile2 = new File(fav_path);
//			flag_b = resizedBitmap(fav, myCaptureFile2);
//			Log.d(tag, "fav_path----" + fav_path);
//		}
//		if (!flag_a || !flag_b) {
//			Log.d(tag, "addBookmark failed---");
//			return false;
//		}
//
//		Object[] values = { null, title, url, time, isBookmark, isHistory,
//				thumb_path, fav_path };
//		dbUtil.insert_replace(BOOKMARK_TABLE, values);
//		Log.d(tag, "addBookmark success---");
//
//		if (thumb != null && !thumb.isRecycled()) {
//			thumb.recycle();
//			thumb = null;
//		}
//		if (fav != null && !fav.isRecycled()) {
//			fav.recycle();
//			fav = null;
//		}
//		System.gc();
//		return true;
//
//	}
//	
//	public boolean addPageHistory(String title, String url, long time,
//			 Bitmap fav) {
//		String  fav_path = "";
//		boolean  flag_b = true;
//		if (fav != null && !fav.isRecycled()) {
//			fav_path = BOOKMARK_FAVCION + MD5.md5s(title + url);
//			File myCaptureFile2 = new File(fav_path);
//			flag_b = resizedBitmap(fav, myCaptureFile2);
//			Log.d(tag, "fav_path----" + fav_path);
//		}
//		if ( !flag_b) {
//			Log.d(tag, "addPageHistory failed---");
//			return false;
//		}
//
//		Object[] values = { null, title, url, time,  fav_path };
//		dbUtil.insert_replace(PAGEHISTORY_TABLE, values);
//		Log.d(tag, "addPageHistory success---");
//
//		if (fav != null && !fav.isRecycled()) {
//			fav.recycle();
//			fav = null;
//		}
//		System.gc();
//		return true;
//	}
//
//	public synchronized boolean deleteBookmark(BookmarkData BookmarkData) {
//		Log.d(tag, "deleteBookmark--BookmarkData.getId()--" + BookmarkData.getId());
//		String sql = " DELETE FROM " + BOOKMARK_TABLE + " WHERE id = "
//				+ BookmarkData.getId();
//		dbUtil.exec(sql);
//		String thumb_path = BookmarkData.getThumb();
//		String fav_path = BookmarkData.getFavcion();
//		if (!thumb_path.equals("")) {
//			File f = new File(thumb_path);
//			if (f.exists()) {
//				f.delete();
//			}
//		}
//		if (!fav_path.equals("")) {
//			File f2 = new File(fav_path);
//			if (f2.exists()) {
//				f2.delete();
//			}
//		}
//		return true;
//	}
//	
//	public synchronized boolean deletePageHistory() {
//		Log.d(tag, "deletePageHistory--------");
//		String sql = " DELETE FROM " + PAGEHISTORY_TABLE + " WHERE id > 0 ";
//		dbUtil.exec(sql);
//		File file=new File(BOOKMARK_FAVCION);
//		if(file.exists()){
//			if(file.isDirectory()){
//			    String[] fileList=file.list();
//			    for(int i=0;i<fileList.length;i++){
//			    	new File(BOOKMARK_FAVCION+fileList[i]).delete();
//			    }
//			}
//		}
//		return true;
//	}
//
//	public synchronized List<BookmarkData> getBookmark() {
//		String sql = "SELECT * FROM " + BOOKMARK_TABLE+ " ORDER BY date DESC";
//		final List<BookmarkData> list = new ArrayList<BookmarkData>();
//		dbUtil.query(sql, null, new QueryHandler() {
//			@Override
//			public Object onResult(Cursor cursor, Object obj) {
//				// TODO Auto-generated method stub
//				boolean hasData = cursor.moveToFirst();
//				while (hasData) {
//					String id = cursor.getString(cursor.getColumnIndex("id"));
//					String title = cursor.getString(cursor
//							.getColumnIndex("title"));
//					String url = cursor.getString(cursor.getColumnIndex("url"));
//					String date = getDateStr(cursor.getLong(cursor
//							.getColumnIndex("date")));
//					int isBookmark = cursor.getInt(cursor
//							.getColumnIndex("isBookmark"));
//					int isHistory = cursor.getInt(cursor
//							.getColumnIndex("isHistory"));
//					String thumb = cursor.getString(cursor
//							.getColumnIndex("thumb"));
//					String favcion = cursor.getString(cursor
//							.getColumnIndex("favcion"));
//					Log.d(tag, "getBookmark---title--url--thumb---"+title+"  "+url+"  " + thumb);
//					list.add(new BookmarkData(id, title, url, date, isBookmark,
//							isHistory, thumb, favcion));
//					hasData = cursor.moveToNext();
//				}
//				return list;
//			}
//		});
//		return list;
//	}
//	
//	public synchronized List<BookmarkData> getPageHistory() {
//		String sql = "SELECT * FROM " + PAGEHISTORY_TABLE+ " ORDER BY date DESC";
//		final List<BookmarkData> list = new ArrayList<BookmarkData>();
//		dbUtil.query(sql, null, new QueryHandler() {
//			@Override
//			public Object onResult(Cursor cursor, Object obj) {
//				// TODO Auto-generated method stub
//				boolean hasData = cursor.moveToFirst();
//				while (hasData) {
//					String id = cursor.getString(cursor.getColumnIndex("id"));
//					String title = cursor.getString(cursor
//							.getColumnIndex("title"));
//					String url = cursor.getString(cursor.getColumnIndex("url"));
//					String date = getDateStr(cursor.getLong(cursor
//							.getColumnIndex("date")));
////					int isBookmark = cursor.getInt(cursor
////							.getColumnIndex("isBookmark"));
////					int isHistory = cursor.getInt(cursor
////							.getColumnIndex("isHistory"));
////					String thumb = cursor.getString(cursor
////							.getColumnIndex("thumb"));
//					String favcion = cursor.getString(cursor
//							.getColumnIndex("favcion"));
//					Log.d(tag, "getBookmark---title--url--favcion---"+title+"  "+url+"  " + favcion);
//					list.add(new BookmarkData(id, title, url, date, 0,
//							1, "", favcion));
//					hasData = cursor.moveToNext();
//				}
//				return list;
//			}
//		});
//		return list;
//	}
//	
//	public synchronized boolean deleteLastPageHistory() {
//		Log.d(tag, "deleteLastPageHistory----");
//		String sql = " DELETE FROM " + PAGEHISTORY_TABLE
//				+ " WHERE date=(SELECT MIN(date) FROM " + PAGEHISTORY_TABLE
//				+ ") ";
//		dbUtil.exec(sql);
//		return true;
//	}
//	
//	public int getHistoryCount(String tableName) {
//        Log.d(tag, "getVideoHistoryCount---------------");
//        SQLiteDatabase db = getDatabaseHandler(SQLiteDatabase.OPEN_READWRITE);
//        if (db == null) {
//            return -1;
//        }
//        Cursor cursor = null;
//        try {
//            cursor = db.query(tableName, new String[] { "title" }, null,
//                    null, null, null, null);
//            Log.d(tag, "getVideoHistoryCount----" + cursor.getCount());
//            int count = cursor.getCount();
//            return count;
//        } finally {
//            if(cursor != null)
//                cursor.close();
//            db.close();
//        }
//        
//    }
//
//	public String getDateStr(long millis) {
//		Calendar cal = Calendar.getInstance();
//		cal.setTimeInMillis(millis);
//		Formatter ft = new Formatter(Locale.CHINA);
//		return ft.format("%1$tY.%1$tm.%1$td", cal).toString();
//	}
//
//	public boolean resizedBitmap(Bitmap bm, File f) {
//
//		try {
//			int width = bm.getWidth();
//			int height = bm.getHeight();
//			int newWidth = BOOKMARK_THUMB_WIDTH;
//			int newHeight = BOOKMARK_THUMB_HEIGHT;
//			float scaleWidth = ((float) newWidth) / width;
//			float scaleHeight = ((float) newHeight) / height;
//			Matrix matrix = new Matrix();
//			matrix.postScale(scaleWidth, scaleHeight);
//			Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
//					matrix, true);
//			BufferedOutputStream bos = new BufferedOutputStream(
//					new FileOutputStream(f));
//			resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
//			bos.flush();
//			bos.close();
//			resizedBitmap.recycle();
//			return true;
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//
//	private SQLiteDatabase getDatabaseHandler(int flags) {
//		SQLiteDatabase db = null;
//		try {
//			db = SQLiteDatabase.openDatabase(DB_FILE, null, flags);
//		} catch (SQLException e) {
//
//		}
//		return db;
//	}
//
//	private String getSQLString(String tableName, Object[] values) {
//
//		String insertValuesString = "";
//		for (Object obj : values) {
//			if (obj == null) {
//				insertValuesString += "null";
//			} else if (obj.getClass().getName().equals("java.lang.String")) {
//				String value = obj.toString();
//				value = value.replace("'", "''");
//				insertValuesString += "'" + value + "'";
//			} else {
//				insertValuesString += obj.toString();
//			}
//			insertValuesString += ",";
//		}
//		insertValuesString = insertValuesString.substring(0,
//				insertValuesString.length() - 1);
//		String sql = "INSERT OR REPLACE INTO " + tableName + " VALUES("
//				+ insertValuesString + ");";
//		return sql;
//	}
//
//}
