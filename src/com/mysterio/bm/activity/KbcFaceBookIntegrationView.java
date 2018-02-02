package com.mysterio.bm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.mysterio.bm.KbcConstants;

public class KbcFaceBookIntegrationView extends View{
		
	private Facebook mFacebook = new Facebook(KbcConstants.FACEBOOK_APPID);
	AsyncFacebookRunner mAsyncFacebookRunner;
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-credentials";
	private Context mContext;
	
	public KbcFaceBookIntegrationView(Context mContext, String name, String playerName, String amount, String picture, String link){
		super(mContext);
		this.mContext = mContext;
		mFacebook = new Facebook(KbcConstants.FACEBOOK_APPID);
		if(!restoreCredentials(mFacebook)){
			loginAndPostToWall(mContext, name, playerName, amount, picture, link);
		}
		else
			postToWall(name, playerName, amount, picture, link);
	}
	
	public Boolean restoreCredentials(Facebook facebook){
    	SharedPreferences sharedPreferences = mContext.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    	facebook.setAccessToken(sharedPreferences.getString(KbcConstants.FACEBOOK_ACCESS_TOKEN, null));
    	facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
    	return facebook.isSessionValid();
	}
	
	public boolean saveCredentials(Facebook facebook) {
    	Editor editor = mContext.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
    	editor.putString(KbcConstants.FACEBOOK_ACCESS_TOKEN, facebook.getAccessToken());
    	editor.putLong(EXPIRES, facebook.getAccessExpires());
    	return editor.commit();
	}	
	
	public void loginAndPostToWall(final Context context, final String name, final String playerName, final String amount, final String picture, final String link){
		mFacebook.authorize((Activity) mContext, KbcConstants.FACEBOOK_PERMISSION, new DialogListener() {
			
			@Override
			public void onFacebookError(FacebookError arg0) {
				showToast("Authentication with Facebook failed!");
			}
			
			@Override
			public void onError(DialogError arg0) {
				showToast("Authentication with Facebook failed!");
				
			}
			
			@Override
			public void onComplete(Bundle arg0) {
		    	saveCredentials(mFacebook);
				postToWall(name, playerName, amount, picture, link);				
				
			}
			
			@Override
			public void onCancel() {
				showToast("Authentication with Facebook cancelled!");
				
			}
		});
	}
	
	private void postToWall(String name, String playerName, String amount, String picture, String link){
		
		String message = playerName + " has won "+ amount + " in KBC5. Play KBC5 and beat his score to post you high score on the wall";
		Bundle parameters = new Bundle();
		parameters.putString("name", name);
        parameters.putString("message", message);
        parameters.putString("description", "");
        parameters.putString("picture", picture);
        parameters.putString("link", link);
        try {
			String response = mFacebook.request("me/feed", parameters, "POST");
			Log.d("Tests", "got response: " + response);
			if (response == null || response.equals("") ||
			        response.equals("false")) {
				showToast("Blank response.");
			}
			else {
				showToast("Message posted to your facebook wall!");
			}
			//finish();
		} catch (Exception e) {
			showToast("Failed to post to wall");
			e.printStackTrace();
			//finish();
		}		
	}
	
	private void showToast(String message){
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	}
}
