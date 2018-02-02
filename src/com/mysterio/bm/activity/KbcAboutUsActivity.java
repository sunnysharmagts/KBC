package com.mysterio.bm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.mysterio.bm.KbcConstants;
import com.mysterio.bm.R;
import com.mysterio.bm.ScreenDimension;

public class KbcAboutUsActivity extends Activity implements OnClickListener{
	
	private LinearLayout mainLayout, mTextLinearLayout;
	private TextView mAboutUsView, mMailMeView;
	private Context mContext;
	private ImageView mKbcImageView = null;
	private Button submitButton = null;
	private AdView adView = null;
	private final String PUBLISHER_ID = "a151334171403af";	
/*	public KbcAboutUsView(Context mContext){
		super(mContext);*/
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		adView = new AdView(this, AdSize.IAB_MRECT, PUBLISHER_ID);		
		mContext = getApplicationContext();
		mainLayout = new LinearLayout(mContext);
		mainLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mainLayout.setBackgroundColor(Color.WHITE);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		mainLayout.setPadding(ScreenDimension.getSingletonObject().getDeviceWidth() * 2 / 100, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100, ScreenDimension.getSingletonObject().getDeviceWidth() * 2 / 100, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100);
		
		mKbcImageView = new ImageView(mContext);
		mKbcImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mKbcImageView.setBackgroundResource(R.drawable.icon);
		
		mainLayout.addView(mKbcImageView);

		mTextLinearLayout = new LinearLayout(mContext);
		mTextLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mTextLinearLayout.setBackgroundColor(Color.WHITE);
		mTextLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mTextLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		
		mAboutUsView = new TextView(mContext);
		mAboutUsView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mAboutUsView.setText(KbcConstants.FEEDBACK_STATEMENT);
		mAboutUsView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
		mAboutUsView.setPadding(0, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100, 0, 0);
		mAboutUsView.setTextColor(Color.BLACK);
		
		mTextLinearLayout.addView(mAboutUsView);
		
		mMailMeView = new TextView(mContext);
		mMailMeView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mMailMeView.setTextColor(Color.BLUE);
		mMailMeView.setText(" "+ KbcConstants.MAIL_ID);
		mMailMeView.setTag("text");
		mMailMeView.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
		mMailMeView.setFocusable(true);
		
		mMailMeView.setOnClickListener(this);
		mTextLinearLayout.addView(mMailMeView);
		
		mainLayout.addView(mTextLinearLayout);
		mainLayout.addView(KbcQuestionActivity.putGapHeight(mContext, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100));
		mainLayout.addView(addButton("email now.."));
		mainLayout.addView(adView);
		adView.loadAd(new AdRequest());
		
		ScrollView scrollLayout = new ScrollView(mContext);
		scrollLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		scrollLayout.setBackgroundColor(Color.WHITE);
		scrollLayout.addView(mainLayout);
		setContentView(scrollLayout);
		
	}

	@Override
	public void onClick(View v) {
			startActivity(new Intent(KbcAboutUsActivity.this, KbcSendFeedbackMailActivity.class));
	}
	
	public Button addButton(String text){
		submitButton = new Button(mContext);
		submitButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		submitButton.setBackgroundResource(R.drawable.blue_button);
		submitButton.setText(text);
		submitButton.setTag("button");
		submitButton.setOnClickListener(this);
		return submitButton;
	}
}
