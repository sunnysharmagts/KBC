package com.mysterio.bm.activity;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.mysterio.bm.KbcAnimation;
import com.mysterio.bm.KbcConstants;
import com.mysterio.bm.R;
import com.mysterio.bm.ScreenDimension;
import com.mysterio.bm.components.KbcFixBarView;
import com.mysterio.bm.components.KbcMusicAsyncTask;

public class KbcMainMenuActivity extends Activity{

	Context mContext;
	private static LinearLayout background;
	private TextView playButton, instructionButton, highScoreButton, aboutUsButton, rateButton, exitButton;
	private DisplayMetrics mDisplayMetrics;
	private KbcMusicAsyncTask music = null;
	private String TAG = "com.sunny.kbc.kbcmainmenuactivity";
	private AdView adView = null;
	private final String PUBLISHER_ID = "a151334171403af";
	private final String TTS_MESSAGE = "Install TexttoSpeech package for Narration feature";
	private final String pButton = "Yes";
	private final String nButton = "No Thanks";
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		mContext = getApplicationContext();
		mDisplayMetrics = new DisplayMetrics();
		overridePendingTransition(R.anim.push_left_in_activity, R.anim.slide_in_up);
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		ScreenDimension.getSingletonObject().setDeviceWidth(mDisplayMetrics.widthPixels);
		ScreenDimension.getSingletonObject().setDeviceHeight(mDisplayMetrics.heightPixels);
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, 1);
		
		try {
			KbcQuestionActivity.insertImages();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		
		adView = new AdView(this, AdSize.BANNER, PUBLISHER_ID);
		setBackgroundLayout();
		getBackgroundLayout().addView(KbcQuestionActivity.putGapHeight(mContext, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100));
		getBackgroundLayout().addView(putPlayButton());
		getBackgroundLayout().addView(KbcQuestionActivity.putGapHeight(mContext, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100));
		getBackgroundLayout().addView(putHighScoreButton());
		getBackgroundLayout().addView(KbcQuestionActivity.putGapHeight(mContext, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100));
		getBackgroundLayout().addView(putInstructionButton());
		getBackgroundLayout().addView(KbcQuestionActivity.putGapHeight(mContext, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100));
		getBackgroundLayout().addView(putAboutUs());
		getBackgroundLayout().addView(KbcQuestionActivity.putGapHeight(mContext, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100));
		getBackgroundLayout().addView(rateThisApplication());
		getBackgroundLayout().addView(KbcQuestionActivity.putGapHeight(mContext, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100));		
		getBackgroundLayout().addView(exitApplication());
		getBackgroundLayout().addView(adView);
	    
	    adView.loadAd(new AdRequest());
	    
		ScrollView scrollLayout = new ScrollView(mContext);
		scrollLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		scrollLayout.setBackgroundColor(Color.WHITE);
		scrollLayout.addView(getBackgroundLayout());		
		
		setContentView(scrollLayout);
		
	}
	
	private View putHighScoreButton() {
		highScoreButton = new TextView(mContext);
		highScoreButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		highScoreButton.setBackgroundResource(R.drawable.highscore);
		highScoreButton.setGravity(Gravity.CENTER);
		highScoreButton.getBackground().setAlpha(210);
		highScoreButton.startAnimation(KbcAnimation.getObject().slideUpAnimation(mContext));
		highScoreButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadHighScores();
			}
		});
		
		return highScoreButton;
	}
	
	private View putInstructionButton(){
		instructionButton = new TextView(mContext);
		instructionButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		instructionButton.setBackgroundResource(R.drawable.instructions);
		instructionButton.setGravity(Gravity.CENTER);
		instructionButton.getBackground().setAlpha(210);
		instructionButton.startAnimation(KbcAnimation.getObject().slideUpAnimation(mContext));
		instructionButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			loadInstructions();
		}
	});		
		return instructionButton;
	}
	
	private View putAboutUs(){
		aboutUsButton = new TextView(mContext);
		aboutUsButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		aboutUsButton.setBackgroundResource(R.drawable.aboutus);
		aboutUsButton.setGravity(Gravity.CENTER);
		aboutUsButton.getBackground().setAlpha(210);
		aboutUsButton.startAnimation(KbcAnimation.getObject().slideUpAnimation(mContext));
		
		aboutUsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadAboutUs();
			}
		});
		
		return aboutUsButton;		
	}
	
	private View rateThisApplication(){
		rateButton = new TextView(mContext);
		rateButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		rateButton.setBackgroundResource(R.drawable.ratethis);
		rateButton.setGravity(Gravity.CENTER);
		rateButton.getBackground().setAlpha(210);
		rateButton.setAnimation(KbcAnimation.getObject().slideUpAnimation(mContext));
		
		rateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchMarket();
			}
		});
		
		return rateButton;		
	}
	
	private void launchMarket() {
		
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+KbcConstants.APP_PNAME));
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if(list.size() > 0){
        	startActivity(intent);
        }
        else{
        	Log.e("Alert","Market is not installed");
        }		
	}	
	
	private View exitApplication(){
		exitButton = new TextView(mContext);
		exitButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		exitButton.setBackgroundResource(R.drawable.exit);
		exitButton.setGravity(Gravity.CENTER);
		exitButton.getBackground().setAlpha(210);
		exitButton.startAnimation(KbcAnimation.getObject().slideUpAnimation(mContext));
		
		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		return exitButton;
	}
	
	private LinearLayout setBackgroundLayout(){
		background = new LinearLayout(mContext);
		background.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		background.setBackgroundResource(R.drawable.b2);
		//background.setBackgroundColor(Color.WHITE);
		background.setOrientation(LinearLayout.VERTICAL);
		
		return background;
	}
	
	private static LinearLayout getBackgroundLayout(){
		return background;
	}
	
	private View putPlayButton(){
		playButton = new TextView(mContext);
		playButton.setBackgroundResource(R.drawable.playbutton);
		playButton.setGravity(Gravity.CENTER);
		playButton.setClickable(true);
		playButton.getBackground().setAlpha(210);
		playButton.startAnimation(KbcAnimation.getObject().slideUpAnimation(mContext));
		
		playButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View mView) {
				removeAllView(getBackgroundLayout());
				/*AlertDialog alert = chooseLanguage().create();
				alert.show();*/
				/*music = new KbcMusicAsyncTask(mContext);
				music.execute(R.raw.raisebar);*/
				try {
					loadMoneyBar();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		return playButton;
	}
	
	public static void removeAllView(LinearLayout layout){
		if(layout != null)
			layout.removeAllViewsInLayout();
	}
	
	private void loadMoneyBar() throws Exception{
		ScrollView scrollView = addScrollView(mContext);
		scrollView.addView(new KbcFixBarView(mContext, KbcMainMenuActivity.this, "", 100));
		this.setContentView(scrollView);
	}
	
	private void loadAboutUs(){
		startActivity(new Intent(KbcMainMenuActivity.this, KbcAboutUsActivity.class));
	}
	
	private void loadHighScores(){
		startActivity(new Intent(KbcMainMenuActivity.this, KbcHighScoresActivity.class));
	}
	
	private void loadInstructions(){
		startActivity(new Intent(KbcMainMenuActivity.this, KbcInstructionsActivity.class));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(music != null)
			music.stop();
		if(adView != null)
			adView.destroy();		
	}
	
	public static ScrollView addScrollView(Context context){
		ScrollView scrollView = new ScrollView(context);
		scrollView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		return scrollView;
	}
	
	public AlertDialog.Builder chooseLanguage(String message, String positiveButton, String negativeButton){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage(message);
		alert.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				installTTS();
			}
		});
		
		alert.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(KbcConstants.TTS_SETTINGS, true);
				KbcSplashScreenActivity.mKbcPrefEditor.commit();				
			}
		});
		
		return alert;
	}
	
	private void checkLanguage(){
		boolean flag = false;
		Locale[] locale = Locale.getAvailableLocales();
		for(int i = 0; i < locale.length; i++){
			if(locale[i].toString().trim().equals("hi")){
				flag = true;
				break;
			}
		}
		if(!flag){
			Toast toast = Toast.makeText(mContext, "Hindi is not supported in your phone", Toast.LENGTH_SHORT);
			toast.show();
		}		
	}
	
	private void installTTS() {
		try {
			Intent installIntent = new Intent();
			installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
			startActivity(installIntent);
			Toast.makeText(getApplicationContext(),"The package is installed now", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}		
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == 1){
    		if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
    			Log.i(TAG, "TTS Package installed");
    		} 
    		else {
    			if(!KbcSplashScreenActivity.mSharedPreferences.getBoolean(KbcConstants.TTS_SETTINGS, false))
    				chooseLanguage(TTS_MESSAGE, pButton, nButton);
    		}
    	}
    }
}