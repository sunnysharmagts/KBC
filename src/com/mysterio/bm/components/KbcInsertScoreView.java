package com.mysterio.bm.components;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.mysterio.bm.KbcAnimation;
import com.mysterio.bm.KbcConstants;
import com.mysterio.bm.ScreenDimension;
import com.mysterio.bm.activity.KbcHighScoresActivity;
import com.mysterio.bm.activity.KbcMainMenuActivity;
import com.mysterio.bm.activity.KbcQuestionActivity;
import com.mysterio.bm.sqlite.KbcDBCategory;
import com.mysterio.bm.R;

public class KbcInsertScoreView extends LinearLayout implements OnClickListener{

	private static LinearLayout scoreLayout;
	private static ImageView imageView;
	private TextView scoreTextView;
	private EditText nameTextView;
	private Context context;
	private Button submitButton;
	private KbcQuestionActivity currentActivity;
	private String score = null;
	private AlertDialog.Builder mErrorDialog = null;
	private AlertDialog mErrorAlert = null;
	private KbcDBCategory db = null;
	private AdView adView = null;
	private final String PUBLISHER_ID = "a151334171403af";	
	
	public KbcInsertScoreView(Context context , String score, KbcQuestionActivity currentActivity) throws Exception{
		super(context);
		this.context = context;
		this.currentActivity = currentActivity;
		this.score = score;
		
		if(KbcDBCategory.mDB == null)
			db = new KbcDBCategory(context);
		scoreForm(score);
	}
	
	public void scoreForm(String score){
		adView = new AdView(currentActivity, AdSize.BANNER, PUBLISHER_ID);
		addMainLayout();
		getMainLayout().addView(addImage());
		getMainLayout().addView(KbcQuestionActivity.putGapHeight(context, ScreenDimension.getSingletonObject().getDeviceHeight() * 3/100));
		getMainLayout().addView(addScoreLayout(LinearLayout.HORIZONTAL));
		getScoreLayout().addView(addName());
		getScoreLayout().addView(addTextScore(score));
		addScoreLayout(LinearLayout.HORIZONTAL);
		getScoreLayout().addView(KbcQuestionActivity.putGapHeight(context, ScreenDimension.getSingletonObject().getDeviceHeight() * 15/100));
		getScoreLayout().addView(addButton("Submit", "submit"));
		getScoreLayout().addView(KbcQuestionActivity.putGapWidth(context, ScreenDimension.getSingletonObject().getDeviceWidth() * 5/100));
		getScoreLayout().addView(addButton("cancel", "cancel"));
		getMainLayout().addView(getScoreLayout());
		getMainLayout().startAnimation(KbcAnimation.getObject().slideLeftinAnimation(context));
		getMainLayout().addView(adView);
		adView.loadAd(new AdRequest());
	}
	
	public void addMainLayout(){
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.WHITE);
		this.setGravity(Gravity.CENTER);
	}
	
	public LinearLayout addScoreLayout(int orientation){
		scoreLayout = new LinearLayout(context);
		scoreLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		scoreLayout.setOrientation(orientation);
		scoreLayout.setGravity(Gravity.CENTER);
		
		return scoreLayout;
	}
	
	public static LinearLayout getScoreLayout(){
		return scoreLayout;
	}
	
	public LinearLayout getMainLayout(){
		return this;
	}
	
	public ImageView addImage(){
		imageView = new ImageView(context);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		imageView.setImageResource(R.drawable.icon);
		return imageView;
	}
	
	public EditText addName(){
		nameTextView = new EditText(context);
		nameTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		nameTextView.setWidth(ScreenDimension.getSingletonObject().getDeviceWidth() * 50 / 100);
		nameTextView.setHeight(ScreenDimension.getSingletonObject().getDeviceHeight() * 2 / 100);
		nameTextView.setHint("e.g:- John");
		nameTextView.setFocusable(true);
		
		return nameTextView;
	}
	
	public String getName(){
		return nameTextView.getText().toString().trim();
	}
	
	public TextView addTextScore(String score){
		scoreTextView = new TextView(context);
		scoreTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		scoreTextView.setText(" has scored Rs. " + score);
		return scoreTextView;
	}
	
	public Button addButton(String text, String tag){
		submitButton = new Button(context);
		submitButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		submitButton.setBackgroundResource(R.drawable.blue_button);
		submitButton.setTag(tag);
		submitButton.setText(text);
		submitButton.setOnClickListener(this);
		return submitButton;
	}

	@Override
	public void onClick(View v) {
		if(v.getTag() == "cancel"){
			startActivity(KbcMainMenuActivity.class);
		}
		
		if(v.getTag() == "submit"){
			if(!isValid())
				showValidationError();
			else{
				insertIntoDB();
				startActivity(KbcHighScoresActivity.class);
			}	
		}
	}
	
	public void startActivity(Class destinedClass){
		Intent intent = new Intent();
		intent.setClass(context, destinedClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		currentActivity.finish();
		
	}
	
	public void insertIntoDB(){
		ContentValues contentValues = new ContentValues();
		contentValues.put(KbcConstants.PLAYER_NAME, getName());
		contentValues.put(KbcConstants.SCORE, score);
		KbcDBCategory.mDB.insert(KbcConstants.DB_TABLE, null, contentValues);
	}
	
	public boolean isValid(){
		if(getName() != null && !getName().equals(""))
			return true;
		else
			return false;
	}
	
	private void showValidationError(){
		mErrorDialog = new AlertDialog.Builder(currentActivity);
		mErrorDialog.setTitle("CAUTION");
		mErrorDialog.setMessage("Please enter the name");
		mErrorDialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int val) {
						mErrorAlert.cancel();
					}
				});
		mErrorAlert = mErrorDialog.create();
		mErrorAlert.show();
	}
}
