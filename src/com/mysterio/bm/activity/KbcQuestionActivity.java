package com.mysterio.bm.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.mysterio.bm.KbcConstants;
import com.mysterio.bm.R;
import com.mysterio.bm.ScreenDimension;
import com.mysterio.bm.components.KbcEndGameResultView;
import com.mysterio.bm.components.KbcFixBarView;
import com.mysterio.bm.components.KbcGraphView;
import com.mysterio.bm.components.KbcImageAdapter;
import com.mysterio.bm.components.KbcInsertScoreView;
import com.mysterio.bm.components.KbcMusicAsyncTask;
import com.mysterio.bm.lifeline.KbcExpertAdviceView;
import com.mysterio.bm.sqlite.KbcDBCategory;

public class KbcQuestionActivity extends Activity {

	private static Context mContext, CurrentActivity;
	private LinearLayout lifeLineLayout;
	private static LinearLayout mainLayout;
	private LinearLayout mQuestionLayout, mCountDownTimeLayout;
	private TextView questionTextView;
	private static TextView gapWidthView, gapHeightView;
	private static TextView[] optionsView;
	private int[] widths;
	private int[] heights;
	private LinearLayout[] optionsLayout;
	private TextView mShowTimeView;
	private ProgressBar mDecreaseProgressBar;
	private ArrayList<String> questions = new ArrayList<String>();
	private ArrayList<String> options = new ArrayList<String>();
	private ArrayList<Integer> answers = new ArrayList<Integer>();
	private ArrayList<Integer> dbID = new ArrayList<Integer>();
	private int qNumber = -1, pauseTimer;
	private CountDownTimer countDownTimer;
	private String[] alphaNumbering = null;
	private static Dialog dialog;
	private AnimationDrawable mAnimationDrawable;
	private Handler mHandler;
	private static String ALERT_EXIT_APP = "Do you wan't to leave the game here at Rs. ";
	public static int POSTDELAYED_TIME = 3000;
	private Integer[] mUsedImageID = { R.drawable.apused, R.drawable.ddused,
			R.drawable.ecused, R.drawable.aqused };
	private static HashMap<String, Integer> mImageIDMap = new HashMap<String, Integer>();
	private int rightAnswer;
	private boolean doubleDip = false;
	private int doubleDipCounter = 0;
	private static Random mRandom;
	private String[] scoreText, suboptions;
	private String scoreWon = "0";
	private Cursor cursor = null;
	private KbcMusicAsyncTask music = null;
	private Set<Integer> set = null;
	private boolean menuDisabled = false;
	private AdView adView = null;
	private static final String TAG = "com.sunny.kbc.questionactivity";
	private final String PUBLISHER_ID = "a151334171403af";
	private TexttoSpeechTask tts = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		CurrentActivity = this;
		flush();
		getData();
		adView = new AdView(this, AdSize.BANNER, PUBLISHER_ID);

		alphaNumbering = new String[] { "A. ", "B. ", "C. ", "D. " };
		if (qNumber <= 12)
			try {
				showQuestions();
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			startActivity(new Intent(this, KbcMainMenuActivity.class));
	}

