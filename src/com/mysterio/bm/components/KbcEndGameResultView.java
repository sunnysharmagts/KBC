package com.mysterio.bm.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysterio.bm.KbcAnimation;
import com.mysterio.bm.ScreenDimension;
import com.mysterio.bm.activity.KbcQuestionActivity;
import com.mysterio.bm.R;

public class KbcEndGameResultView extends LinearLayout {
	
	public KbcEndGameResultView(Context mContext, String score){
		super(mContext);
		String[] getScore = KbcFixBarView.getMoneyBar();
		this.startAnimation(KbcAnimation.getObject().slideRightinAnimation(mContext));
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		if(score.equalsIgnoreCase(getScore[(getScore.length) - 1])){
			this.addView(KbcQuestionActivity.putGapHeight(mContext, ScreenDimension.getSingletonObject().getDeviceHeight() * 15 / 100));
			this.addView(addImage(mContext, R.drawable.jackpot, ""));
		}
		else{
			this.addView(KbcQuestionActivity.putGapHeight(mContext, ScreenDimension.getSingletonObject().getDeviceHeight() * 10 / 100));
			this.addView(addImage(mContext, R.drawable.result, ""));
			this.addView(KbcQuestionActivity.putGapHeight(mContext, ScreenDimension.getSingletonObject().getDeviceHeight() * 2 / 100));
			this.addView(addImage(mContext, R.drawable.resultscore, "	Rs. "+score));			
		}
	}
	
	private TextView addImage(Context mContext, int image, String text){
		TextView textView = new TextView(mContext);
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		textView.setBackgroundResource(image);
		textView.setText(text);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setTextColor(Color.WHITE);
		textView.setTypeface(Typeface.DEFAULT_BOLD);
		return textView;
	}
	
}
