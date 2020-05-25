package com.example.mogratatakigame;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private boolean updateHighScore,mogAttack,moveOKMog;
    private boolean ngMog,lastTimeMog,endGame,stopMog,mogContinue,firstGame,endOKMog,startOKMog,tutorialOn;
    private boolean birdTap,flyingBird,swipeHum,gameRetry,startGame,resulting,endingNow,onceEnd;
    private boolean[] recordHiyoko = {false, false, false, false, false, false, false, false, false};
    private boolean[] spawnHum     = {false, false, false, false, false, false, false, false, false};
    private String bou = "| ";
    private double startSystemTime, nowSystemTime,countdownTime,nowGameTime,setTime,swipeStartTime,swipeEndTime;
    private float upPointMog,centerPointMog,underPointMog,left1PointMog,left2PointMog,fromXBird,toXBird,screenDensity;
    private float startTouchX,startTouchY;
    private int intScore, timeCount, sharedHighScore,recordScore,hiyokoCount,playCount,
            upMograNum,nowMograNum,upHiyokoNum,nowHiyokoNum,mograHeight,mograCountNum,clearMog,
            mograCountint,birdTopPoint,nowStage,tapCount,nextClearScore;
    private int[] upTime        = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] beforeUpTime  = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] clearScore    = {250,230,270,300};
//    private int[] gameMograTime,gameHiyokoTime;
    private TextView scoreTxt,nokoriTime, highScoreTxt,sumMogra,sumHiyoko,hiyokoCountint
            ,highScoreTime,endMsg,endTxt,endScore,updateHighScoreTxt,nowStageTxt,nowStageInt
            ,nowCondiNum,condiNum,clearTxt,discript,discript2,tutorialText,tutorialText2
            ,tutorialCheckText,stageTxt,stageInt,touchPleaseTxt,clearText;
    private ImageView happyIcon,flyBird,discriptionImage,discriptionImage2,dotImg,dotImg2;
    private Handler timerHandler;
    private Handler[] DnHd,humHd;
    private RelativeLayout resultLayout,animLayout;
    private TranslateAnimation birdMove,textMove;
    private Timer gameTimer;
    private DecimalFormat timeFormat;
    private Calendar highScoreDate;
    private SimpleDateFormat highScoreDateFormat;
    private SharedPreferences gameData;
    private Button startMogGameBtn,stopGameBtn,resetScoreBtn;
    private Button backTopBtn,nextStageBtn;
    private MogUpDnCanvas[] mogUpDnCanvas;
    private ShowMsgCanvas[] showMsgCanvas;
    private MogNumAdmin mogNumAdmin;
    private HummerCanvas[] hummerCanvas;
    private Map<String, Integer> drawableMap;
    private PointTestCanvas pointTestCanvas;
    private TouchCanvas touchCanvas;
    private CheckBox checkBox;
    private AnimatorSet gameEndAnim,endAlphaAnim;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //縦画面固定(横の場合ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        int bRed,bGreen,bBlue;
        bRed   = 173;
        bGreen = 179;
        bBlue  = 255;
        int backgroundColor = Color.rgb(bRed, bGreen, bBlue);
        birdTopPoint = 200;

        //部品初期化
        final RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setBackgroundColor(backgroundColor);

        ImageView companyLogo = new ImageView(this);
        companyLogo.setImageResource(R.drawable.shakun);
        companyLogo.setAlpha(0.1f);
        RelativeLayout.LayoutParams logoParam = new RelativeLayout.LayoutParams(500, 500);
        logoParam.addRule(RelativeLayout.CENTER_IN_PARENT, 13);
        //メモ：左下に設置するとき
        //logoParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 13);
        //logoParam.setMargins(0, 1400,0,0);
        companyLogo.setLayoutParams(logoParam);

        drawableMap = new HashMap<>();
        drawableMap.put("mogra",    R.drawable.moguratataki);
        drawableMap.put("hiyoko",   R.drawable.hiyoko_mog);
        drawableMap.put("tenreg",   R.drawable.tenreg);
        drawableMap.put("lemming",  R.drawable.lemming);
        drawableMap.put("deathMan", R.drawable.shinigami);
        drawableMap.put("bird",     R.drawable.bluebird);

        //ImageView初期化
        RelativeLayout.LayoutParams flyBirdParam = new RelativeLayout.LayoutParams(200,200);//flyBird.getWidth() / 10,flyBird.getHeight() / 10);
        flyBirdParam.setMargins(0, birdTopPoint,0,0);
        flyBird = new ImageView(this);
        flyBird.setImageResource(R.drawable.bluebird);
        flyBird.setLayoutParams(flyBirdParam);
        flyBird.setAlpha(0.0f);

        mogNumAdmin         = new MogNumAdmin();

        DisplayMetrics mogDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mogDisplayMetrics);
        final int windowHeight    = mogDisplayMetrics.heightPixels;
        int windowWidth     = mogDisplayMetrics.widthPixels;

        timerHandler    = new Handler();

        pointTestCanvas = new PointTestCanvas(this);
        touchCanvas     = new TouchCanvas(this);

        DnHd            = new Handler[]      {  new Handler(), new Handler(), new Handler(),
                                                new Handler(), new Handler(), new Handler(),
                                                new Handler(), new Handler(), new Handler() };
        humHd           = new Handler[]      {  new Handler(), new Handler(), new Handler(),
                                                new Handler(), new Handler(), new Handler(),
                                                new Handler(), new Handler(), new Handler() };
        showMsgCanvas   = new ShowMsgCanvas[]{  new ShowMsgCanvas(this),
                                                new ShowMsgCanvas(this),
                                                new ShowMsgCanvas(this),
                                                new ShowMsgCanvas(this),
                                                new ShowMsgCanvas(this),
                                                new ShowMsgCanvas(this),
                                                new ShowMsgCanvas(this),
                                                new ShowMsgCanvas(this),
                                                new ShowMsgCanvas(this),
                                                new ShowMsgCanvas(this) };
        hummerCanvas    = new HummerCanvas[] {  new HummerCanvas(this,mogNumAdmin,showMsgCanvas[0]),
                                                new HummerCanvas(this,mogNumAdmin,showMsgCanvas[1]),
                                                new HummerCanvas(this,mogNumAdmin,showMsgCanvas[2]),
                                                new HummerCanvas(this,mogNumAdmin,showMsgCanvas[3]),
                                                new HummerCanvas(this,mogNumAdmin,showMsgCanvas[4]),
                                                new HummerCanvas(this,mogNumAdmin,showMsgCanvas[5]),
                                                new HummerCanvas(this,mogNumAdmin,showMsgCanvas[6]),
                                                new HummerCanvas(this,mogNumAdmin,showMsgCanvas[7]),
                                                new HummerCanvas(this,mogNumAdmin,showMsgCanvas[8]) };
        mogUpDnCanvas   = new MogUpDnCanvas[]{  new MogUpDnCanvas(this,showMsgCanvas[0],mogNumAdmin,hummerCanvas[0]),
                                                new MogUpDnCanvas(this,showMsgCanvas[1],mogNumAdmin,hummerCanvas[1]),
                                                new MogUpDnCanvas(this,showMsgCanvas[2],mogNumAdmin,hummerCanvas[2]),
                                                new MogUpDnCanvas(this,showMsgCanvas[3],mogNumAdmin,hummerCanvas[3]),
                                                new MogUpDnCanvas(this,showMsgCanvas[4],mogNumAdmin,hummerCanvas[4]),
                                                new MogUpDnCanvas(this,showMsgCanvas[5],mogNumAdmin,hummerCanvas[5]),
                                                new MogUpDnCanvas(this,showMsgCanvas[6],mogNumAdmin,hummerCanvas[6]),
                                                new MogUpDnCanvas(this,showMsgCanvas[7],mogNumAdmin,hummerCanvas[7]),
                                                new MogUpDnCanvas(this,showMsgCanvas[8],mogNumAdmin,hummerCanvas[8]) };
        Log.d("onCreate","Length\nDnHd:" + DnHd.length + "\nShowMsgCanvas:" + showMsgCanvas.length + "\nHummerCanvas:" + hummerCanvas.length + "\nMogUpDnCanvas:" + mogUpDnCanvas.length);
        mograCountNum       = mogUpDnCanvas.length;

        clearMog            = 250;

        setTime             = 32.0;
        countdownTime       = setTime;
        timeFormat          = new DecimalFormat("0.0");

        swipeStartTime      = 0;
        swipeEndTime        = 0;

        DisplayMetrics dm   = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenDensity = getApplicationContext().getResources().getDisplayMetrics().density;
        mogNumAdmin.setDensity(screenDensity);

        highScoreDate       = Calendar.getInstance();
        highScoreDateFormat = new SimpleDateFormat("[ yyyy/MM/dd ]");

        fromXBird   = windowWidth;
        toXBird     = 0;
        birdMove    = new TranslateAnimation(Animation.ABSOLUTE, fromXBird,Animation.ABSOLUTE, toXBird,Animation.ABSOLUTE, birdTopPoint,Animation.ABSOLUTE, birdTopPoint);
        birdMove.setDuration(1500);
        birdMove.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(birdTap){
                    flyBird.setImageResource(R.drawable.tegami_bird);
                    birdTap = false;
                }
                AlphaAnimation alphaBird = new AlphaAnimation(1.0f,0.0f);
                alphaBird.setDuration(1000);
                alphaBird.setFillEnabled(true);
                alphaBird.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        flyBird.setAlpha(0.0f);
                        flyingBird = false;
                        flyBird.setImageResource(R.drawable.bluebird);
                        flyBird.setAlpha(0.0f);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                flyBird.startAnimation(alphaBird);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

