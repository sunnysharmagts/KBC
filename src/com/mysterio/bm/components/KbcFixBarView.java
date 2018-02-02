package com.mysterio.bm.components;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysterio.bm.KbcAnimation;
import com.mysterio.bm.KbcConstants;
import com.mysterio.bm.ScreenDimension;
import com.mysterio.bm.activity.KbcMainMenuActivity;
import com.mysterio.bm.activity.KbcQuestionActivity;
import com.mysterio.bm.activity.KbcSplashScreenActivity;
import com.mysterio.bm.R;

public class KbcFixBarView extends LinearLayout/* implements OnClickListener*/{
	
	private Context mContext;
	private TextView[] barView;
	private Handler mHandler;
	//private DisplayMetrics mDisplayMetrics;
	//private static int mDeviceWidth, mDeviceHeight;
	private static int i, focusBar, whiteText;
	private String tag;
	private static final String[] moneyBar = new String[] {"5,000", "10,000", "20,000", "40,000", "80,000", "1,60,000", "3,20,000", "6,40,000", "12,50,000", "25,00,000", "50,00,000", "10,000,000", "50,000,000"};
	KbcMainMenuActivity kbcMainMenuActivity;	
	
	public KbcFixBarView(Context mContext, KbcMainMenuActivity kbcMainMenuActivity, String tag, int focusBar) throws Exception {
		super(mContext);
		this.mContext = mContext;
		this.kbcMainMenuActivity = kbcMainMenuActivity;
		this.tag = tag;
		this.focusBar = focusBar;
		
		setBackgroundLayout();
		barView = new TextView[moneyBar.length];
		
		for(i = moneyBar.length - 1; i >= 0; i--){
			getBackgroundLayout().addView(getBarView(i));
			if(tag == "")
				getBackgroundLayout().addView(KbcQuestionActivity.putGapHeight(mContext, ScreenDimension.getSingletonObject().getDeviceHeight() * 2 / 100));
		}
	}
	
	public View getBarView(final int i) throws Exception{
		barView[i] = new TextView(mContext);
		barView[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		barView[i].startAnimation(KbcAnimation.getObject().slideUpAnimation(mContext));
		int setBar = KbcSplashScreenActivity.mSharedPreferences.getInt(KbcConstants.RAISE_BAR, -1);
		if(i == focusBar)
			barView[i].setBackgroundResource(R.drawable.optionorange);
		else if(i == setBar){
			barView[i].setBackgroundResource(R.drawable.optiongreen);
			barView[i].setTextColor(Color.WHITE);
		}
		
		else{
			barView[i].setBackgroundResource(R.drawable.optionblue);
			barView[i].setTextColor(Color.rgb(240, 140, 0));
		}	
		barView[i].setGravity(Gravity.CENTER);
		barView[i].getBackground().setAlpha(210);
		barView[i].setText(moneyBar[i]);
		if(i == 11 || i== 12)
			barView[i].setTextColor(Color.WHITE);
			
		if(tag == "")
			barView[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for(int i = 0; i < moneyBar.length; i++)
						barView[i].setClickable(false);
					v.setBackgroundResource(R.drawable.optiongreen);
					barView[i].setTextColor(Color.WHITE);
					KbcSplashScreenActivity.mKbcPrefEditor.putInt(KbcConstants.RAISE_BAR, i);
					KbcSplashScreenActivity.mKbcPrefEditor.commit();
					/*mHandler = new Handler();
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {*/
							Intent i = new Intent();
							i.setClass(mContext, KbcQuestionActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							mContext.startActivity(i);
							kbcMainMenuActivity.finish();
					/*	}
					},KbcQuestionActivity.POSTDELAYED_TIME);*/
					
				}
			});
		
		return barView[i];
	}
	
	private void setBackgroundLayout() {
		Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
		this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.WHITE);
		//this.setBackgroundResource(R.drawable.background);
		this.setPadding(0, ScreenDimension.getSingletonObject().getDeviceHeight() * 2 / 100, 0, ScreenDimension.getSingletonObject().getDeviceHeight() * 2 / 100);
		this.startAnimation(animation);
	}
	
	private LinearLayout getBackgroundLayout(){
		return this;
	}
	
	public static String[] getMoneyBar(){
		return moneyBar;
	}
}
