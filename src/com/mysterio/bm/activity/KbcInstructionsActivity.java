package com.mysterio.bm.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mysterio.bm.KbcConstants;
import com.mysterio.bm.R;
import com.mysterio.bm.ScreenDimension;

public class KbcInstructionsActivity extends Activity{
	private LinearLayout[] detailsLayout;
	private static Context mContext;
	private static TextView divView, headerView, questionView, addFooter;
	private TextView[] imageView, dataView;
	private HashMap<String, Integer> mID = new HashMap<String, Integer>();
	private String[] description = new String[] {"Choose Audience poll to get audience reviews in form of percentage",
												 "Get a double chance by choose 2 options out of 4",
												 "Take help from renowned persons",
												 "If you don't know the answer of the question then choose this lifeline to flip the question",
												 "Enable/Disable Sound while playing game",
												 "Update your highest scores on Facebook and challenge your friends to beat it",
												 "Press menu button and check out the milestone for the bar you have set and prize money of the question you are playing for" };
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		mID.clear();
		mID.putAll(KbcQuestionActivity.getImages());
		mID.put("4", R.drawable.sound);
		mID.put("5", R.drawable.facebook);
		mID.put("6", R.drawable.icon);
		
		int length = mID.size();
		
		KbcQuestionActivity.setMainLayout(Color.LTGRAY, "color", mContext);
		LinearLayout linearLayout = KbcQuestionActivity.getMainLayout();
		
		linearLayout.addView(addHeader());
		linearLayout.addView(horizontalDivView(2));
		linearLayout.addView(addQuestionInfo());
		linearLayout.addView(horizontalDivView(2));
		detailsLayout = new LinearLayout[length];
		imageView = new TextView[length];
		dataView = new TextView[length];
		for(int i = 0; i < length; i++){
			linearLayout.addView(addInstructions(i, mID.get(String.valueOf(i)), description[i]));
			linearLayout.addView(horizontalDivView(2));
		}
		linearLayout.addView(addFooter());
		linearLayout.addView(horizontalDivView(2));		
		ScrollView scrollView = KbcMainMenuActivity.addScrollView(mContext);
		scrollView.addView(linearLayout);
		setContentView(scrollView);
	}
	
	public static TextView horizontalDivView(int heightParams){
		divView = new TextView(mContext);
		divView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, heightParams));
		divView.setBackgroundColor(Color.BLACK);
		
		return divView;
	}
	
	public static TextView verticalDivView(int heightParams){
		divView = new TextView(mContext);
		divView.setLayoutParams(new LayoutParams(heightParams, LayoutParams.FILL_PARENT));
		divView.setBackgroundColor(Color.BLACK);
		
		return divView;
	}	
	
	public LinearLayout addInstructions(int i, int image, String text){
		detailsLayout[i] = new LinearLayout(mContext);
		detailsLayout[i].setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		detailsLayout[i].setOrientation(LinearLayout.HORIZONTAL);
		detailsLayout[i].addView(addPicture(i, image));
		detailsLayout[i].addView(KbcQuestionActivity.putGapWidth(mContext, ScreenDimension.getSingletonObject().getDeviceWidth() * 2 / 100));
		detailsLayout[i].addView(verticalDivView(1));
		detailsLayout[i].addView(KbcQuestionActivity.putGapWidth(mContext, ScreenDimension.getSingletonObject().getDeviceWidth() * 2 / 100));
		detailsLayout[i].addView(addDetail(i, text));
		//detailsLayout[i].addView(horizontalDivView(1));

		return detailsLayout[i];
	}
	
	public TextView addPicture(int i, int image){
		imageView[i] = new TextView(mContext);
		imageView[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		imageView[i].setBackgroundResource(image);
		
		return imageView[i];
	}
	
	public TextView addDetail(int i, String text){	
		dataView[i] = new TextView(mContext);
		dataView[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dataView[i].setTextColor(Color.BLACK);
		dataView[i].setText(text);
		
		return dataView[i];
	}
	
	public TextView addHeader(){
		headerView = new TextView(mContext);
		headerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		headerView.setBackgroundColor(Color.GRAY);
		headerView.setText("Instructions");
		headerView.setTextColor(Color.BLACK);
		headerView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		
		return headerView;
	}
	
	public TextView addQuestionInfo(){
		questionView = new TextView(mContext);
		questionView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		questionView.setBackgroundColor(Color.GRAY);
		questionView.setText(KbcConstants.QUESTION_INFO);
		questionView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC);
		questionView.setTextColor(Color.BLACK);
		
		return questionView;
	}
	
	public TextView addFooter(){
		addFooter = new TextView(mContext);
		addFooter.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		addFooter.setBackgroundColor(Color.GRAY);
		addFooter.setText(KbcConstants.FACEBOOK);
		addFooter.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC);
		addFooter.setTextColor(Color.BLACK);
		
		return addFooter;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
			finish();
		return true;
	}
}