//        gameMograTime       = new int[(int)setTime / 10];
//        gameHiyokoTime      = new int[(int)setTime / 10];
//        upMograNum          = 50;
//        upHiyokoNum         = 15;

        playCount       = 0;
        intScore        = 0;
        sharedHighScore = 0;
        hiyokoCount     = 0;
//        nowMograNum     = 0;
//        nowHiyokoNum    = 0;
        mograHeight     = 0;

        upPointMog      = 550.0f + 100.0f;
        centerPointMog  = 880.0f;// + 100.0f;
        underPointMog   = 1400.0f;// + 100.0f;
        left1PointMog   = 350.0f;
        left2PointMog   = 650.0f + 30.0f;

        startTouchX     = 0;
        startTouchY     = 0;

        birdTopPoint    = 350;

        nowStage        = 0;
        tapCount        = 0;

        //startGame       = false;
        endGame         = true;
        moveOKMog       = false;
        mogAttack       = false;
        ngMog           = false;
        lastTimeMog     = false;
        updateHighScore = false;
        stopMog         = false;
        mogContinue     = false;
        flyingBird      = false;
        birdTap         = false;
        firstGame       = true;
        endOKMog        = false;
        startOKMog      = true;
        tutorialOn      = true;
        startGame       = false;
        gameRetry       = false;
        resulting       = false;
        endingNow       = false;
        onceEnd         = true;

        //
        // スコアパーツ部分
        //
        happyIcon       = new ImageView(this);
        happyIcon.setImageResource(R.drawable.mark_syuku);

        TextView scoreTxtV,hiScoreTxtV,nokoriTxtV,slash,hiyokoSlash,hiyokoCountTxt,minus,condiSlash;
        scoreTxtV       = new TextView(this);
        scoreTxt        = new TextView(this);
        sumMogra        = new TextView(this);
        slash           = new TextView(this);
        minus           = new TextView(this);
        hiyokoCountTxt  = new TextView(this);
        hiyokoCountint  = new TextView(this);
        //hiyokoSlash     = new TextView(this);
        //sumHiyoko       = new TextView(this);
        hiScoreTxtV     = new TextView(this);
        highScoreTxt    = new TextView(this);
        highScoreTime   = new TextView(this);
        nokoriTxtV      = new TextView(this);
        nokoriTime      = new TextView(this);
        nowStageTxt     = new TextView(this);
        nowStageInt     = new TextView(this);
        nowCondiNum     = new TextView(this);
        condiSlash      = new TextView(this);
        condiNum        = new TextView(this);
        clearTxt        = new TextView(this);

        scoreTxtV.setText("Score:");
        slash.setText("/");
        minus.setText("-");
        hiyokoCountTxt.setText("Miss:");
        //hiyokoSlash.setText("/");
        //sumHiyoko.setText("0");
        hiScoreTxtV.setText("HighScore:");
        highScoreTxt.setText(String.format("%03d", 0));
        highScoreTime.setText(String.format("%03d", 0));
        //Log.d("CREATE_TAG","\nFormat:" + highScoreDateFormat.format(highScoreDate.getTime()));
        nokoriTxtV.setText("Time:");
        nowStageTxt.setText("ステージ");
        nowStageInt.setText("0");
        condiSlash.setText("/");
        clearTxt.setText("OK!");
        initZeroTextView();

        scoreTxtV.setTextSize(30);
        scoreTxt.setTextSize(30);
        sumMogra.setTextSize(30);
        slash.setTextSize(30);
        minus.setTextSize(20);
        minus.setTextColor(Color.RED);
        hiyokoCountTxt.setTextSize(20);
        hiyokoCountint.setTextSize(20);
        hiyokoCountint.setTextColor(Color.RED);
        //hiyokoSlash.setTextSize(30);
        //sumHiyoko.setTextSize(30);
        //sumHiyoko.setTextColor(Color.RED);
        hiScoreTxtV.setTextSize(20);
        highScoreTxt.setTextSize(20);
        highScoreTime.setTextSize(20);
        nokoriTxtV.setTextSize(30);
        nokoriTime.setTextSize(30);
        nowStageTxt.setTextSize(20);
        nowStageInt.setTextSize(20);
        nowCondiNum.setTextSize(20);
        condiSlash.setTextSize(20);
        condiNum.setTextSize(20);
        clearTxt.setTextSize(20);
        clearTxt.setTextColor(Color.RED);
        clearTxt.setAlpha(0.0f);

        RelativeLayout.LayoutParams happyIconParam       = new RelativeLayout.LayoutParams(100,100);//happyIcon.getWidth(),happyIcon.getHeight());
        RelativeLayout.LayoutParams scoreTxtParam        = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        RelativeLayout.LayoutParams scoreParam           = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        RelativeLayout.LayoutParams sumMograParam        = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        RelativeLayout.LayoutParams slashParam           = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        RelativeLayout.LayoutParams minusParam           = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        RelativeLayout.LayoutParams hiScoreTxtParam      = new RelativeLayout.LayoutParams(dpToPx(115), dpToPx(43));
        RelativeLayout.LayoutParams hiScoreParam         = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        RelativeLayout.LayoutParams hiScoreTimeParam     = new RelativeLayout.LayoutParams(dpToPx(140) , dpToPx(43));
        RelativeLayout.LayoutParams hiyokoCountParam     = new RelativeLayout.LayoutParams(dpToPx(115), dpToPx(43));
        RelativeLayout.LayoutParams hiyokoCountintParam  = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(100));
        //RelativeLayout.LayoutParams hiyokoSlashParam     = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        //RelativeLayout.LayoutParams sumHiyokoParam       = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        RelativeLayout.LayoutParams nokoriTxtParam       = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        RelativeLayout.LayoutParams nokoriParam          = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        RelativeLayout.LayoutParams nowStageTxtParam     = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        final RelativeLayout.LayoutParams nowStageIntParam     = new RelativeLayout.LayoutParams(dpToPx(90) , dpToPx(43));
        RelativeLayout.LayoutParams nowCondiNumParam     = new RelativeLayout.LayoutParams(dpToPx(90), dpToPx(43));
        RelativeLayout.LayoutParams condiSlashParam      = new RelativeLayout.LayoutParams(dpToPx(90), dpToPx(43));
        RelativeLayout.LayoutParams condiNumParam        = new RelativeLayout.LayoutParams(dpToPx(90), dpToPx(43));
        RelativeLayout.LayoutParams clearTxtParam        = new RelativeLayout.LayoutParams(dpToPx(150), dpToPx(43));

        int left = dpToPx(16);
        int center = dpToPx(196);
        int right = dpToPx(308);
        int paramPoint = dpToPx(108);
        int paramPoint2 = dpToPx(108 + 20);
        int top = dpToPx(16);
        int top2 = dpToPx(68);
        int top3 = dpToPx(120);
        int top4 = dpToPx(172);

        happyIconParam.setMargins       (center + 100, top + 100,0,0);
        scoreTxtParam.setMargins        (left       ,top  , 0,  0);
        scoreParam.setMargins           (paramPoint ,top  , 0,  0);
        sumMograParam.setMargins        (center - 30 ,top  , 0,  0);
        slashParam.setMargins           (center - dpToPx(30) ,top  , 0,  0);
        minusParam.setMargins           (paramPoint - dpToPx(20) - 60 ,top2,0,0);
        hiyokoCountParam.setMargins     (left ,top2 , 0,  0);
        hiyokoCountintParam.setMargins  (paramPoint - dpToPx(15) - 60 ,top2 , 0,  0);
        hiScoreTxtParam.setMargins      (center - dpToPx(65) - 60 ,top2 , 0,  0);
        hiScoreParam.setMargins         (center + 30  ,top2 , 0,  0);
        hiScoreTimeParam.setMargins     (center + 140 ,top2 , 0,  0);
        nokoriTxtParam.setMargins       (center + 140 ,top  , 0,  0);
        nokoriParam.setMargins          (right  + 100 ,top  , 0,  0);
        nowStageTxtParam.setMargins     (left, top3 - 50,0,0);
        nowStageIntParam.setMargins     (paramPoint2 - 80, top3 - 50, 0, 0);
        nowCondiNumParam.setMargins     (center - 100 , top3 - 50, 0, 0);
        condiSlashParam.setMargins      (center            , top3 - 50, 0, 0);
        condiNumParam.setMargins        (center + 30  , top3 - 50, 0, 0);
        clearTxtParam.setMargins        (center + 150  , top3 - 50, 0, 0);

        happyIcon.setLayoutParams(happyIconParam);
        scoreTxtV.setLayoutParams(scoreTxtParam);
        scoreTxt.setLayoutParams(scoreParam);
        sumMogra.setLayoutParams(sumMograParam);
        slash.setLayoutParams(slashParam);
        minus.setLayoutParams(minusParam);
        hiyokoCountTxt.setLayoutParams(hiyokoCountParam);
        hiyokoCountint.setLayoutParams(hiyokoCountintParam);
        hiScoreTxtV.setLayoutParams(hiScoreTxtParam);
        highScoreTxt.setLayoutParams(hiScoreParam);
        highScoreTime.setLayoutParams(hiScoreTimeParam);
        nokoriTxtV.setLayoutParams(nokoriTxtParam);
        nokoriTime.setLayoutParams(nokoriParam);
        nowStageTxt.setLayoutParams(nowStageTxtParam);
        nowStageInt.setLayoutParams(nowStageIntParam);
        nowCondiNum.setLayoutParams(nowCondiNumParam);
        condiSlash.setLayoutParams(condiSlashParam);
        condiNum.setLayoutParams(condiNumParam);
        clearTxt.setLayoutParams(clearTxtParam);

        happyIcon.setAlpha(0.0f);

        //ゲームデータをロードする
        String loadDate = "";
        gameData = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        //ハイスコアを読み込む
        sharedHighScore = gameData.getInt("HIGH_SCORE", 0);
        recordScore = sharedHighScore;
        loadDate = gameData.getString("UPDATE", "");
        highScoreTxt.setText(String.format("%03d", sharedHighScore));
        if(loadDate == ""){ highScoreTime.setText("[ ----/--/-- ]"); }
        else{ highScoreTime.setText(loadDate); }

        // ボタン部分
        RelativeLayout.LayoutParams startBtnParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
        startBtnParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,   12);
        startBtnParam.addRule(RelativeLayout.CENTER_HORIZONTAL,     14);

        startMogGameBtn = new Button(this);
        String startBtnLabel = "Start!";
        startMogGameBtn.setText(startBtnLabel);
        startMogGameBtn.setLayoutParams(startBtnParam);
        startMogGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ボタンタップ時の処理
                Log.d("DEBUG_TAG","GameStart!");
                gameStart();
            }
        });

        RelativeLayout.LayoutParams stopBtnParam = new RelativeLayout.LayoutParams(windowWidth / 2, 250);
        stopBtnParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        stopBtnParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        stopGameBtn = new Button(this);
        String stpBtnTxt = "中断";
        stopGameBtn.setText(stpBtnTxt);
        stopGameBtn.setTextSize(30);
        stopGameBtn.setLayoutParams(stopBtnParam);
        stopGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ボタンタップ時の処理
                stopMog = true;
                updateHighScore = false;
                endGame();
            }
        });

        stopGameBtn.setEnabled(false);

        RelativeLayout.LayoutParams resetBtnParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        resetBtnParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        resetBtnParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        resetScoreBtn = new Button(this);
        String resetScoreBtnTxt = "ハイスコアリセット";
        resetScoreBtn.setText(resetScoreBtnTxt);
        resetScoreBtn.setLayoutParams(resetBtnParam);
        resetScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ボタンタップ時の処理
                DialogFragment newFlagment = new ShowDialogClass();
                newFlagment.show(getSupportFragmentManager(),"Reset");
            }
        });

        mogUpDnCanvas[0].setMogPoint(1,1);
        mogUpDnCanvas[1].setMogPoint(1,2);
        mogUpDnCanvas[2].setMogPoint(1,3);
        mogUpDnCanvas[3].setMogPoint(2,1);
        mogUpDnCanvas[4].setMogPoint(2,2);
        mogUpDnCanvas[5].setMogPoint(2,3);
        mogUpDnCanvas[6].setMogPoint(3,1);
        mogUpDnCanvas[7].setMogPoint(3,2);
        mogUpDnCanvas[8].setMogPoint(3,3);

        mograHeight = mogUpDnCanvas[0].checkMograHeight();
        int mograHeadHeight = 35;

        //隠す部分上部
        RelativeLayout.LayoutParams upMogSoilLayout = new RelativeLayout.LayoutParams(1500,250);
        upMogSoilLayout.setMargins(0,820, 0, 0);
        ImageView upMogSoil = new ImageView(this);
        upMogSoil.setImageResource(R.drawable.soil);
        upMogSoil.setLayoutParams(upMogSoilLayout);
        upMogSoil.setBackgroundColor(backgroundColor);

        //隠す部分中央部
        RelativeLayout.LayoutParams centerMogSoilLayout = new RelativeLayout.LayoutParams(1500,250);
        centerMogSoilLayout.setMargins(0,820 + mograHeight + mograHeadHeight + 20, 0, 0);
        ImageView centerMogSoil = new ImageView(this);
        centerMogSoil.setImageResource(R.drawable.soil);
        centerMogSoil.setLayoutParams(centerMogSoilLayout);
        centerMogSoil.setBackgroundColor(backgroundColor);

        //隠す部分下部
        RelativeLayout.LayoutParams underMogSoilLayout = new RelativeLayout.LayoutParams(1500,250);
        underMogSoilLayout.setMargins(0,(820 + mograHeight) + mograHeight + mograHeadHeight + 50, 0, 0);
        ImageView underMogSoil = new ImageView(this);
        underMogSoil.setImageResource(R.drawable.soil);
        underMogSoil.setLayoutParams(underMogSoilLayout);
        underMogSoil.setBackgroundColor(backgroundColor);

        //見た目 - テキスト
        //relativeLayout.addView(backImg);
        relativeLayout.addView(scoreTxtV);
        relativeLayout.addView(scoreTxt);
        relativeLayout.addView(slash);
        relativeLayout.addView(sumMogra);
        relativeLayout.addView(hiScoreTxtV);
        relativeLayout.addView(highScoreTxt);
        relativeLayout.addView(highScoreTime);
        relativeLayout.addView(nokoriTxtV);
        relativeLayout.addView(nokoriTime);
        relativeLayout.addView(minus);
        relativeLayout.addView(hiyokoCountTxt);
        relativeLayout.addView(hiyokoCountint);
        relativeLayout.addView(nowStageTxt);
        relativeLayout.addView(nowStageInt);
        relativeLayout.addView(nowCondiNum);
        relativeLayout.addView(condiSlash);
        relativeLayout.addView(condiNum);
        relativeLayout.addView(clearTxt);
        relativeLayout.addView(stopGameBtn);
        //見た目 - モグラ
        relativeLayout.addView(mogUpDnCanvas[0]);
        relativeLayout.addView(mogUpDnCanvas[1]);
        relativeLayout.addView(mogUpDnCanvas[2]);
        relativeLayout.addView(upMogSoil);
        relativeLayout.addView(mogUpDnCanvas[3]);
        relativeLayout.addView(mogUpDnCanvas[4]);
        relativeLayout.addView(mogUpDnCanvas[5]);
        relativeLayout.addView(centerMogSoil);
        relativeLayout.addView(mogUpDnCanvas[6]);
        relativeLayout.addView(mogUpDnCanvas[7]);
        relativeLayout.addView(mogUpDnCanvas[8]);
        relativeLayout.addView(underMogSoil);
        relativeLayout.addView(flyBird);
        relativeLayout.addView(companyLogo);
        //Hit or Miss
        relativeLayout.addView(showMsgCanvas[0]);
        relativeLayout.addView(showMsgCanvas[1]);
        relativeLayout.addView(showMsgCanvas[2]);
        relativeLayout.addView(showMsgCanvas[3]);
        relativeLayout.addView(showMsgCanvas[4]);
        relativeLayout.addView(showMsgCanvas[5]);
        relativeLayout.addView(showMsgCanvas[6]);
        relativeLayout.addView(showMsgCanvas[7]);
        relativeLayout.addView(showMsgCanvas[8]);
        relativeLayout.addView(hummerCanvas[0]);
        relativeLayout.addView(hummerCanvas[1]);
        relativeLayout.addView(hummerCanvas[2]);
        relativeLayout.addView(hummerCanvas[3]);
        relativeLayout.addView(hummerCanvas[4]);
        relativeLayout.addView(hummerCanvas[5]);
        relativeLayout.addView(hummerCanvas[6]);
        relativeLayout.addView(hummerCanvas[7]);
        relativeLayout.addView(hummerCanvas[8]);
        relativeLayout.addView(showMsgCanvas[9]);
        //新記録の場合
        relativeLayout.addView(happyIcon);
        //ボタン
