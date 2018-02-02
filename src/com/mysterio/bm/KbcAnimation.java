package com.mysterio.bm;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class KbcAnimation {
	
	private static KbcAnimation animation;
	private KbcAnimation(){ }

	public static synchronized KbcAnimation getObject() {
		if(animation == null)
			animation = new KbcAnimation();
		
		return animation;
	}
	
	public Animation slideUpAnimation(Context context){
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
		return animation;
	}
	
	public Animation slideLeftinAnimation(Context context){
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
		return animation;
	}
	
	public Animation slideRightinAnimation(Context context){
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_right_in);
		return animation;		
	}
}
