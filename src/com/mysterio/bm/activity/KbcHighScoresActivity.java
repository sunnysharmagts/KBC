package com.mysterio.bm.activity;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mysterio.bm.KbcConstants;
import com.mysterio.bm.R;
import com.mysterio.bm.ScreenDimension;
import com.mysterio.bm.sqlite.KbcDBCategory;

public class KbcHighScoresActivity extends Activity {

	private Context mContext;
	private LinearLayout[] mHighScoreLayout;
	private TextView[] highScoreNameView, highScoreAmountView, highScoreIDView;
	private ListView mListView;
	private Cursor cursor = null;
	private ArrayList<String> playerName = new ArrayList<String>();
	private ArrayList<String> playerScore = new ArrayList<String>();
	private int cursorCount;
	private KbcDBCategory db = null;
	private int position;
	ScoreAdapter adapter = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
				
		if(KbcDBCategory.mDB == null)
			try {
				db = new KbcDBCategory(mContext);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			cursor = KbcDBCategory.mDB.query(KbcConstants.DB_TABLE, new String[]{KbcConstants.KEY_ROWID, KbcConstants.PLAYER_NAME, KbcConstants.SCORE }, null, null, null, null, null);
			startManagingCursor(cursor);
			cursorCount = cursor.getCount();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			cursorCount = 0;
		}
		//cursor.close();
		if(cursorCount == 0)
			setContentView(noHighScoreAvailable());
		else{
			int i = 0;
			cursor.moveToFirst();
			do{
				String playerName = cursor.getString(cursor.getColumnIndex(KbcConstants.PLAYER_NAME));
				String playerScore = cursor.getString(cursor.getColumnIndex(KbcConstants.SCORE));
				this.playerName.add(playerName);
				this.playerScore.add(playerScore);
				i++;
			}
			while(cursor.moveToNext());
			cursor.close();
			setContentView(highScoreAvailable());
		}

	}

	class ScoreAdapter extends BaseAdapter{

		public ScoreAdapter(){
			mHighScoreLayout = new LinearLayout[cursorCount];
			highScoreIDView = new TextView[cursorCount];
			highScoreNameView = new TextView[cursorCount];
			highScoreAmountView = new TextView[cursorCount];
			
			for(int i = 0; i < cursorCount; i++){
				mHighScoreLayout[i] = new LinearLayout(mContext);
				mHighScoreLayout[i].setOrientation(LinearLayout.HORIZONTAL);
				mHighScoreLayout[i].setBackgroundResource(R.drawable.highscorestab);
				mHighScoreLayout[i].setGravity(Gravity.CENTER_VERTICAL);

				highScoreIDView[i] = new TextView(mContext);
				highScoreIDView[i].setText((i+1)+".");
				highScoreIDView[i].setTextColor(Color.BLACK);
				highScoreIDView[i].setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
				highScoreIDView[i].setPadding(ScreenDimension.getSingletonObject().getDeviceWidth() * 5 / 100, 0, ScreenDimension.getSingletonObject().getDeviceWidth() * 5 / 100, 0);
				mHighScoreLayout[i].addView(highScoreIDView[i]);
				
				highScoreNameView[i] = new TextView(mContext);
				highScoreNameView[i].setPadding(ScreenDimension.getSingletonObject().getDeviceWidth() * 5 / 100, 0, ScreenDimension.getSingletonObject().getDeviceWidth() * 5 / 100, 0);
				highScoreNameView[i].setText(playerName.get(i));
				highScoreNameView[i].setTextColor(Color.WHITE);
				highScoreNameView[i].setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
				mHighScoreLayout[i].addView(highScoreNameView[i]);
				
				highScoreAmountView[i] = new TextView(mContext);
				highScoreAmountView[i].setPadding(ScreenDimension.getSingletonObject().getDeviceWidth() * 5 / 100, 0, ScreenDimension.getSingletonObject().getDeviceWidth() * 5 / 100, 0);
				highScoreAmountView[i].setText("Rs. " + playerScore.get(i));
				highScoreAmountView[i].setTextColor(Color.WHITE);
				highScoreAmountView[i].setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
				mHighScoreLayout[i].addView(highScoreAmountView[i]);
			}
		}
		
		@Override
		public int getCount() {
			return cursorCount;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return mHighScoreLayout[position];
		}
		
	}
	
	private RelativeLayout noHighScoreAvailable(){
		
		RelativeLayout relativeLayout = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		relativeLayout.setBackgroundColor(Color.WHITE);
		
		TextView textView = new TextView(mContext);
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		textView.setText("No High Score Available");
		textView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC);
		textView.setTextColor(Color.BLUE);
		textView.setTextSize(20);
		textView.setId(1);
		
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, textView.getId());
		
		relativeLayout.addView(textView);
		
		return relativeLayout;
	}
	
	private void cleanScoreBoard(){
		KbcDBCategory.mDB.delete(KbcConstants.DB_TABLE, null, null);
	}
	
	private void clearnScoreBoard(int position){
		KbcDBCategory.mDB.delete(KbcConstants.DB_TABLE, KbcConstants.KEY_ROWID + " = "+ position, null);
	}
	
	private ListView highScoreAvailable(){
		mListView = new ListView(mContext);
		mListView.setBackgroundColor(Color.TRANSPARENT);
		adapter = new ScoreAdapter();
		mListView.setAdapter(adapter);
		registerForContextMenu(mListView);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//adapter.notifyDataSetChanged();
				KbcHighScoresActivity.this.position = position;
			}
		});
		return mListView;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Menu");
		menu.add("Facebook");
		menu.add("Delete All");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		if(item.getTitle().equals("Delete All")){
			cleanScoreBoard();
			adapter.notifyDataSetChanged();
			startActivity(new Intent(this, KbcHighScoresActivity.class));
		}
		
		else if(item.getTitle().equals("Delete")){
			clearnScoreBoard(position);
			//startActivity(new Intent(this, KbcHighScoresActivity.class));
		}
		
		else if(item.getTitle().equals("Facebook")){
			postFacebook();
		}
		return true;
	}
	
	private void postFacebook(){
		String picture = "https://sites.google.com/site/sunnyslls/Home/icon.png";
		String playerName = highScoreNameView[position].getText().toString();
		String amount = highScoreAmountView[position].getText().toString();
		Intent intent = new Intent();
		intent.setClass(this, FaceBookIntegration.class);
		intent.putExtra("picture", picture);
		intent.putExtra("name", playerName);
		intent.putExtra("amount", amount);
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			startActivity(new Intent(this, KbcMainMenuActivity.class));
			finish();
		}
		return true;
	}
}
