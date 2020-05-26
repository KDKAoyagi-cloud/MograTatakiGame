package com.example.mogratatakigame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class HummerCanvas extends View {

    private float paintX,paintY;
    private boolean endHum,attack;
    private MogNumAdmin mogNumAdmin;
    private ShowMsgCanvas showMsgCanvas;
    private Paint paint;
    private Bitmap smallHumBit;
    private ObjectAnimator rotateAnim,alphaAnim,endAlphaAnim,endRotateAnim;
    private AnimatorSet animatorSet,endAnimSet,gameEndAlphaAnim;

    public HummerCanvas(Context context,MogNumAdmin conMogNumAd, ShowMsgCanvas conShowMsgCan) {
        super(context);

        Log.d("DRAWHUMMER","HammerCanvas");

        this.setAlpha(0.0f);

        mogNumAdmin = conMogNumAd;
        showMsgCanvas = conShowMsgCan;

        paint = new Paint();
        Bitmap humBit = BitmapFactory.decodeResource(getResources(), R.drawable.shinigami);

        smallHumBit   = Bitmap.createScaledBitmap(humBit,humBit.getWidth() / 7, humBit.getHeight() / 7, true);

        paintX = 0;
        paintY = 0;

        endHum   = true;
        attack   = true;

        rotateAnim = ObjectAnimator.ofFloat(this,"rotation", 0.0f, 23.0f);
        rotateAnim.setDuration(1500);
        alphaAnim = ObjectAnimator.ofFloat(this, "alpha", 0.0f,0.5f);
        alphaAnim.setDuration(1000);
        endAlphaAnim = ObjectAnimator.ofFloat(this, "alpha", 0.5f,0.0f);
        endAlphaAnim.setDuration(1000);
        endRotateAnim = ObjectAnimator.ofFloat(this,"rotation", 23.0f, -23.0f);

        animatorSet = new AnimatorSet();
//        animatorSet.play(rotateAnim).with(alphaAnim);
        animatorSet.play(alphaAnim);
        endAnimSet  = new AnimatorSet();
//        endAnimSet.play(endRotateAnim).with(endAlphaAnim);
        endAnimSet.play(endAlphaAnim);
        endAnimSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if(endHum){
                    alphaChangeView(0.0f);
                }
            }
        });
        gameEndAlphaAnim = new AnimatorSet();
        gameEndAlphaAnim.play(endAlphaAnim);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        Log.d("DRAWHUMMER","onDraw");
        canvas.drawBitmap(smallHumBit, paintX, paintY, paint);
    }

    public void setHumPoint(float x, float y,int col,int row){
        Log.d("DRAWHUMMER","setHumPoint");
        float setX = x;
        float setY = y;

//        if(col == 1){
//            setY += 200;
//        }else if(col == 2){
//            setY += 100;
//        }
//        if(row == 3){
//            setX += 120;
//        }else if(row == 2){
//            setX += 50;
//        }

        paintX = setX;
        paintY = setY;

        showMsgCanvas.setMsgPoint(x,y);

        invalidate();
    }

    public void emergHumAnim(){
        if(attack){
            this.setAlpha(1.0f);
            attack = false;
            animatorSet.start();
        }
    }

    public void swipeAnim(){
        if(animatorSet.isRunning()){ animatorSet.cancel(); }
        if(!attack){
            attack = true;
            endAnimSet.start();
            showMsgCanvas.msgType(1);
            mogNumAdmin.scoreAddOrCut(50);
            mogNumAdmin.mograCountAddOrCut(5);
        }
    }

    public void endHum(){
        if(animatorSet.isRunning()){ animatorSet.cancel(); }
        if(!attack){
            attack = true;
            gameEndAlphaAnim.start();
        }
    }

    public void movePoint(float x, float y){
        paintX = x;
        paintY = y;

        invalidate();
    }

    public void missHum(int msgType){
        if(!attack){
            endHum();
            showMsgCanvas.msgType(msgType);
            attack = true;
            mogNumAdmin.scoreAddOrCut(-50);
            mogNumAdmin.attackHiyokoCountAddOrCut(50);
        }
    }

    public void resetFlag(){
        attack = false;
    }

    public boolean checkFlag(){
        return attack;
    }

    private void alphaChangeView(float alpha){
        this.setAlpha(alpha);
    }
}