	private void showQuestions() throws Exception {
		qNumber++;
		if (qNumber > 12)
			showResultView(scoreText[scoreText.length - 1]);
		else {
			int background = R.drawable.b2;
			Animation mSlide = AnimationUtils.loadAnimation(mContext,
					R.anim.push_left_in);
			setMainLayout(background, "image", mContext);

			getMainLayout().addView(getLifeLineLinearLayout());
			getMainLayout().addView(
					putGapHeight(mContext, ScreenDimension.getSingletonObject()
							.getDeviceHeight() * 10 / 100));
			getMainLayout().addView(getCountDownTimeLayout());
			getMainLayout().addView(getQuestionLinearLayout(qNumber));
			getMainLayout().addView(
					putGapHeight(mContext, ScreenDimension.getSingletonObject()
							.getDeviceHeight() * 2 / 100));
			getMainLayout().startAnimation(mSlide);

			optionsView = new TextView[4];
			widths = new int[4];
			heights = new int[4];
			optionsLayout = new LinearLayout[4];

			suboptions = options.get(qNumber).split("~");
			showAnimation();
			scoreText = KbcFixBarView.getMoneyBar();
			int i = 0;
			for (String option : suboptions) {
				rightAnswer = answers.get(qNumber);
				mainLayout.addView(getOptionsLinearLayout(i, option,
						answers.get(qNumber)));
				i++;
			}
			getMainLayout().addView(adView);
			adView.loadAd(new AdRequest());

			ScrollView scrollLayout = new ScrollView(mContext);
			scrollLayout.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			scrollLayout.setBackgroundResource(R.drawable.b2);
			scrollLayout.addView(mainLayout);

			setContentView(scrollLayout);
		}

	}

	private void showQuestions(boolean flip) throws Exception {
		int background = R.drawable.b2;
		setMainLayout(background, "image", mContext);

		int lastQuestion = questions.size() - 1;

		getMainLayout().addView(getLifeLineLinearLayout());
		getMainLayout().addView(
				putGapHeight(mContext, ScreenDimension.getSingletonObject()
						.getDeviceHeight() * 10 / 100));
		getMainLayout().addView(getCountDownTimeLayout());
		getMainLayout().addView(getQuestionLinearLayout(lastQuestion));
		getMainLayout().addView(
				putGapHeight(mContext, ScreenDimension.getSingletonObject()
						.getDeviceHeight() * 2 / 100));

		optionsView = new TextView[4];
		widths = new int[4];
		heights = new int[4];
		optionsLayout = new LinearLayout[4];

		suboptions = options.get(lastQuestion).split("~");
		showAnimation();
		scoreText = KbcFixBarView.getMoneyBar();
		int i = 0;
		for (String option : suboptions) {
			rightAnswer = answers.get(lastQuestion);
			mainLayout.addView(getOptionsLinearLayout(i, option,
					answers.get(lastQuestion)));
			i++;
		}
		getMainLayout().addView(adView);
		adView.loadAd(new AdRequest());

		ScrollView scrollLayout = new ScrollView(mContext);
		scrollLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		scrollLayout.setBackgroundResource(R.drawable.b2);
		scrollLayout.addView(mainLayout);

		setContentView(scrollLayout);
	}

