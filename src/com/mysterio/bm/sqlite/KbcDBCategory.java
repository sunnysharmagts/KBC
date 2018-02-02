package com.mysterio.bm.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mysterio.bm.sqlite.KbcDBHelper;

public class KbcDBCategory {

	public static SQLiteDatabase mDB;
	private KbcDBHelper dbHelper;
	
	public KbcDBCategory(Context context) throws Exception{
		dbHelper = new KbcDBHelper(context);
		mDB = dbHelper.getWritableDatabase();
	}
}
