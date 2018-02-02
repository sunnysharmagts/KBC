package com.mysterio.bm.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.mysterio.bm.activity.KbcQuestionActivity;
import com.mysterio.bm.R;

public class KbcImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;

/*    private Integer[] mImageIds = {
	            R.drawable.audiencepoll, R.drawable.doubledip, R.drawable.phonefriend, R.drawable.expertchoice
    };*/    
    
    public KbcImageAdapter(Context c) {
        mContext = c;
        TypedArray attr = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
        mGalleryItemBackground = attr.getResourceId(R.styleable.HelloGallery_android_galleryItemBackground, 0);
        attr.recycle();
    }

    public int getCount() {
        return KbcQuestionActivity.getImages().size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);

        imageView.setImageResource(KbcQuestionActivity.getImages().get(String.valueOf(position)));
        imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(mGalleryItemBackground);

        return imageView;
    }
}