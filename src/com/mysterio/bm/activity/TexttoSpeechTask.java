package com.mysterio.bm.activity;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class TexttoSpeechTask extends AsyncTask<Void, Void, Void>{

	TextToSpeech tts = null;
	String speechText = null;
	String optionText = null;
	KbcQuestionActivity currentActivity = null;
	Context context = null;
	String[] options = null;
	String[] optionNo = new String[]{"A", "B", "C", "D"};
	
	public TexttoSpeechTask(KbcQuestionActivity currentActivity, Context context, String speechText, String options){
		this.currentActivity = currentActivity;
		this.context = context;
		this.options = options.split("~");
		this.speechText = speechText;
	}
    
    private void speak(String speechText, int queueMode) throws Exception {
    	tts.setSpeechRate(1);
    	tts.speak(speechText, queueMode, null);
    	for(int i = 0; i < options.length; i++){
    		if(i == options.length - 1)
    			tts.speak("and", TextToSpeech.QUEUE_ADD, null);
    		tts.speak("option"+ optionNo[i], TextToSpeech.QUEUE_ADD, null);
    		tts.speak(options[i], TextToSpeech.QUEUE_ADD, null);	
    	}
    }

	@Override
	protected Void doInBackground(Void... params) {
		try {
			init(speechText, TextToSpeech.QUEUE_FLUSH);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void onDestroy(){
    	if(tts != null){
    		tts.stop();
    		tts.shutdown();
    	}
	}
	
	private void init(final String speechText, final int queueMode) throws Exception {
        tts = new TextToSpeech(currentActivity, new OnInitListener() {
			@Override
			public void onInit(int status) {
				if(status == TextToSpeech.SUCCESS){
					int result = 0;
					try {
						result = tts.setLanguage(Locale.UK);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						result = 1;
					}
					if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				        Log.e("", "Language is not available.");
		                Intent intent = new Intent();
		                intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		                currentActivity.startActivityForResult(intent, 1);				        
				                
				    } 
					else{
						new Handler().postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									speak(speechText, queueMode);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} , 1000);
						
					}
				}
				 else {
			            Log.e("", "Could not initialize TextToSpeech.");
			        }
			}
		});
	}
}