package com.example.mogratatakigame;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

//タップ時のメッセージを制御するCanvas
public class ShowMsgCanvas extends View {

    private ObjectAnimator zoomMsgAnimX,zoomMsgAnimY,alphaMsgAnim;
    private AnimatorSet msgAnim;
    private Paint paint;
    private final Bitmap msgHitBit,msgMissBit;
    private Bitmap msgBit;
    private float msgLeftP,msgTopP;
    private boolean msgClr;

    @SuppressLint("ObjectAnimatorBinding")
    public ShowMsgCanvas(Context context){
        super(context);

        msgLeftP = 0.0f;
        msgTopP  = 0.0f;

        msgClr = false;

        paint = new Paint();
        //zoomMsgAnimX = new ObjectAnimator();
        zoomMsgAnimY = new ObjectAnimator();
        alphaMsgAnim = new ObjectAnimator();
        msgAnim = new AnimatorSet();

        msgHitBit = BitmapFactory.decodeResource(getResources(), R.drawable.hit);
        msgMissBit = BitmapFactory.decodeResource(getResources(), R.drawable.miss);

        alphaMsgAnim = ObjectAnimator.ofFloat(this, "alpha",1.0f,0.0f);
        alphaMsgAnim.setDuration(800);
        //zoomMsgAnimX = ObjectAnimator.ofFloat(this, "scaleX",0.5f,1.0f);
        //zoomMsgAnimX.setDuration(400);
        zoomMsgAnimY = ObjectAnimator.ofFloat(this, "scaleY",0.9f,1.0f);
        zoomMsgAnimY.setDuration(400);

        msgBit = msgHitBit;
    }

    @Override
    public void onDraw(Canvas canvas){
        //Log.d("msgCanvas","DrawMsg");
        if(!msgClr){
            this.setAlpha(0.0f);
            msgClr = true;
        }else{
            if(msgAnim.isRunning()){
                msgAnim.cancel();
            }
            this.setAlpha(1.0f);
            canvas.drawBitmap(msgBit, msgLeftP, msgTopP, paint);
            //複数アニメーションを一回で実行する
            msgAnim.play(alphaMsgAnim).with(zoomMsgAnimY);//.with(zoomMsgAnimX);
            msgAnim.start();

//        canvas.drawBitmap(msgBit, 0, 0, paint);
        }
    }

    public void msgType(int type){
        //Log.d("msgCanvas","selectMsgType");
        if(type == 1){
            msgBit = msgHitBit;
        }else{
            msgBit = msgMissBit;
        }
        //Log.d("msgCanvas","successMsgBit");

        invalidate();
    }

    public void setMsgPoint(float leftMog, float topMog){
        msgLeftP = leftMog - 100.0f;
        msgTopP  = topMog  - 100.0f;
    }
}