	private void startTimer(int milliSecs, int interval) {
		countDownTimer = new CountDownTimer(milliSecs, interval) {
			@Override
			public void onTick(long millisUntilFinished) {
				try {
					int mTimer = (int) (millisUntilFinished / 1000);
					mShowTimeView.setText(String.valueOf(mTimer));
					mDecreaseProgressBar.setProgress(mTimer);
					pauseTimer = mTimer;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				try {
					mShowTimeView.setText("");
					mDecreaseProgressBar.setVisibility(View.INVISIBLE);
					// scoreWon = scoreText[qNumber - 1];
					stopAnimation();
					try {
						resetPreferences();
					} catch (Exception e) {
						e.printStackTrace();
					}
					for (int i = 0; i < 4; i++) {
						if (rightAnswer == optionsView[i].getId()) {
							optionsView[i]
									.setBackgroundResource(R.drawable.optiongreen);
							break;
						}
					}
					mHandler = new Handler();
					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							showResultView();
						}
					}, POSTDELAYED_TIME);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void stopTimer() {
		if (countDownTimer != null)
			countDownTimer.cancel();
	}

	public void resumeTimer(int pauseTimer) throws Exception {
		startTimer(pauseTimer * 1000, 1000);
	}

	private LinearLayout getCountDownTimeLayout() {
		try {
			mCountDownTimeLayout = new LinearLayout(mContext);
			mCountDownTimeLayout.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			mCountDownTimeLayout.setOrientation(LinearLayout.VERTICAL);
			mCountDownTimeLayout.setGravity(Gravity.CENTER_HORIZONTAL);

			mShowTimeView = new TextView(mContext);
			mShowTimeView.setGravity(Gravity.CENTER_VERTICAL);
			mShowTimeView.setTextColor(Color.DKGRAY);
			mShowTimeView.setTextSize(30f);
			mShowTimeView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);

			LinearLayout mLinearLayout = new LinearLayout(mContext);
			mLinearLayout.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			mLinearLayout.setGravity(Gravity.CENTER);
			mLinearLayout.addView(mShowTimeView);
			mCountDownTimeLayout.addView(mLinearLayout);
			int raiseBar = KbcSplashScreenActivity.mSharedPreferences.getInt(
					KbcConstants.RAISE_BAR, 0);
			if (qNumber <= raiseBar)
				mCountDownTimeLayout.addView(getProgressBar());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mCountDownTimeLayout;
	}

	private LinearLayout getQuestionLinearLayout(int questionIndex) {
		try {
			mQuestionLayout = new LinearLayout(mContext);
			mQuestionLayout.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			questionTextView = new TextView(mContext);
			questionTextView.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			questionTextView.setBackgroundResource(R.drawable.q1);
			questionTextView.getBackground().setAlpha(127);
			questionTextView.setText(questions.get(questionIndex));
			questionTextView.setTextColor(Color.WHITE);
			questionTextView.setTextSize(13f);
			questionTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
			questionTextView.setGravity(Gravity.CENTER);
			mQuestionLayout.addView(questionTextView);
			destroyTTS();
			if (KbcSplashScreenActivity.mSharedPreferences.getBoolean(
					KbcConstants.SOUND_SETTINGS, true)) {
				// if(getLanguage().equalsIgnoreCase("english"))
				narrate(questionIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mQuestionLayout;
	}

	private void narrate(int number) {
		tts = new TexttoSpeechTask(this, mContext, questions.get(number),
				options.get(number));
		tts.execute();
	}

	private void flush() {
		try {
			if (questions != null && options != null && answers != null
					&& dbID != null) {
				questions.clear();
				options.clear();
				answers.clear();
				dbID.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Gallery getLifeLineLinearLayout() throws Exception {
		lifeLineLayout = new LinearLayout(mContext);
		lifeLineLayout.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		lifeLineLayout.setOrientation(LinearLayout.HORIZONTAL);
		lifeLineLayout.setBackgroundResource(Color.TRANSPARENT);
		lifeLineLayout.setGravity(Gravity.CENTER_VERTICAL);

		Gallery mGallery = new Gallery(mContext);
		mGallery.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		final KbcImageAdapter kbcImageAdapter = new KbcImageAdapter(mContext);
		mGallery.setAdapter(kbcImageAdapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {
				mImageIDMap.put(String.valueOf(position),
						mUsedImageID[position]);
				music = new KbcMusicAsyncTask(mContext);
				music.execute(R.raw.beep);
				kbcImageAdapter.notifyDataSetChanged();
				if (position == 0) {
					if (!KbcSplashScreenActivity.mSharedPreferences.getBoolean(
							KbcConstants.AUDIENCE_POLL, false)) {

						KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(
								KbcConstants.AUDIENCE_POLL, true);
						KbcSplashScreenActivity.mKbcPrefEditor.commit();
						stopTimer();
						showDialog(view, rightAnswer);
					}
				} else if (position == 1) {
					if (!KbcSplashScreenActivity.mSharedPreferences.getBoolean(
							KbcConstants.DOUBLE_DIP, false)) {
						KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(
								KbcConstants.DOUBLE_DIP, true);
						KbcSplashScreenActivity.mKbcPrefEditor.commit();
						setDoubleDip(true);
					}
				} else if (position == 2) {
					if (!KbcSplashScreenActivity.mSharedPreferences.getBoolean(
							KbcConstants.EXPERT_ADVICE, false)) {
						KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(
								KbcConstants.EXPERT_ADVICE, true);
						KbcSplashScreenActivity.mKbcPrefEditor.commit();
						stopTimer();
						new KbcExpertAdviceView(CurrentActivity,
								alphaNumbering[rightAnswer], pauseTimer);
					}
				}

				else if (position == 3) {
					if (!KbcSplashScreenActivity.mSharedPreferences.getBoolean(
							KbcConstants.ALTER_QUESTION, false)) {

						KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(
								KbcConstants.ALTER_QUESTION, true);
						KbcSplashScreenActivity.mKbcPrefEditor.commit();
						stopTimer();
						try {
							getMainLayout().removeAllViewsInLayout();
							showQuestions(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

		});

		BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(
				R.drawable.audiencepoll);
		int width = bd.getBitmap().getWidth();

		MarginLayoutParams mlp = (MarginLayoutParams) mGallery
				.getLayoutParams();
		mlp.setMargins(
				-(ScreenDimension.getSingletonObject().getDeviceWidth() / 2 + width),
				mlp.topMargin, mlp.rightMargin, mlp.bottomMargin);

		return mGallery;
	}

	private LinearLayout getOptionsLinearLayout(final int i,
			final String subOptions, final int correctAnswer) throws Exception {

		optionsLayout[i] = new LinearLayout(mContext);
		optionsLayout[i].setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		optionsLayout[i].setOrientation(LinearLayout.HORIZONTAL);
		optionsLayout[i]
				.setPadding(
						ScreenDimension.getSingletonObject().getDeviceWidth() * 1 / 100,
						ScreenDimension.getSingletonObject().getDeviceHeight() * 2 / 100,
						ScreenDimension.getSingletonObject().getDeviceWidth() * 1 / 100,
						0);

		final int j = i;
		optionsView[i] = new TextView(mContext) {
			@Override
			protected void onLayout(boolean changed, int left, int top,
					int right, int bottom) {
				super.onLayout(changed, left, top, right, bottom);
				widths[j] = getMeasuredWidth();
				heights[j] = getMeasuredHeight();
			}
		};

		optionsView[i].setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, ScreenDimension.getSingletonObject()
						.getDeviceHeight() * 7 / 100));
		optionsView[i].setBackgroundResource(R.drawable.optionblue);
		optionsView[i].setText(alphaNumbering[i] + " " + subOptions);
		optionsView[i].setTextColor(Color.BLACK);
		optionsView[i].setPadding(ScreenDimension.getSingletonObject()
				.getDeviceWidth() * 4 / 100, ScreenDimension
				.getSingletonObject().getDeviceHeight() * 1 / 100, 0, 0);
		optionsView[i].setId(i);
		optionsView[i].setTextSize(13f);
		optionsView[i].setTypeface(Typeface.SANS_SERIF);
		optionsView[i].setGravity(Gravity.CENTER_HORIZONTAL);
		//optionsView[i].getBackground().setAlpha(100);
		optionsLayout[i].addView(optionsView[i]);

		optionsView[i].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				destroyTTS();
				music = new KbcMusicAsyncTask(mContext);
				music.execute(R.raw.optionlock);
				if (isDoubleDip())
					setMultipleOptionClickable(true);
				else
					setMultipleOptionClickable(false);

				v.setBackgroundDrawable(mAnimationDrawable);
				((TextView) v).setWidth(widths[j]);
				((TextView) v).setHeight(heights[j]);
				v.post(mAnimationDrawable);

				stopTimer();
				removeTimer();

				Runnable defaultOptionColor = new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						stopAnimation();
						v.setBackgroundResource(R.drawable.optionorange);
					}
				};

				delay(defaultOptionColor, POSTDELAYED_TIME);

				mHandler = new Handler();
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						if (isOptionCorrect(i, correctAnswer)) {
							v.setBackgroundResource(R.drawable.optiongreen);
							music = new KbcMusicAsyncTask(mContext);
							music.execute(R.raw.rightanswer);
							setDoubleDip(false);
							amountWon(scoreText[qNumber]);
						} else {
							v.setBackgroundResource(R.drawable.optionred);
							int optionLength = optionsView.length;
							if (isDoubleDip()) {
								if (correctAnswer == v.getId()) {
									v.setBackgroundResource(R.drawable.optiongreen);
									music = new KbcMusicAsyncTask(mContext);
									music.execute(R.raw.rightanswer);
									setDoubleDip(false);
								} else
									doubleDipCounter++;
							}

							if (isDoubleDipCorrect(doubleDipCounter)) {
								showCorrectAnswer(optionLength, correctAnswer);
								endGameShow();
							}

							else if (!isDoubleDip()) {
								showCorrectAnswer(optionLength, correctAnswer);
								endGameShow();
							}
						}
					}
				}, POSTDELAYED_TIME + 700);

				if (isOptionCorrect(i, correctAnswer)) {
					Runnable correctAnswer = new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							getMainLayout().removeAllViewsInLayout();
							try {
								showQuestions();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					delay(correctAnswer, POSTDELAYED_TIME + 5000);
				}
			}
		});

		return optionsLayout[i];
	}

	private void showCorrectAnswer(int optionLength, int correctAnswer) {
		for (int i = 0; i < optionLength; i++) {
			if (correctAnswer == optionsView[i].getId()) {
				optionsView[i].setBackgroundResource(R.drawable.optiongreen);
				break;
			}
		}
	}

	private void getData() {

		int hardRecords = checkDB(KbcConstants.DB_RES_TABLE);
		restoreFactory(hardRecords, KbcConstants.DB_RES_TABLE, 520);

		if (hardRecords > 0) {
			set = new TreeSet<Integer>();
			set.addAll(randomGenerator(1, 380, 4));
			set.addAll(randomGenerator(381, 1000, 10));
			fillBuffer(KbcConstants.DB_RES_TABLE, KbcConstants.DB_QUESTIONS,
					KbcConstants.DB_OPTIONS);
		}
	}

	private int checkDB(final String tableName) {

		int defaultValue = 540;
		if (KbcDBCategory.mDB == null)
			try {
				new KbcDBCategory(mContext);
			} catch (Exception e1) {
				Log.e(TAG, e1.toString());
			}

		int records;
		try {
			cursor = KbcDBCategory.mDB.query(tableName, new String[] {
					KbcConstants.KEY_ROWID, KbcConstants.DB_QUESTIONS,
					KbcConstants.DB_OPTIONS, KbcConstants.DB_ANSWERS }, null,
					null, null, null, null);
			records = cursor.getCount();
			cursor.close();
			return records;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			return defaultValue;
		}
	}

	private void restoreFactory(int records, final String tableName, int limit) {
		if (records > limit) {
			try {
				ContentValues contentValues = new ContentValues();
				contentValues.put(KbcConstants.OCCURANCE, "false");
				KbcDBCategory.mDB.update(tableName, contentValues,
						KbcConstants.OCCURANCE + " = " + '"' + "true" + '"',
						null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void fillBuffer(final String tableName, final String questionAttr,
			final String optionAttr) {
		try {
			Iterator<Integer> itr = set.iterator();
			while (itr.hasNext()) {
				cursor = KbcDBCategory.mDB.query(tableName, new String[] {
						KbcConstants.KEY_ROWID, questionAttr, optionAttr,
						KbcConstants.DB_ANSWERS }, KbcConstants.KEY_ROWID
						+ " = " + itr.next() + " and " + KbcConstants.OCCURANCE
						+ " = " + "'" + "false" + "'", null, null, null, null);
				cursor.moveToFirst();
				do {
					dbID.add(cursor.getInt(cursor
							.getColumnIndex(KbcConstants.KEY_ROWID)));
					questions.add(cursor.getString(cursor
							.getColumnIndex(questionAttr)));
					options.add(cursor.getString(cursor
							.getColumnIndex(optionAttr)));
					answers.add(Integer.parseInt(cursor.getString(cursor
							.getColumnIndex(KbcConstants.DB_ANSWERS))));
				} while (cursor.moveToNext());
				cursor.close();
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "fill buffer " + e.toString());
		}
	}

	private boolean isDoubleDipCorrect(int chance) {
		if (chance != 2)
			return false;
		else
			return true;
	}

	private ProgressBar getProgressBar() throws Exception {
		mDecreaseProgressBar = new ProgressBar(mContext, null,
				android.R.attr.progressBarStyleHorizontal);
		mDecreaseProgressBar.setLayoutParams(new LayoutParams(ScreenDimension
				.getSingletonObject().getDeviceWidth() * 80 / 100,
				LayoutParams.WRAP_CONTENT));

		if (countDownTimer != null)
			stopTimer();
		startTimer(60000, 1000);
		return mDecreaseProgressBar;
	}

	public void showAnimation() {
		mAnimationDrawable = new AnimationDrawable();
		mAnimationDrawable.addFrame(
				getResources().getDrawable(R.drawable.optiongreen), 300);
		mAnimationDrawable.addFrame(
				getResources().getDrawable(R.drawable.optionorange), 300);
		mAnimationDrawable.setOneShot(false);
	}

	public void stopAnimation() {
		if (mAnimationDrawable != null)
			mAnimationDrawable.stop();
	}

	public static LinearLayout setMainLayout(int image, String tag,
			Context context) {
		mainLayout = new LinearLayout(context);
		mainLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		if (tag == "image")
			mainLayout.setBackgroundResource(image);
		else
			mainLayout.setBackgroundColor(image);

		return mainLayout;
	}

	public static LinearLayout getMainLayout() throws NullPointerException {
		return mainLayout;
	}

	private Boolean isOptionCorrect(int chosenOption, int correctAnswer) {
		boolean answer = false;
		if (chosenOption == correctAnswer) {
			answer = true;
			ContentValues contentValues = new ContentValues();
			contentValues.put(KbcConstants.OCCURANCE, "true");
			KbcDBCategory.mDB.update(KbcConstants.DB_RES_TABLE, contentValues,
					KbcConstants.KEY_ROWID + " = " + dbID.get(qNumber), null);
		} else
			answer = false;

		if (qNumber != 0) {
			try {
				scoreWon = scoreText[qNumber - 1];
			} catch (Exception e) {
				scoreWon = "0";
			}
		} else {
			scoreWon = "0";
		}

		return answer;
	}

	public static View putGapHeight(Context context, int height) {
		gapWidthView = new TextView(context);
		gapWidthView.setHeight(height);
		return gapWidthView;
	}

	public static View putGapWidth(Context context, int width) {
		gapHeightView = new TextView(context);
		gapHeightView.setWidth(width);
		return gapHeightView;
	}

	public static HashMap<String, Integer> getImages() {
		return mImageIDMap;
	}

	public static void insertImages() throws Exception {
		mImageIDMap.clear();
		mImageIDMap.put("0", R.drawable.audiencepoll);
		mImageIDMap.put("1", R.drawable.doubledip);
		mImageIDMap.put("2", R.drawable.expertchoice);
		mImageIDMap.put("3", R.drawable.alterquestion);
	}

	public void showDialog(View view, int rightAnswer) {
		float[] values = new float[4];
		int[] values2 = new int[4];
		TreeSet<Integer> values1 = getRandomNumber();
		int total = 0;

		Iterator<Integer> itr = values1.iterator();
		int s = 0;
		while (itr.hasNext()) {
			values2[s] = Integer.parseInt(itr.next().toString());
			values[s] = (float) values2[s];
			total += values[s];
			s++;
		}

		for (int i = 0; i < values.length; i++) {
			if (values[i] > values[rightAnswer]) {
				float k = values[rightAnswer];
				values[rightAnswer] = values[i];
				values[i] = k;
			}
		}

		// getPercentageValue(values);

		String[] verlabels = new String[] { "", "", "", "", "", "", "", "", "" };
		String[] horlabels = new String[] { "A", "B", "C", "D" };
		dialog = new Dialog(CurrentActivity, R.style.CustomTheme);
		LinearLayout mLinearLayout = new LinearLayout(mContext);
		mLinearLayout.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mLinearLayout.setBackgroundResource(R.drawable.b2);
		mLinearLayout
				.addView(new KbcGraphView(mContext, values, "", horlabels,
						verlabels, getPercentageValue(values, total),
						KbcGraphView.BAR));
		mLinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismissDialog();
				try {
					resumeTimer(pauseTimer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		dialog.setContentView(mLinearLayout);
		dialog.show();
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					stopTimer();
					try {
						resumeTimer(pauseTimer);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			}
		});
	}

	private Handler delay(Runnable runnableObj, long milliSeconds) {
		mHandler = new Handler();
		mHandler.postDelayed(runnableObj, milliSeconds);

		return mHandler;
	}

	public static void dismissDialog() {
		if (dialog != null)
			dialog.dismiss();
	}

	public void setDoubleDip(boolean status) {
		doubleDip = status;
	}

	public boolean isDoubleDip() {
		return doubleDip;
	}

	public void setMultipleOptionClickable(boolean status) {
		if (optionsView != null) {
			for (int i = 0; i < optionsView.length; i++)
				optionsView[i].setClickable(status);
		}
	}

	private void endGameShow() {
		menuDisabled = true;
		/*music = new KbcMusicAsyncTask(mContext);
		music.execute(R.raw.wrong);*/
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				try {
					resetPreferences();
				} catch (Exception e) {
					e.printStackTrace();
				}
				showResultView();
			}
		}, POSTDELAYED_TIME + 3000);
	}

	private void showResultView() {
		try {
			final String finalScore = showFinalScore();
			menuDisabled = true;
			if (mainLayout != null) {
				mainLayout.removeAllViewsInLayout();
				mainLayout.addView(new KbcEndGameResultView(mContext,
						finalScore));
			}

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					KbcMainMenuActivity.removeAllView(getMainLayout());
					try {
						loadInsertScore(finalScore);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, POSTDELAYED_TIME + 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showResultView(final String score) {
		mainLayout.removeAllViewsInLayout();
		mainLayout.addView(new KbcEndGameResultView(mContext, score));

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				KbcMainMenuActivity.removeAllView(getMainLayout());
				try {
					loadInsertScore(score);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, POSTDELAYED_TIME + 1500);
	}

	private void resetPreferences() throws Exception {
		KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(
				KbcConstants.AUDIENCE_POLL, false);
		KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(
				KbcConstants.EXPERT_ADVICE, false);
		KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(
				KbcConstants.DOUBLE_DIP, false);
		KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(
				KbcConstants.ALTER_QUESTION, false);
		KbcSplashScreenActivity.mKbcPrefEditor.putInt(
				KbcConstants.RAISE_BAR, -1);		
		KbcSplashScreenActivity.mKbcPrefEditor.commit();
	}

	private void alertExitApp(String alertMessage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String score = null;
		if (qNumber != 0)
			score = scoreText[qNumber - 1];
		else
			score = "0";
		final String finalscore = score;
		builder.setMessage(alertMessage + finalscore)
				.setIcon(R.drawable.icon)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								showResultView(finalscore);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private static TreeSet<Integer> getRandomNumber() {
		mRandom = new Random();
		TreeSet<Integer> generated = new TreeSet<Integer>();
		while (generated.size() != 4)
			generated.add(mRandom.nextInt(7) + 1);

		return generated;
	}

	private static String[] getPercentageValue(float[] value, int total) {
		String[] percentage = new String[value.length];
		float length = value.length;
		Log.i("LENGTH ", String.valueOf(length));
		for (int j = 0; j < length; j++) {
			Log.i("percentage ", String.valueOf((value[j]) / total));
			percentage[j] = String.valueOf((int) ((value[j] / total) * 100));
		}
		return percentage;
	}

	private void removeTimer() {
		mDecreaseProgressBar.setVisibility(View.INVISIBLE);
		mShowTimeView.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!menuDisabled) {
			destroyTTS();
			menu.clear();
			menu.add("MileStone").setIcon(R.drawable.icon);
			menu.add("SoundOff").setIcon(R.drawable.soundoff);
			menu.add("Quit").setIcon(R.drawable.menu_exit);
		}
		return true;
	}

	private void loadInsertScore(String finalScore) throws Exception {
		this.setContentView(new KbcInsertScoreView(mContext, finalScore,
				KbcQuestionActivity.this));
	}

	private Set<Integer> randomGenerator(int start, int end, int limit) {
		Random random = new Random();
		Set<Integer> list = new TreeSet<Integer>();
		int n = 0;
		while (list.size() < limit) {
			n = showRandomInteger(start, end, random);
			list.add(n);
		}

		return list;
	}

	private int showRandomInteger(int start, int end, Random random) {
		int randomNumber = start + Math.abs(random.nextInt()) % (end - start);
		return randomNumber;
	}

	private void reset() throws Exception {
		qNumber = -1;
	}

	private void destroyTTS() {
		if (tts != null)
			tts.onDestroy();
	}

	private String showFinalScore() {
		try {
			int state = KbcSplashScreenActivity.mSharedPreferences.getInt(
					KbcConstants.RAISE_BAR, 0);
			Log.v("THE MONEY BAR HAS BEEN SET TO", String.valueOf(state));
			if (qNumber > state)
				scoreWon = scoreText[state];
			else
				scoreWon = "0";
		} catch (Exception e) {
			scoreWon = "0";
		}

		return scoreWon;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getTitle() == "Quit") {
			if (!isDoubleDip())
				alertExitApp(ALERT_EXIT_APP);

		} else if (item.getTitle() == "MileStone") {
			Dialog dialog = new Dialog(CurrentActivity);
			try {
				dialog.setContentView(new KbcFixBarView(mContext, null,
						"showProgressLevel", qNumber));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dialog.show();
		} else if (item.getTitle() == "SoundOff") {
			if (KbcSplashScreenActivity.mSharedPreferences.getBoolean(
					KbcConstants.SOUND_SETTINGS, true)) {
				KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(
						KbcConstants.SOUND_SETTINGS, false);
				KbcSplashScreenActivity.mKbcPrefEditor.commit();
				item.setIcon(R.drawable.sound);

			} else {
				KbcSplashScreenActivity.mKbcPrefEditor.putBoolean(
						KbcConstants.SOUND_SETTINGS, true);
				KbcSplashScreenActivity.mKbcPrefEditor.commit();
				item.setIcon(R.drawable.soundoff);
			}

		}
		return true;
	}

	public void amountWon(final String score) {

		Runnable showAmountWon = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mainLayout.removeAllViewsInLayout();
				mainLayout.addView(new KbcEndGameResultView(mContext, score));
			}
		};

		delay(showAmountWon, 2000);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			destroyTTS();
			if (!isDoubleDip()) {
				alertExitApp(ALERT_EXIT_APP);
				return true;
			} else
				return false;
		} else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop() {
		super.onStop();
		menuDisabled = false;
		destroyTTS();		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			destroyTTS();
			if (adView != null)
				adView.destroy();
			reset();
			resetPreferences();			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
