package com.mysterio.bm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysterio.bm.KbcConstants;
import com.mysterio.bm.R;
import com.mysterio.bm.components.KbcMusicAsyncTask;
import com.mysterio.bm.components.KbcXMLContent;
import com.mysterio.bm.sqlite.KbcDBCategory;

public class KbcSplashScreenActivity extends Activity implements Runnable{
	
	private Context mContext;
	public static SharedPreferences mSharedPreferences;
	public static SharedPreferences.Editor mKbcPrefEditor;
	private KbcMusicAsyncTask music = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		mSharedPreferences = mContext.getSharedPreferences(KbcConstants.PREFERENCE_NAME, 0);
		mKbcPrefEditor = mSharedPreferences.edit();
		
		if(KbcDBCategory.mDB == null)
			try {
				new KbcDBCategory(mContext);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
        FrameLayout splashScreen = new FrameLayout(mContext);
        splashScreen.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        splashScreen.setBackgroundResource(R.drawable.splashscreen);
        
        setContentView(splashScreen);
		
		///showCautionDialog();
        if(!KbcXMLContent.isQuestionDB())
        	new KbcXMLContent(mContext, this).execute();
        else
        	new Handler().postDelayed(this, 3000);
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void run() {
		startActivity(new Intent(this, KbcMainMenuActivity.class));
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		finish();
	}
	
	private void showCautionDialog(){
		   AlertDialog.Builder screenDialog = new AlertDialog.Builder(this);
		    screenDialog.setTitle("INFORMATION");
		 
		    TextView TextOut = new TextView(this);
		    TextOut.setText(KbcConstants.INFORMATION);
		    LayoutParams textOutLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    TextOut.setLayoutParams(textOutLayoutParams);
		     
		    ImageView bmImage = new ImageView(this);
		    bmImage.setBackgroundResource(R.drawable.icon);
		    LayoutParams bmImageLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    bmImage.setLayoutParams(bmImageLayoutParams);
		     
		    LinearLayout dialogLayout = new LinearLayout(this);
		    dialogLayout.setOrientation(LinearLayout.VERTICAL);
		    dialogLayout.addView(bmImage);
		    dialogLayout.addView(TextOut);
		    screenDialog.setView(dialogLayout);
		        
		    screenDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        // do something when the button is clicked
		        public void onClick(DialogInterface arg0, int arg1) {
		        	arg0.dismiss();
		            /*music = new KbcMusicAsyncTask(mContext);
		            music.execute(R.raw.thememusic);*/
		    		KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(KbcConstants.SOUND_SETTINGS, true);
		    		KbcSplashScreenActivity.mKbcPrefEditor.commit();		        	
		        	if(!KbcXMLContent.isQuestionDB())
		            	new KbcXMLContent(mContext, KbcSplashScreenActivity.this).execute();
		            else
		            	new Handler().postDelayed(KbcSplashScreenActivity.this, 3000);		        	
		         }
		        });
		    screenDialog.show();
	}
}