//        relativeLayout.addView(startMogGameBtn);
//        relativeLayout.addView(stopGameBtn);
//        relativeLayout.addView(resetScoreBtn);

        //確認用
//        relativeLayout.addView(pointTestCanvas);
//        relativeLayout.addView(touchCanvas);

        //
        // 状況表示画面
        //
        resultLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams resultLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        resultLayout.setLayoutParams(resultLayoutParam);
        resultLayout.setBackgroundColor(Color.argb(150, 0, 0, 0));
        resultLayout.setAlpha(0.0f);

        int whiteColor = Color.WHITE;
        //チュートリアル位置
        int tutMarginTop = 600;
        int tutMarginTop2 = tutMarginTop + 200;
        int tutMarginLeft = 150;
        int tutMarginLeft2 = tutMarginLeft + 250;
        int tutMarginLeft3 = tutMarginLeft2 + 300;

        stageTxt              = new TextView(this);
        stageInt              = new TextView(this);
        endMsg                = new TextView(this);
        endTxt                = new TextView(this);
        endScore              = new TextView(this);
        discript              = new TextView(this);
        discript2             = new TextView(this);
        tutorialText          = new TextView(this);
        tutorialText2         = new TextView(this);
        tutorialCheckText     = new TextView(this);
        touchPleaseTxt        = new TextView(this);

        stageTxt.setText("ステージ1");
        stageInt.setText("0");
        endMsg.setText("GAME OVER");
