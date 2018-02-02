package com.mysterio.bm.sqlite;

import com.mysterio.bm.KbcConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class KbcDBHelper extends SQLiteOpenHelper{

	private SQLiteDatabase db = null;
	private static final String SCORE_TABLE = " create table "+ KbcConstants.DB_TABLE + " ( " + KbcConstants.KEY_ROWID + " integer primary key autoincrement, "
								+ KbcConstants.PLAYER_NAME + " text not null, " + KbcConstants.SCORE + " text not null " + " ); ";
	
	private static final String RES_TABLE = " create table "+ KbcConstants.DB_RES_TABLE + " ( " + KbcConstants.KEY_ROWID + " integer primary key autoincrement, "
			+ KbcConstants.DB_QUESTIONS + " text not null, " + KbcConstants.DB_OPTIONS + " text not null, " +KbcConstants.DB_ANSWERS + " text not null, " + KbcConstants.OCCURANCE + " text not null" + " ); ";
	
	public KbcDBHelper(Context context) {
		super(context, KbcConstants.DB_NAME, null, KbcConstants.DB_VERSION);
	}
	
	@Override
	public synchronized void close() {
		db.close();
		super.close();
	}	

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.execSQL(RES_TABLE);
		db.execSQL(SCORE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("DB", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS "+KbcConstants.DB_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+KbcConstants.DB_RES_TABLE);
		onCreate(db);
	}
}
