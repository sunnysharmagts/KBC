package com.mysterio.bm.components;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.mysterio.bm.KbcConstants;
import com.mysterio.bm.activity.KbcMainMenuActivity;
import com.mysterio.bm.activity.KbcSplashScreenActivity;
import com.mysterio.bm.sqlite.KbcDBCategory;
import com.mysterio.bm.R;

public class KbcXMLContent extends AsyncTask<Void, Void, Boolean>{
	
	private static ArrayList<String> questionList;
	private static ArrayList<String> optionList;
	private static ArrayList<Integer> answerList;
	private String tag;
	private Context mContext;
	private KbcDBCategory db = null;
	private KbcSplashScreenActivity splash = null;
	private int mXMLTagType;
	
	public KbcXMLContent(Context mContext, KbcSplashScreenActivity splash){
		this.mContext = mContext;
		this.splash = splash;
	}

	private void getXMLContent(Context mContext) throws XmlPullParserException,IOException {
		Resources mResources = mContext.getResources();
		XmlResourceParser mParser = mResources.getXml(R.xml.new_question);
		mParser.next();
		mXMLTagType = mParser.getEventType();
		while (mXMLTagType != XmlPullParser.END_DOCUMENT) {
			if (mXMLTagType == XmlPullParser.START_TAG && mParser.getName().equalsIgnoreCase("question")) {
				String question = mParser.getAttributeValue(null, "question");
				String option = mParser.getAttributeValue(null, "option");
				String answer = mParser.getAttributeValue(null, "ans");
				if (!question.equalsIgnoreCase("") && !option.equalsIgnoreCase("") && !answer.equalsIgnoreCase("")) {
					ContentValues contentValues = new ContentValues();
					contentValues.put(KbcConstants.DB_QUESTIONS, question);
					contentValues.put(KbcConstants.DB_OPTIONS, option);
					contentValues.put(KbcConstants.DB_ANSWERS, answer);
					contentValues.put(KbcConstants.OCCURANCE, "false");
					KbcDBCategory.mDB.insert(KbcConstants.DB_RES_TABLE, null, contentValues);					
				}
			}
			mXMLTagType = mParser.next();
		}	
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		
		try{
			if(KbcDBCategory.mDB == null)
				db = new KbcDBCategory(mContext);
			
			if(!isQuestionDB()){
				getXMLContent(mContext);
				//setDBQuestions();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
/*		if(!isQuestionDB()){	
			new KbcLoadDB().execute();
		}	*/
		Intent intent = new Intent();
		intent.setClass(mContext, KbcMainMenuActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		splash.finish();
	}
	
	public void setDBQuestions(){
		
		if(!isQuestionDB()){
			//System.out.println("CURSOR COUNT IS :-"+cursorCount);
			for(int i = 0; i < answerList.size(); i++){
				//System.out.println("CURSOR COUNT IS :-");
				ContentValues contentValues = new ContentValues();
				contentValues.put(KbcConstants.DB_QUESTIONS, questionList.get(i));
				contentValues.put(KbcConstants.DB_OPTIONS, optionList.get(i));
				contentValues.put(KbcConstants.DB_ANSWERS, answerList.get(i));
				contentValues.put(KbcConstants.OCCURANCE, "false");
				KbcDBCategory.mDB.insert(KbcConstants.DB_RES_TABLE, null, contentValues);
			}	
		}
		else{
			Log.e("TAG","QUESTION ALREADY LOADED");
		}		
	}
	
	public static boolean isQuestionDB(){
		try {
			Cursor cursor = KbcDBCategory.mDB.query(KbcConstants.DB_RES_TABLE, new String[]{KbcConstants.KEY_ROWID}, null, null, null, null, null);
			int cursorCount  = cursor.getCount();
			cursor.close();
			if(cursorCount == 0)
				return false;
			else
				return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}
}