//        endTxt.setText(String.valueOf(clearMog) + "点以上");
        endScore.setText("0");
        discript.setText(clearMog + "点以上で");
        discript2.setText("次ステージ！");
        tutorialText.setText("タップしよう！！");
        tutorialText2.setText("を");
        tutorialCheckText.setText("チュートリアルを表示しない");
        touchPleaseTxt.setText("画面をタップしてスタート！");

        stageTxt.setTextSize(60);
        stageInt.setTextSize(60);
        endMsg.setTextSize(50);
        endTxt.setTextSize(50);
        endScore.setTextSize(30);
        discript.setTextSize(50);
        discript2.setTextSize(40);
        tutorialText.setTextSize(35);
        tutorialText2.setTextSize(35);
        tutorialCheckText.setTextSize(20);
        touchPleaseTxt.setTextSize(30);

        stageTxt.setTextColor(whiteColor);
        stageInt.setTextColor(whiteColor);
        endMsg.setTextColor(whiteColor);
        endTxt.setTextColor(whiteColor);
        endScore.setTextColor(whiteColor);
        discript.setTextColor(Color.YELLOW);
        discript2.setTextColor(whiteColor);
        tutorialText.setTextColor(whiteColor);
        tutorialText2.setTextColor(whiteColor);
        tutorialCheckText.setTextColor(whiteColor);
        touchPleaseTxt.setTextColor(whiteColor);

        RelativeLayout.LayoutParams stageTxtParam           = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams stageIntParam           = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams endMsgParam             = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams endTxtParam             = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams endScoreParam           = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams discriptParam           = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams discript2Param          = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams tutorialParam           = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams tutorialParam2          = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams checkTxtParam           = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        stageTxtParam.setMargins            (130,100,0,0);
        stageIntParam.setMargins            (center + 240,180,0,0);
        endMsgParam.setMargins              (0,700, 0, 0);
        endTxtParam.setMargins              (paramPoint - 100, 780,0, 0);
        endScoreParam.setMargins            (center + 30, 780, 0, 0);
        discriptParam.setMargins            (0, 1050, 0, 0);
        discript2Param.setMargins           (0, 1200, 0, 0);
        tutorialParam.setMargins            (tutMarginLeft,tutMarginTop2,0,0);
        tutorialParam2.setMargins           (tutMarginLeft3,tutMarginTop + 50,0,0);
        checkTxtParam.setMargins            (100,1400,0,0);

        endMsgParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        discriptParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        discript2Param.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tutorialParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        stageTxt.setLayoutParams(stageTxtParam);
        stageInt.setLayoutParams(stageIntParam);
        endMsg.setLayoutParams(endMsgParam);
        endTxt.setLayoutParams(endTxtParam);
        endScore.setLayoutParams(endScoreParam);
        discript.setLayoutParams(discriptParam);
        discript2.setLayoutParams(discript2Param);
        tutorialText.setLayoutParams(tutorialParam);
        tutorialText2.setLayoutParams(tutorialParam2);
        tutorialCheckText.setLayoutParams(checkTxtParam);

        //ImageView
        RelativeLayout.LayoutParams dotParam = new RelativeLayout.LayoutParams(210,210);
        dotParam.setMargins(tutMarginLeft,tutMarginTop,0,0);
        dotImg = new ImageView(this);
        dotImg.setImageResource(R.drawable.dot);
        dotImg.setLayoutParams(dotParam);
        dotImg.setAlpha(1.0f);

        RelativeLayout.LayoutParams dotParam2 = new RelativeLayout.LayoutParams(210,210);
        dotParam2.setMargins(tutMarginLeft2,tutMarginTop,0,0);
        dotImg2 = new ImageView(this);
        dotImg2.setImageResource(R.drawable.dot);
        dotImg2.setLayoutParams(dotParam2);
        dotImg2.setAlpha(1.0f);

        RelativeLayout.LayoutParams imageViewParam = new RelativeLayout.LayoutParams(200,200);
        imageViewParam.setMargins(tutMarginLeft,tutMarginTop,0,0);
        discriptionImage   = new ImageView(this);
        discriptionImage.setImageResource(drawableMap.get("mogra"));
        discriptionImage.setLayoutParams(imageViewParam);
        discriptionImage.setAlpha(1.0f);

        RelativeLayout.LayoutParams imageViewParam2 = new RelativeLayout.LayoutParams(200,200);
        imageViewParam2.setMargins(tutMarginLeft2,tutMarginTop,0,0);
        discriptionImage2   = new ImageView(this);
        discriptionImage2.setImageResource(drawableMap.get("mogra"));
        discriptionImage2.setLayoutParams(imageViewParam2);
        discriptionImage2.setAlpha(0.0f);

        //Resultボタン
        backTopBtn   = new Button(this);
        nextStageBtn = new Button(this);
        backTopBtn.setText("やめる");
        nextStageBtn.setText("次ステージへ");

        int buttonHeight = 250;
        int buttonWidth  = windowWidth / 2;
        RelativeLayout.LayoutParams backTopBtnParam = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
        RelativeLayout.LayoutParams nextStageBtnParam = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);

//        backTopBtnParam.setMargins(0,1000 ,0 ,0);
//        nextStageBtnParam.setMargins(0,1000 ,0 ,0);

        backTopBtnParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        backTopBtnParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        nextStageBtnParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        nextStageBtnParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        backTopBtn.setLayoutParams(backTopBtnParam);
        backTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameTimer.cancel();
                startGame = false;
                endOKMog = false;
                mogContinue = false;
                firstGame = true;
                onceEnd   = true;
                stageTxt.setText("ステージ1");
                resultLayout.setAlpha(0.0f);
                nextStageBtn.setText("ゲーム開始");
                nextStageBtn.setEnabled(true);
                backTopBtn.setEnabled(false);
                initZeroTextView();
                resultLayout.setAlpha(1.0f);
                animLayout.setAlpha(0.0f);
                mogNumAdmin.stage = 1;
                setTutorialImage(1);
            }
        });

        nextStageBtn.setLayoutParams(nextStageBtnParam);
        nextStageBtn.setEnabled(true);
        nextStageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onceEnd = true;
                if(endGame && startGame){
                    //ゲームスタート
                    gameTimer.cancel();
                    startGame = true;
                    firstGame = true;
                    gameStart();
                }else if(gameRetry){
                    //もう一度
                    gameTimer.cancel();
                    gameRetry = false;
                    gameStart();
                }else{
                    //次のステージ
                    gameTimer.cancel();
                    gameStart();
                }
            }
        });

