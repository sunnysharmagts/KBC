package com.mysterio.bm.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.mysterio.bm.KbcConstants;
import com.mysterio.bm.R;
import com.mysterio.bm.ScreenDimension;

public class FaceBookIntegration extends Activity{

	//private static final String APP_ID = "269876589726953";
/*	private static final String[] PERMISSIONS = new String[] {"publish_stream"};

	private static final String TOKEN = "access_token";
        private static final String EXPIRES = "expires_in";
        private static final String KEY = "facebook-credentials";*/
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-credentials";
	private Facebook facebook;
	private String messageToPost;
	private ImageView imageView;
	private String name, amount, picture;

	public boolean saveCredentials(Facebook facebook) {
        	Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        	editor.putString(KbcConstants.FACEBOOK_ACCESS_TOKEN, facebook.getAccessToken());
        	editor.putLong(EXPIRES, facebook.getAccessExpires());
        	return editor.commit();
    	}

    	public boolean restoreCredentials(Facebook facebook) {
        	SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        	facebook.setAccessToken(null);
        	facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
        	return facebook.isSessionValid();
    	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		amount = intent.getStringExtra("amount");
		picture = intent.getStringExtra("picture");
		
		facebook = new Facebook(KbcConstants.FACEBOOK_APPID);
		imageView = new ImageView(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(facebookView());

	}
	
	private LinearLayout facebookView(){
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setBackgroundColor(Color.WHITE);

		linearLayout.addView(KbcQuestionActivity.putGapHeight(this, ScreenDimension.getSingletonObject().getDeviceHeight() * 15 / 100));

		LinearLayout iconLayout = new LinearLayout(this);
		iconLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		iconLayout.setPadding(ScreenDimension.getSingletonObject().getDeviceWidth() * 50 / 100, 0, 0, 0);
		
		TextView iconView = new TextView(this);
		iconView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		iconView.setBackgroundResource(R.drawable.icon);
		iconView.setGravity(Gravity.CENTER_VERTICAL);
		iconLayout.addView(iconView);
		
		linearLayout.addView(iconLayout);
		
		linearLayout.addView(KbcQuestionActivity.putGapHeight(this, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100));
		
		TextView textView = new TextView(this);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		textView.setText("Do you wan't to post this score on your Facebook wall ?");
		textView.setTextColor(Color.BLACK);
		textView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		linearLayout.addView(textView);
		
		linearLayout.addView(KbcQuestionActivity.putGapHeight(this, ScreenDimension.getSingletonObject().getDeviceHeight() * 5 / 100));
		
		LinearLayout buttonLayout = new LinearLayout(this);
		buttonLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		Button yesButton = new Button(this);
		yesButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		yesButton.setBackgroundResource(R.drawable.blue_button);
		yesButton.setText("Yes");
		buttonLayout.addView(yesButton);
		
		yesButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				share();
			}
		});
		
		Button noButton = new Button(this);
		noButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		noButton.setBackgroundResource(R.drawable.blue_button);
		noButton.setText("No");
		buttonLayout.addView(noButton);
		noButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				doNotShare();
			}
		});
		
		Button logoutButton = new Button(this);
		logoutButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		logoutButton.setBackgroundResource(R.drawable.blue_button);
		logoutButton.setText("Logout");
		buttonLayout.addView(logoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				logout();
			}
		});
		
		linearLayout.addView(buttonLayout);
		
		return linearLayout;
		
	}

	public void doNotShare(){
		finish();
	}
	public void share(){
		
		if (!facebook.isSessionValid() ) {
			loginAndPostToWall();
		}
		else {
			postToWall();
		}
	}

	public void loginAndPostToWall(){
		 facebook.authorize(this, KbcConstants.FACEBOOK_PERMISSION, new LoginDialogListener());
	}
	
	public void logout(){
		try {
			facebook.logout(this);
			Util.clearCookies(this);
			restoreCredentials(facebook);
			finish();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	public void postToWall(){
		Bundle parameters = new Bundle();
		String msg = name + " has won "+ amount + " in KBC5. Play KBC5 and beat his score to post you high score on the wall";
		parameters.putString("name", name);
        parameters.putString("message", msg);
        parameters.putString("description", "");
        parameters.putString("picture", "https://sites.google.com/site/sunnyslls/Home/logo.jpg");
        parameters.putString("link", "");
        try {
			String response = facebook.request("me/feed", parameters, "POST");
			Log.d("Tests", "got response: " + response);
			if (response == null || response.equals("") ||
			        response.equals("false")) {
				showToast("Blank response.");
			}
			else {
				showToast("Message posted to your facebook wall!");
			}
			finish();
		} catch (Exception e) {
			showToast("Failed to post to wall");
			e.printStackTrace();
			finish();
		}
	}
	
	public byte[] image(){
		byte[] data = null;
		try{
			Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.sachin);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			data = stream.toByteArray();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return data;
	}

	class LoginDialogListener implements DialogListener {
	    public void onComplete(Bundle values) {
	    	saveCredentials(facebook);
	    	if (name != null && amount != null){
	    		postToWall();
		}
	    }
	    public void onFacebookError(FacebookError error) {
	    	showToast("Authentication with Facebook failed!");
	        finish();
	    }
	    public void onError(DialogError error) {
	    	showToast("Authentication with Facebook failed!");
	        finish();
	    }
	    public void onCancel() {
	    	showToast("Authentication with Facebook cancelled!");
	        finish();
	    }
	}

	private void showToast(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
}