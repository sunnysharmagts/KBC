package com.mysterio.bm.components;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.mysterio.bm.KbcConstants;
import com.mysterio.bm.activity.KbcSplashScreenActivity;

public class KbcMusicAsyncTask extends AsyncTask<Integer, Void, Void> implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

	private MediaPlayer mMediaPlayer = null;
	private String data;
	private Context context = null;

	public KbcMusicAsyncTask(Context context) {
		this.context = context;
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		if(KbcSplashScreenActivity.mSharedPreferences.getBoolean(KbcConstants.SOUND_SETTINGS, true))
			mp.start();
	}


	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		mp.reset();
		return true;
	}

	@Override
	protected Void doInBackground(Integer... params) {
		try{
			if(KbcSplashScreenActivity.mSharedPreferences.getBoolean(KbcConstants.SOUND_SETTINGS, true)){
				if(mMediaPlayer != null){
					mMediaPlayer.release();
				}
				mMediaPlayer = MediaPlayer.create(context, params[0]);
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mMediaPlayer.setOnPreparedListener(this);
				mMediaPlayer.setOnErrorListener(this);
				mMediaPlayer.start();
			}
		}
		catch (IllegalStateException e) {

		}
		catch (RuntimeException e) {
			
		}
		return null;
	}
	
	public void stop(){
		if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
	}
}