//描画メモ
//                float[][] testLine = new float[10][];
//                testLine[0] = new float[]{left1PointMog,0,left1PointMog,1600};
//                testLine[1] = new float[]{left2PointMog,0,left2PointMog,1600};
//                testLine[2] = new float[]{0,upPointMog + mograHeight,1000,upPointMog + mograHeight};
//                testLine[3] = new float[]{0,centerPointMog + mograHeight,1000,centerPointMog + mograHeight};
//                testLine[4] = new float[]{0,underPointMog,1000,underPointMog};
//                pointTestCanvas.addLineInfo(testLine,5);

        resultLayout.addView(stageTxt);
//        resultLayout.addView(stageInt);
        resultLayout.addView(endMsg);
//        resultLayout.addView(endTxt);
//        resultLayout.addView(endScore);
//        resultLayout.addView(updateHighScoreTxt);
        resultLayout.addView(discript);
        resultLayout.addView(discript2);
        resultLayout.addView(dotImg);
        resultLayout.addView(discriptionImage);
        resultLayout.addView(tutorialText);
        resultLayout.addView(dotImg2);
        resultLayout.addView(discriptionImage2);
        resultLayout.addView(tutorialText2);
        resultLayout.addView(backTopBtn);
        resultLayout.addView(nextStageBtn);
//        resultLayout.addView(checkBox);
//        resultLayout.addView(tutorialCheckText);

//        resultLayout.addView(backTopBtn);
//        resultLayout.addView(nextStageBtn);

        relativeLayout.addView(resultLayout);

        animLayout = new RelativeLayout(this);
        animLayout.setBackgroundColor(Color.argb(150, 0, 0, 0));
        clearText  = new TextView(this);
        updateHighScoreTxt    = new TextView(this);
        RelativeLayout.LayoutParams updateHighScoreTxtParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams clearParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        clearParam.setMargins(0,100,0,0);
        updateHighScoreTxtParam.setMargins  (0,0, 0, 0);
        clearParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        clearText.setLayoutParams(clearParam);
        clearText.setText(bou + "クリアー");
        clearText.setTextSize(50);
        clearText.setTextColor(whiteColor);
        updateHighScoreTxt.setLayoutParams(updateHighScoreTxtParam);
        updateHighScoreTxt.setText(bou + "ハイスコア更新！");
        updateHighScoreTxt.setTextSize(50);
        updateHighScoreTxt.setTextColor(whiteColor);
        animLayout.addView(clearText);
