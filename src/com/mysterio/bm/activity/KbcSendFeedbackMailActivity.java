package com.mysterio.bm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mysterio.bm.KbcConstants;

public class KbcSendFeedbackMailActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		sendMail();
	}
	
	public void sendMail(){
		//setBackground();
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{KbcConstants.MAIL_ID});
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, KbcConstants.SUBJECT);
		emailIntent.setType("text/html");
		Intent intent = Intent.createChooser(emailIntent, "Send email ...");
		//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}
}
