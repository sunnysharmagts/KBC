package com.mysterio.bm;

public interface KbcConstants {
	public static final String FEEDBACK_STATEMENT = "Any ideas or suggestions are welcomed.\nPlease send me your feedback/suggestions at";
	public static final String MAIL_ID = "mysterio.devel@gmail.com";
	public static final String SUBJECT = "BM feedback";
	
    public static final String APP_TITLE = "Be A Millionare";
    public static final String APP_PNAME = "com.mysterio.bm";
    
    public static final int DAYS_UNTIL_PROMPT = 3;
    public static final int LAUNCHES_UNTIL_PROMPT = 7;
    
	public static final String FACEBOOK_APPID = "245369118874135";
	public static final String[] FACEBOOK_PERMISSION = { "publish_stream" };
	public static final String FACEBOOK_ACCESS_TOKEN = "access_token";
    
	public static final String DB_NAME = "KBC_5";
    public static final int DB_VERSION = 1;  // For version 1.0
    public static final String KEY_ROWID = "_id";
    public static final String DB_TABLE = "KBC_SCORE";
    public static final String PLAYER_NAME = "name"; 
    public static final String SCORE = "score";
    
    public static final String DB_RES_TABLE = "KBC_QUESTIONS";
    public static final String OCCURANCE = "attempt";
    public static final String DB_QUESTIONS = "questions";
    public static final String DB_OPTIONS = "options";
    public static final String DB_ANSWERS = "answers";
    
    /****************************************************
     * 
     * SHARED PREFERENCE KEY VALUES
     * 
     ****************************************************/
    
    public static final String PREFERENCE_NAME = "KBC";
    public static final String AUDIENCE_POLL = "audiencepoll";
    public static final String ALTER_QUESTION = "nextquestion";
    public static final String EXPERT_ADVICE = "expertadvice";
    public static final String DOUBLE_DIP = "doubledip";
    public static final String RAISE_BAR = "KBC Choose bar";
    
    public static final String QUESTION_INFO = "13 Questions, 4 possible answers, 4 LifeLines and top Prize of Rs. 5,000,0000 ";
    public static final String FACEBOOK = "Get to see the hightest scores, update it on your wall and challenge others";
    public static final String SOUND_SETTINGS = "sound";
    public static final String TTS_SETTINGS = "tts";
    
    public static final String INFORMATION = "Hey Guys. This is a general information for KBC5 Users. \n\nIf this game crashes too often, even after downloading updates, then please uninstall the game and install it once again. \n\n This is mainly happening due to the loopholes which occured in initial phase of the application hence creating problem even in the later version. \n\n Thank you. \nHope you enjoy the game \n\n THOSE YOU HAVE READ THIS MESSAGE, PLEASE IGNORE IT.";
    
}