//        animLayout.addView(updateHighScoreTxt);
        animLayout.setAlpha(0.0f);

        relativeLayout.addView(animLayout);

        setContentView(relativeLayout);

        resultLayout.setAlpha(1.0f);
        backTopBtn.setEnabled(false);
        nextStageBtn.setText("ゲーム開始");
        endMsg.setText("");
        updateHighScoreTxt.setText("");
        backTopBtn.setTextSize(30);
        nextStageBtn.setTextSize(30);

        attackFlagAllOff();
        mogAllDown();
        mogNumAdmin.stage = 1;
        setTutorialImage(mogNumAdmin.stage);
        //mogAllUp();

        //attackFlagAllOn();

        ObjectAnimator downAnim = ObjectAnimator.ofFloat(clearText,"translationY",-100,windowHeight / 3);
        downAnim.setDuration(1500);
        ObjectAnimator downHighAnim = ObjectAnimator.ofFloat(updateHighScoreTxt,"translationY",0,windowHeight / 3 + 100);
        downAnim.setDuration(1500);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(clearText,"alpha",0.0f,1.0f);
        alphaAnim.setDuration(1500);
        ObjectAnimator alphaHighAnim = ObjectAnimator.ofFloat(updateHighScoreTxt,"alpha",0.0f,1.0f);
        alphaAnim.setDuration(1500);
        gameEndAnim = new AnimatorSet();
        gameEndAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                resulting = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        gameEndAnim.play(downAnim).with(downHighAnim).with(alphaAnim).with(alphaHighAnim);;

        /*
        //座標の計算が終了後に値を取得する
        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {

            }
        });
         */
    }

    private void initZeroTextView(){
        scoreTxt.setText(String.format("%03d", 0));
        sumMogra.setText(String.format("%03d", 0));
        hiyokoCountint.setText(String.format("%03d", 0));
        nokoriTime.setText("0");
        nowCondiNum.setText(String.format("%03d", 0));
        condiNum.setText(String.format("%03d", 0));
        happyIconAnim();
    }

    private void resetUpDn(){
        for(int i = 0; i < mogUpDnCanvas.length ; i++){
            mogUpDnCanvas[i].resetParam();
        }
    }

    public void resetHighScore(){
        Log.d("ResetButton","reset record");
        SharedPreferences.Editor updateGameData = gameData.edit();
        updateGameData.putInt("HIGH_SCORE", 0);
        updateGameData.putString("UPDATE", "");
        updateGameData.apply();
        sharedHighScore = 0;
        recordScore = 0;
        highScoreTxt.setText(String.format("%03d", 0));
        highScoreTxt.setTextColor(Color.BLACK);
        happyIconAnim();
    }

    private void setTutorialImage(int stage){
        int nowStage = mogNumAdmin.stage % 5;
        Log.d("tutorial","Num:" + nowStage);
        if(stage < 5){ nowStage = stage; }
        int image = 0;
        int image2 = 0;
        switch(nowStage){
            case 1:
                //もぐらと鳥
                image = drawableMap.get("mogra");
                tutorialText.setText("タップしよう！");
                tutorialText.setTextColor(Color.WHITE);
                image2 = drawableMap.get("bird");
                discriptionImage2.setAlpha(1.0f);
                dotImg2.setAlpha(1.0f);
                break;
            case 2:
                //ひよこ
                image = drawableMap.get("hiyoko");
                tutorialText.setText("タップしない！");
                tutorialText.setTextColor(Color.RED);
                discriptionImage2.setAlpha(0.0f);
                dotImg2.setAlpha(0.0f);
                break;
            case 3:
                //テンレッグとレミング
                image = drawableMap.get("tenreg");
                image2 = drawableMap.get("lemming");
                tutorialText.setText("タップしよう！");
                tutorialText.setTextColor(Color.WHITE);
                discriptionImage2.setAlpha(1.0f);
                dotImg2.setAlpha(1.0f);
                break;
            case 4:
                //死神
                image = drawableMap.get("deathMan");
                tutorialText.setText("スワイプしよう！");
                tutorialText.setTextColor(Color.WHITE);
                discriptionImage2.setAlpha(0.0f);
                dotImg2.setAlpha(0.0f);
                break;
            default:
                Log.d("tutorialImage","1～5を選択せよ！");
        }
        discriptionImage.setImageResource(image);
        discriptionImage2.setImageResource(image2);
    }

    private void mogMultiUp(int[] upMog)
    {
        for(int i = 0;i < upMog.length; i++){
            //if(upMog[i] == 10){ continue; }
            if (mogUpDnCanvas[upMog[i]].checkAttackFlag()) {
                if(upMog[i] == 0 && !(timeCount % 30 == 0)){ continue; }
                //if(nowMograNum > 0 && !arrayRecord[upMog[i]]){
                if(!recordHiyoko[upMog[i]]){
                    mogUpDnCanvas[upMog[i]].piyokana(false);
                    //nowMograNum -= 1;
                    mogNumAdmin.mograCountAddOrCut(1);
                }
                //else if(nowHiyokoNum > 0 && arrayRecord[upMog[i]]){
                else if(recordHiyoko[upMog[i]]){
                    mogUpDnCanvas[upMog[i]].piyokana(true);
                    //nowHiyokoNum -= 1;
                    mogNumAdmin.hiyokoCountAddOrCut(1);
                }
                //else{ break; }
                mogAttack = mogMove(mogUpDnCanvas[upMog[i]],recordHiyoko[upMog[i]],upTime[upMog[i]],upMog[i]);
                if (!mogAttack && !lastTimeMog) {
                    beforeUpTime[upMog[i]] = upTime[upMog[i]];
                    upTime[upMog[i]] = timeCount;
                    mogUpDnCanvas[i].coolTimeFlag(true);
                    DnHd[upMog[i]].postDelayed(mogRunnable(mogUpDnCanvas[upMog[i]], upTime[upMog[i]],beforeUpTime[upMog[i]]), 2500);
                    //Log.d("NumCount","\nMogra:" + mograCount + "\nHiyoko:" + hiyokoCount);
                    //sumMogra.setText(String.valueOf(mograCount));
                }
            }
        }
    }

    private void deathMulti(int upDeath[]){
        for(int d = 0; d < upDeath.length; d++){
            if(upDeath[d] == 10){ continue; }
            if(spawnHum[upDeath[d]]){
                spawnHum[upDeath[d]] = false;
                hummerCanvas[upDeath[d]].setAlpha(1.0f);
                hummerCanvas[upDeath[d]].emergHumAnim();
                humHd[upDeath[d]].postDelayed(humRunnable(upDeath[d]), 3000);
            }
        }
    }

    //上にあげるアニメーション
    private boolean mogMove(MogUpDnCanvas mogUpDnCanvas,boolean recNgMog,int upTime,int mogNum)
    {
        boolean selectNgmog = recNgMog;
        // ひよこさん出す場合Canvasに変更お願いする
        mogUpDnCanvas.piyokana(selectNgmog);
        moveOKMog = mogUpDnCanvas.moveMogAni("up",false,selectNgmog);
        //アニメーションができたら頭出てる判定のフラグ立て
        if(moveOKMog){
            mogUpDnCanvas.attackFlag(false);
            mogUpDnCanvas.upMogFlag(true);
            mogUpDnCanvas.coolTimeFlag(true);
            //Log.d("Main:mogMove","mogAttack:false");
            return false;
        }
        //埋まっている状態の判定のままにする
        //Log.d("Main:mogMove","mogAttack:true");
        return true;
    }

    //タップされていない場合遅れて下に下げるアニメーション
    private Runnable mogRunnable(MogUpDnCanvas mogUpDnCanvas,int upTimeRec,int beforeUpTimeRec)
    {
        final MogUpDnCanvas setMogUpDnCanvas = mogUpDnCanvas;
        final boolean checkAttackMog = setMogUpDnCanvas.checkAttackFlag();
        final boolean checkUpMog = setMogUpDnCanvas.checkUpMogFlag();
        final int upTime = upTimeRec;
        final int beforeUpTime = beforeUpTimeRec;

        Runnable mogRunn = new Runnable() {
            @Override
            public void run() {
                //
                //叩かれていなくて、ゲーム終了でなかったら実行   //3秒以上上がっている
                if( (!checkAttackMog && checkUpMog) && !endGame){//&& upTime - beforeUpTime >= 20 ){
                    setMogUpDnCanvas.moveMogAni("dn", false, false);
                    setMogUpDnCanvas.attackFlag(true);
                    setMogUpDnCanvas.upMogFlag(false);
                    setMogUpDnCanvas.coolTimeFlag(false);
                }
            }
        };
        return mogRunn;
    }

    private Runnable humRunnable(final int humNum){
        final boolean checkFlag = hummerCanvas[humNum].checkFlag();

        Runnable humRunn = new Runnable() {
            @Override
            public void run() {
                if(!checkFlag){
                    hummerCanvas[humNum].missHum(0);
                }
            }
        };

        return humRunn;
    }

    private void mogAllDown()
    {
        for(int i = 0; i < mogUpDnCanvas.length; i++){
            mogUpDnCanvas[i].piyokana(false);
            if(!mogUpDnCanvas[i].checkAttackFlag() && mogUpDnCanvas[i].checkUpMogFlag()){
                mogUpDnCanvas[i].moveMogAni("dn",false,false);
            }
        }
        attackFlagAllOn();
    }

    private void mogAllUp()
    {
        for(int i = 0; i < mogUpDnCanvas.length; i++){
            mogUpDnCanvas[i].piyokana(false);
            if(mogUpDnCanvas[i].checkAttackFlag() && !mogUpDnCanvas[i].checkUpMogFlag()){
                mogUpDnCanvas[i].moveMogAni("up",false,false);
            }
        }
        if(endGame){ attackFlagAllOn(); }
    }

    // true     ：叩かれた状態(叩けない)
    // false    ：叩かれていない状態(叩ける)
    private void attackFlagAllOn() {
        for(int i = 0; i < mogUpDnCanvas.length; i++){
            mogUpDnCanvas[i].attackFlag(true);
            Log.d("AttackFlagAllOn","\nMogUpCanvas" + (i + 1) + ": attackFlag[ " + mogUpDnCanvas[i].checkAttackFlag() + " ]");
        }
    }

    private void attackFlagAllOff()
    {
        for(int i = 0; i < mogUpDnCanvas.length; i++){
            mogUpDnCanvas[i].attackFlag(false);
            mogUpDnCanvas[i].upMogFlag(true);
            Log.d("AttackFlagAllOff","\nMogUpCanvas" + (i + 1) + ": attackFlag[ " + mogUpDnCanvas[i].checkAttackFlag() + " ]");
        }
    }

    private void cancelAllAnim(){
        for(int i = 0; i < mogUpDnCanvas.length; i++){
            mogUpDnCanvas[i].animCancel();
        }
    }

    private void resetCoolTime()
    {
        for(int i = 0; i < mogUpDnCanvas.length; i++){
            mogUpDnCanvas[i].coolTimeFlag(false);
            Log.d("ResetCoolTime","\nMogUpCanvas" + (i + 1) + ": coolTimeFlag[ " + mogUpDnCanvas[i].checkCoolTImeFlag() + " ]");
        }
    }

    public void gameStart() {
        tapCount = 0;
        if(firstGame){
            mogNumAdmin.stage = 1;
            nowStageInt.setText(String.valueOf(1));
            setTutorialImage(mogNumAdmin.stage);
            firstGame = false;
        }
        if (mogNumAdmin.stage < 5) {
            int scoreNum = mogNumAdmin.stage - 1;
            clearMog = clearScore[scoreNum];
        }else{
            clearMog += 30;
        }
        discript.setAlpha(1.0f);
        updateHighScoreTxt.setAlpha(0.0f);
        dotImg.setAlpha(1.0f);
        tutorialText.setAlpha(1.0f);
        discriptionImage.setAlpha(1.0f);
        condiNum.setText(String.valueOf(clearMog));
//        endMsg.setText("ステージクリア条件");
//        endTxt.setText("得点:");
//        endScore.setText(clearMog + "以上");
        backTopBtn.setAlpha(0.0f);
        nextStageBtn.setAlpha(0.0f);
        backTopBtn.setEnabled(false);
        nextStageBtn.setEnabled(false);
        highScoreTxt.setTextColor(Color.BLACK);
        sumMogra.setText(String.format("%03d", 0));
        hiyokoCountint.setText(String.format("%03d", 0));
        nokoriTime.setText(String.valueOf(countdownTime - 2));
        mogNumAdmin.scoreReset();
        mogNumAdmin.mograCountReset();
        mogNumAdmin.hiyokoCountReset();
        resetCoolTime();
        resultLayout.setAlpha(1.0f);
        Handler startHandler = new Handler();
        startHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!endGame) {
                    resultLayout.setAlpha(0.0f);
                }
            }
        }, 2000);

        resetScoreBtn.setEnabled(false);
        startMogGameBtn.setEnabled(false);
        stopGameBtn.setEnabled(true);
        gameTimer = new Timer();
        countdownTime = setTime;

        playCount++;
        timeCount = 0;
        hiyokoCount = 0;
        lastTimeMog = false;
        endGame = false;
        if (mogNumAdmin.stage != 1) {
            attackFlagAllOff();
            mogAllDown();
            resetUpDn();
        }
        nowStage = mogNumAdmin.stage;
        startSystemTime = System.currentTimeMillis();
            //0.1秒ごとに実行する
            gameTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                //メインスレッド以外でUIを変更するために必要なハンドル
                timerHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    //0.1秒ごとに各値を取得
                    intScore = mogNumAdmin.scoreCheck();
                    mograCountint = (mogNumAdmin.mograCountCheck() * 10);
                    scoreTxt.setText(String.format("%03d", intScore));
                    nowCondiNum.setText(String.format("%03d", intScore));
                    sumMogra.setText(String.format("%03d", mograCountint));
                    hiyokoCountint.setText(String.format("%03d", mogNumAdmin.checkAttackHiyoko()));
                    if (intScore >= clearMog) {
                        clearTxt.setAlpha(1.0f);
                    } else {
                        clearTxt.setAlpha(0.0f);
                    }
                    //sumHiyoko.setText(String.valueOf(-mogNumAdmin.hiyokoCountCheck() * 10));

                    //timeCount 0.1秒ごとにプラス1
                    //1秒ごとに実行
                    if (timeCount % 10 == 0) {
                    /*
                    int beforeCount = 0;
                    int beforeCountHiyo = 0;
                    if(timeCount == 0){
                        nowMograNum = gameMograTime[0];
                        nowHiyokoNum = gameHiyokoTime[0];
                    }else if(timeCount == 100){
                        beforeCount = nowMograNum;
                        beforeCountHiyo = nowHiyokoNum;
                        nowMograNum = gameMograTime[1];
                        nowMograNum += beforeCount;
                        nowHiyokoNum = gameHiyokoTime[1];
                        nowHiyokoNum += beforeCountHiyo;
                    }else if(timeCount == 200){
                        beforeCount = nowMograNum;
                        beforeCountHiyo = nowHiyokoNum;
                        nowMograNum = gameMograTime[2];
                        nowMograNum += beforeCount;
                        nowMograNum += 6;
                        nowHiyokoNum = gameHiyokoTime[2];
                        nowHiyokoNum += beforeCountHiyo;
                    }
                     */
                        if (timeCount != 0 && timeCount != 10) {
                            //Log.d("DEBUG_TAG","RandomStart_normal");
                            randomMog();
                        }
                    }
                /*
                if(timeCount % 5 == 0 && timeCount != 0 && !endGame){
                    if(timeCount >= 70){
                        //15秒を超えたら加速
                        Log.d("FAST_TAG","RandomStart_fast");
                        randomMog();
                    }
                }
                 */
                    if (intScore > recordScore) {
                        //ハイスコアの値をスコアが越した場合ハイスコアの数値も更新
                        highScoreTxt.setText(String.format("%03d",intScore));
                        highScoreTxt.setTextColor(Color.GREEN);
                        sharedHighScore = intScore;
                        happyIconAnim();
                        if (!updateHighScore) updateHighScore = true;
                    }
                    //カウントダウンタイマー
                    nowSystemTime = System.currentTimeMillis();
                    nowGameTime = countdownTime + ((startSystemTime - nowSystemTime) / 1000);
                    //ゲーム時間が終了した場合タイマーストップ
                    if (nowGameTime < 0) {
                        if(onceEnd){
                            onceEnd = false;
                            gameTimer.cancel();
                            endGame();
                        }
                    } else {
                        if(timeCount > 20){
                            nokoriTime.setText(timeFormat.format(nowGameTime));
                        }
                    }
                    timeCount++;

                    }
                });
                }
            }, 0, 100);
    }

    private void randomMog()
    {
        // 1秒ごとに呼び出される関数
        boolean[] arrayCoolTime =
                {mogUpDnCanvas[0].checkCoolTImeFlag()
                ,mogUpDnCanvas[1].checkCoolTImeFlag()
                ,mogUpDnCanvas[2].checkCoolTImeFlag()
                ,mogUpDnCanvas[3].checkCoolTImeFlag()
                ,mogUpDnCanvas[4].checkCoolTImeFlag()
                ,mogUpDnCanvas[5].checkCoolTImeFlag()
                ,mogUpDnCanvas[6].checkCoolTImeFlag()
                ,mogUpDnCanvas[7].checkCoolTImeFlag()
                ,mogUpDnCanvas[8].checkCoolTImeFlag()
                };
        ngMog = false;
        Random randomMog = new Random();
        //鳥は飛ぶか
        if(!flyingBird){//&& nowStage > 2){
            int canFly = randomMog.nextInt(100);
            if(canFly < 10){
                mogNumAdmin.mograCountAddOrCut(2);
                flyBird.setAlpha(1.0f);
                flyBird.startAnimation(birdMove);
                flyingBird = true;
            }
        }
        //死神さんが出るか
        int canHum = randomMog.nextInt(100);
        if(canHum < 10 && nowStage > 3){
            int multiDeathCount = randomMog.nextInt(4);
            int[] arrayDeath = new int[multiDeathCount];
            for(int d = 0; d < multiDeathCount; d++){
                int donoDeath = randomMog.nextInt(9);
                if(!spawnHum[donoDeath]){
                    spawnHum[donoDeath] = true;
                    arrayDeath[d] = donoDeath;
                }else{
                    arrayDeath[d] = 10;
                }
            }
            deathMulti(arrayDeath);
        }


        //どのモグラが出てくるか
        int donoMog;
        int multiMog = randomMog.nextInt(mograCountNum);
        boolean sameMog = false;
//        if( 1 < multiMog){
            int[] arrayRandomMog = new int[multiMog];
            /*
            for(int k = 0;k < multiMog; k++){
                //10を選択なしということにする
                arrayRandomMog[k] = 10;
            }
             */
            for(int i = 0;i < multiMog; i++){
                ngMog = false;
                donoMog = randomMog.nextInt(mograCountNum);
                if(arrayRandomMog != null){
                    if(arrayCoolTime[donoMog]){ continue; }
                    if(multiMog != 9) {
                        for (int j = 0; j < arrayRandomMog.length; j++) {
                            if (arrayRandomMog[j] == donoMog) {
                                sameMog = true;
                                break;
                            }
                        }
                        if (sameMog) {
                            sameMog = false;
                            i--;
                            continue;
                        }
                    }else{
                        arrayRandomMog[i] = i;
                    }
                    //ひよこになるか
                    int intNgMog = randomMog.nextInt(12);
                    if(intNgMog < 4 && !lastTimeMog && nowStage > 1){
                        //Log.d("MOG_TAG", "donomog:" + donoMog);
                        ngMog = true;
                    }
                    if(ngMog){ recordHiyoko[donoMog] = true; }
                    else{ recordHiyoko[donoMog] = false; }

                    }else{
                    Log.d("RandomMog","arrayRandomMog is null!");
                }
                arrayRandomMog[i] = donoMog;
            }
        mogMultiUp(arrayRandomMog);
    }

    private void happyIconAnim(){
        if(!endGame){
            happyIcon.setAlpha(1.0f);
        }else if(endGame){
            happyIcon.setAlpha(0.0f);
        }
    }

    private void endGame(){
        Log.d("NowStage","stage:" + mogNumAdmin.stage);
        animLayout.setAlpha(1.0f);
        gameEndAnim.start();
        endGame = true;
//        discript.setAlpha(0.0f);
//        tutorialText.setAlpha(0.0f);
//        discriptionImage.setAlpha(0.0f);
        updateHighScoreTxt.setAlpha(1.0f);
        String addHigh = "";
        if(updateHighScore && !stopMog){
            //ハイスコアを更新していた場合書き換える
            Log.d("DEBUG_TAG","write record");
            SharedPreferences.Editor updateGameData = gameData.edit();
            updateGameData.putInt("HIGH_SCORE", intScore);
            updateGameData.putString("UPDATE", highScoreDateFormat.format(highScoreDate.getTime()));
            updateGameData.apply();
            highScoreTime.setText(highScoreDateFormat.format(highScoreDate.getTime()));
            happyIconAnim();
            addHigh = bou + "ハイスコア更新！";
            updateHighScore = false;
            recordScore = sharedHighScore;
        }else{
            updateHighScoreTxt.setText("");
        }
        mogNumAdmin.attackHiyokoReset();
        attackFlagAllOff();
        resetCoolTime();
        cancelAllAnim();
        mogAllDown();
        //mogAllUp();
        nokoriTime.setText("0");
        mogNumAdmin.mograCountReset();
        mogNumAdmin.hiyokoCountReset();
        Log.d("MainView","GameOver");
        //見た目
        endTxt.setText("Score:");
        endTxt.setTextSize(30);
        resetScoreBtn.setEnabled(true);
        stopGameBtn.setEnabled(false);
        backTopBtn.setAlpha(1.0f);
//        backTopBtn.setEnabled(true);
        nextStageBtn.setAlpha(1.0f);
        endScore.setText(String.valueOf(intScore));
//        nextStageBtn.setEnabled(true);
        endHummer();
        float alphaNum = 0.0f;
        String setClearText = "";
        if(stopMog){
            //中断した場合
//            endMsg.setText("中断");
            setClearText = bou + "中断";
            nextStageBtn.setText("再プレイ");
            stopMog = false;
            mogContinue = true;
            nowStageInt.setText("0");
            highScoreTxt.setText(String.format("%03d", recordScore));
            highScoreTxt.setTextColor(Color.BLACK);
            happyIconAnim();
            alphaNum = 0.0f;
            updateHighScore = false;
        }else if(intScore >= clearMog){
            mogContinue = false;
//            endMsg.setText("クリアー！");
            setClearText = bou + "クリアー！";
            nextStageBtn.setText("次ステージへ");
            nextStageBtn.setEnabled(true);
            alphaNum = 1.0f;
            mogNumAdmin.stage += 1;
        }else{
//            endMsg.setText("ゲームオーバー");
            setClearText = bou + "ゲームオーバー";
            nextStageBtn.setText("再プレイ");
            alphaNum = 0.0f;
            mogContinue = true;
        }
//        discript.setAlpha(alphaNum);
//        discript2.setAlpha(alphaNum);
//        discriptionImage.setAlpha(alphaNum);
//        discriptionImage2.setAlpha(alphaNum);
//        dotImg.setAlpha(alphaNum);
//        dotImg2.setAlpha(alphaNum);
//        tutorialText.setAlpha(alphaNum);
//        tutorialText2.setAlpha(alphaNum);
        int nextStage = mogNumAdmin.stage -1;
        if(mogNumAdmin.stage > 3){
            nextStage = 3;
            nextClearScore += 20;
            discript.setText(nextClearScore + "点以上で");
        }else{
            discript.setText(clearScore[nextStage] + "点以上で");
            nextClearScore = clearScore[nextStage];
        }
        clearText.setText(setClearText + "\n" + addHigh);
        stageTxt.setText("ステージ" + mogNumAdmin.stage);
        if(mogNumAdmin.stage < 5){
            setTutorialImage(mogNumAdmin.stage);
        }else{
            setTutorialImage(4);
        }
        resultLayout.setAlpha(1.0f);
        Handler delayNext = new Handler();
        delayNext.postDelayed(new Runnable() {
            @Override
            public void run() {
                endOKMog = true;
            }
        },2000);
    }

    private void endHummer(){
        for(int i = 0; i < 9; i++){
            hummerCanvas[i].endHum();
        }
    }

    private int dpToPx(int px)
    {
        float d = screenDensity;
        return (int)((px * d) + 0.5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float endTouchX,endTouchY;
        swipeHum    = false;
        endTouchX   = 0;
        endTouchY   = 0;

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //指を画面に触れた瞬間
                touchCanvas.canvasTouch(event.getX(),event.getY());
                if(!endGame) {
                    //ゲーム中の場合
                    //タッチしたときの座標取得
                    startTouchX = event.getX();
                    startTouchY = event.getY();
                }else if(resulting && endOKMog){
                    resulting = false;
                    ObjectAnimator layoutAlpAnim = ObjectAnimator.ofFloat(animLayout,"alpha",1.0f,0.0f);
                    layoutAlpAnim.setDuration(1500);
                    layoutAlpAnim.start();
                    backTopBtn.setEnabled(true);
                    nextStageBtn.setEnabled(true);
                }
//                else if(endOKMog){
//                    endOKMog = false;
//                    if(!mogContinue){
//                        mogNumAdmin.stage++;
//                        nowStageInt.setText(String.valueOf(mogNumAdmin.stage));
//                        clearMog += 10;
//                    }
//                    resultLayout.setAlpha(0.0f);
//                    gameStart();
//                }

                break;
            case MotionEvent.ACTION_MOVE:
                //触れてから離さずに動いた場合
                if(!endGame) {
                    swipeStartTime = System.currentTimeMillis();
                }
                break;
            case MotionEvent.ACTION_UP:
                //画面から指を離した瞬間
                if(!endGame) {
                    //ゲーム中の場合
                    swipeEndTime = System.currentTimeMillis();
                    endTouchX = event.getX();
                    endTouchY = event.getY();
                    float absX = Math.abs(startTouchX - endTouchX);
                    float absY = Math.abs(startTouchY - endTouchY);
                    if (absX > 100 && absY > 100) {
                        Log.d("ACTION_UP", "Swipe");
                        swipeHum = true;
                    }
                    if (swipeHum && swipeEndTime - swipeStartTime < 200) {
                        swipeHum = false;
                        Log.d("ACTION_UP", "SwipeOK");
                        mogTouchPoint(startTouchX, startTouchY, 2);
                    } else {
//                    Log.d("MainCanvas", "touchX:" + touchX + "\ntouchY:" + touchY);
//                    Log.d("Bird","\nBirdPoint:" + birdTopPoint + "\nBird + heightPoint:" + (birdTopPoint +  flyBird.getHeight()) );
                        //座標からどのモグラか判断する
                        mogTouchPoint(startTouchX, startTouchY, 1);
                    }
                }else{
                    //ゲームが終了している
                    tapCount++;
                    if(tapCount > 10){
                        DialogFragment newFlagment = new ShowDialogClass();
                        newFlagment.show(getSupportFragmentManager(),"Reset");
                        tapCount = 0;
                    }
                }
                break;
            default :
                return super.onTouchEvent(event);
        }

        return true;
    }

    private void mogTouchPoint(float touchX, float touchY, int mode){
        //Modeは １：モグラ ２：ハンマー
        /* upPointMog:モグラ部分の一番上のY座標
         * underPointMog:モグラ部分の一番下のY座標
         * left1PointMog: 左モグラの右端X座標
         * left2PointMog: 真ん中モグラの右端X座標
         */
        boolean topMog,centerMog,bottomMog;
        topMog = centerMog = bottomMog = false;
        if(birdTopPoint <= touchY && touchY < birdTopPoint + flyBird.getHeight() && flyingBird && !birdTap){
            Log.d("Bird","BirdTouched");
            showMsgCanvas[9].setMsgPoint(touchX,touchY);
            showMsgCanvas[9].msgType(1);
            mogNumAdmin.scoreAddOrCut(20);
            birdTap = true;
        }else if( upPointMog < touchY && touchY < underPointMog ){
            //モグラのタップ範囲かを判定
            if( touchY < upPointMog + mograHeight ){
                Log.d("MainCanvas","TopTap!");
                topMog = true;
            }else if(upPointMog + mograHeight < touchY && touchY < centerPointMog + mograHeight){
                Log.d("MainCanvas","CenterTap!");
                centerMog = true;
            }else if(centerPointMog + mograHeight < touchY){
                Log.d("MainCanvas","UnderTap!");
                bottomMog = true;
            }
            if (topMog) {
                if(mode == 1) {
                    touchAction(mogUpDnCanvas[0], recordHiyoko[0], mogUpDnCanvas[1]
                            , recordHiyoko[1], mogUpDnCanvas[2], recordHiyoko[2], touchX);
                }else{
                    touchAction(hummerCanvas[0],hummerCanvas[1],hummerCanvas[2],touchX);
                }
            } else if (centerMog) {
                if(mode == 1) {
                    touchAction(mogUpDnCanvas[3], recordHiyoko[3], mogUpDnCanvas[4]
                            , recordHiyoko[4], mogUpDnCanvas[5], recordHiyoko[5], touchX);
                }else{
                    touchAction(hummerCanvas[3],hummerCanvas[4],hummerCanvas[5],touchX);
                }
            } else if (bottomMog) {
                if(mode == 1) {
                    touchAction(mogUpDnCanvas[6], recordHiyoko[6], mogUpDnCanvas[7]
                            , recordHiyoko[7], mogUpDnCanvas[8], recordHiyoko[8], touchX);
                }else{
                    touchAction(hummerCanvas[6],hummerCanvas[7],hummerCanvas[8],touchX);
                }
            }
        }
    }

    private void touchAction(MogUpDnCanvas leftMogCanvas,   boolean leftNgMog
                            ,MogUpDnCanvas centerMogCanvas, boolean centerNgMog
                            ,MogUpDnCanvas rightMogCanvas,  boolean rightNgMog
                            ,float touchX){
        if(touchX < left1PointMog){
            //左モグラ
            if (!leftMogCanvas.checkAttackFlag()){
                touchMoveMog(leftMogCanvas, leftNgMog);
                leftMogCanvas.attackFlag(true);
            }
        }else if(left1PointMog < touchX && touchX < left2PointMog){
            //真ん中モグラ
            if (!centerMogCanvas.checkAttackFlag()){
                touchMoveMog(centerMogCanvas, centerNgMog);
                centerMogCanvas.attackFlag(true);
            }
        }else{
            //右モグラ
            if (!rightMogCanvas.checkAttackFlag()){
                touchMoveMog(rightMogCanvas, rightNgMog);
                rightMogCanvas.attackFlag(true);
            }
        }
    }

    private void touchAction(HummerCanvas left, HummerCanvas center, HummerCanvas right, float touchX){
        if(touchX < left1PointMog) {
            left.swipeAnim();
        }else if(left1PointMog < touchX && touchX < left2PointMog){
            center.swipeAnim();
        }else{
            right.swipeAnim();
        }
    }

    private void touchMoveMog(MogUpDnCanvas mogUpDnCanvas,boolean recNgMog)
    {
        mogUpDnCanvas.moveMogAni("dn", true, recNgMog);
        mogUpDnCanvas.attackFlag(true);
        //mograCanvas.hummerAnim(touchX,touchY);
    }


}
