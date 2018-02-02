package com.mysterio.bm.lifeline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mysterio.bm.R;

public class KbcExpertAdviceView extends View {

	private Context context;
	private int pause;
	private Integer[] imageID = {R.drawable.einstein, R.drawable.abdulkalam, R.drawable.mark, R.drawable.sachin, R.drawable.stevejobs};
	private String[] names = {"Albert Einstein", "Dr. A.P.J Abdul Kalam Azad", "Mark Zuckerberg", "Sachin Tendulkar", "Steve Jobs"};
	TextView imageView, celebrityView;
	private String rightAnswer;
	public KbcExpertAdviceView(Context context, String rightAnswer, int pause) {
		super(context);
		this.context = context;
		this.rightAnswer = rightAnswer;
		this.pause = pause;
		final Item[] items = new Item[names.length];
		for(int i = 0; i < imageID.length; i++)
			items[i] = new Item(names[i], imageID[i]);

			ListAdapter adapter = new ArrayAdapter<Item>(context,android.R.layout.select_dialog_item,android.R.id.text1,items){
			        public View getView(int position, View convertView, ViewGroup parent) {
			            //User super class to create the View
			            View v = super.getView(position, convertView, parent);
			            TextView tv = (TextView)v.findViewById(android.R.id.text1);

			            //Put the image on the TextView
			            tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

			            //Add margin between image and text (support various screen densities)
			            int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
			            tv.setCompoundDrawablePadding(dp5);

			            return v;
			        }
			    };
			    showDialog(adapter);
	}
	
	private void showDialog(final ListAdapter adapter){
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setTitle("Expert Advice");
	    builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			
			private int icon;
			private String name;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				name = ((Item)adapter.getItem(which)).text;
				icon = ((Item)adapter.getItem(which)).icon;
				showCelebrityDialog(icon ,name ,"c");				
			}
		}).show();
	}
	
	private void showCelebrityDialog(int icon, String name, String alertMessage){
		final Dialog dialog = new Dialog(context);
		LinearLayout mCelebrityLayout = new LinearLayout(context);
		mCelebrityLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mCelebrityLayout.setOrientation(LinearLayout.HORIZONTAL);
		mCelebrityLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		
		imageView = new TextView(context);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//imageView.setImageResource(icon);
		imageView.setBackgroundResource(icon);
		imageView.setGravity(Gravity.CENTER);
		mCelebrityLayout.addView(imageView);
		
		celebrityView = new TextView(context);
		celebrityView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		celebrityView.setText("I think the answer is "+rightAnswer.toUpperCase());
		celebrityView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		
		mCelebrityLayout.addView(celebrityView);
		mCelebrityLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.setContentView(mCelebrityLayout);
		dialog.setTitle(name);
		dialog.show();
		
	}
	
	public static class Item{
	    public String text;
	    public final int icon;
	    public Item(String text, Integer icon) {
	        this.text = text;
	        this.icon = icon;
	    }
	    @Override
	    public String toString() {
	        return text;
	    }
	}
}
