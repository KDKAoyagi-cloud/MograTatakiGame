package com.example.mogratatakigame;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.Random;

//モグラを個々に制御するCanvas
public class MogUpDnCanvas extends View {

    private ShowMsgCanvas showMsgCanvas;
    private MogNumAdmin mogNumAdmin;
    private HummerCanvas hummerCanvas;
    private Bitmap smallMograBit,smallHiyokoBit,smallTenregBit,smallLemmingBit,bigMograBit,smallGoldMograBit
            ,outThingBit;
    private Paint paint;
    private ObjectAnimator mogAni;
    private float leftMog,topMog;
    private int mograHeight;
    private boolean ngMog,upMog,attackMogra,coolTime,mogAnimation,goldMogra;

    public MogUpDnCanvas(Context context, ShowMsgCanvas cnvShowMsgCanvas,MogNumAdmin canMogNumAdmin,HummerCanvas conHummerCanvas){
        super(context);

        paint = new Paint();

        hummerCanvas = conHummerCanvas;

        Bitmap mograBit  = BitmapFactory.decodeResource(getResources(), R.drawable.moguratataki);
        Bitmap goldMograBit = BitmapFactory.decodeResource(getResources(), R.drawable.gold_mogura);
        Bitmap hiyokoBit = BitmapFactory.decodeResource(getResources(), R.drawable.hiyoko_mog);
        Bitmap tenregBit = BitmapFactory.decodeResource(getResources(), R.drawable.tenreg);
        Bitmap lemmingBit = BitmapFactory.decodeResource(getResources(), R.drawable.lemming);

        int mogHeight,mogWidth,GmogHeight,GMogWidth,piyoHeight,piyoWidth
                ,tenHeight,tenWidth,lemHeight,lemWidth;
        mogHeight  = mograBit.getHeight();
        mogWidth   = mograBit.getWidth();
        GmogHeight = goldMograBit.getHeight();
        GMogWidth  = goldMograBit.getWidth();
        piyoHeight = hiyokoBit.getHeight();
        piyoWidth  = hiyokoBit.getWidth();
        tenHeight  = tenregBit.getHeight();
        tenWidth   = tenregBit.getWidth();
        lemHeight  = lemmingBit.getHeight();
        lemWidth   = lemmingBit.getWidth();

        mograHeight = mogHeight / 8;
        //Log.d("MogSiz","Height:" + mogHeight + "\nmogWidth:" + mogWidth);
        smallMograBit   = Bitmap.createScaledBitmap(mograBit  , mogWidth  / 8,  mogHeight  / 8, true);
        smallGoldMograBit = Bitmap.createScaledBitmap(goldMograBit, GMogWidth / 8, GmogHeight / 8, true);
        smallHiyokoBit  = Bitmap.createScaledBitmap(hiyokoBit , piyoWidth / 8,  piyoHeight / 8, true);
        smallTenregBit  = Bitmap.createScaledBitmap(tenregBit , tenWidth  / 4,  tenHeight  / 4, true);
        smallLemmingBit = Bitmap.createScaledBitmap(lemmingBit, lemWidth  / 5,  lemHeight  / 5, true);
        bigMograBit     = Bitmap.createScaledBitmap(mograBit  , mogWidth  / 4,  mogHeight  / 8, true);
        outThingBit     = smallMograBit;

        mogNumAdmin = canMogNumAdmin;
        showMsgCanvas = cnvShowMsgCanvas;
        mogAni = new ObjectAnimator();


        leftMog = 20;
        topMog = 550;

        coolTime = false;
        upMog = false;
        attackMogra = false;
        goldMogra = false;
    }

    @Override
    public void onDraw(Canvas canvas){
        //Log.d("MogMoveCanvas", "drawMogra");
        canvas.drawBitmap(outThingBit,leftMog,topMog, paint);
//        canvas.drawBitmap(smallMograBit,20,550, paint);

    }

    public void setMogPoint(int mogRow, int mogCol){
        //Log.d("MogMoveCanvas","mogPointMorai");
        leftMog = 360;
        if(mogRow == 1){
            topMog = 550;
        }else if(mogRow == 2){
            topMog = 820;
        }else{
            topMog = 1070;
        }
        if(mogCol == 1){
            leftMog = 50;
        }else if(mogCol == 2){
            leftMog = 400;
        }else{
            leftMog = 730;
        }
        showMsgCanvas.setMsgPoint(leftMog + 200, topMog);
        hummerCanvas.setHumPoint(leftMog, topMog, mogCol, mogRow);

        //Log.d("mogMoveCanvas","topMog:" + topMog + "\nleftMog:" + leftMog);
        invalidate();
    }

    public void mogXYPoint(float x, float y){
        //Log.d("mogMoveCanvas","getXY");
        leftMog = x;
        topMog  = y;

        invalidate();
    }

    @SuppressLint("ObjectAnimatorBinding")
    public boolean moveMogAni(String type, boolean mogTap, boolean piyoCheck){
        if(mogAni.isRunning()){
            Log.d("moveMogAni","\nアニメーション中！");
            return false;
        }
        //Log.d("moveMogAni","\nアニメーション開始！");

        int sendScore = 0;
        int sendHiyoko = 0;
        float fromY = 90.0f;
        float toY   = 90.0f;

        if(type == "up"){
            if(upMog){ return false; }
            attackMogra = false;
            fromY = 200.0f;
        }else{
            if(!upMog){ return false; }
            attackMogra = true;
            toY = 200.0f;
        }
        if(mogTap){
            //Log.d("moveMogAni","ngMog:" + ngMog);
            if(ngMog){
                if(goldMogra){
                    sendScore = 50;
                    sendHiyoko = 0;
                    showMsgCanvas.msgType(1);
                }else{
                    sendScore = -10;
                    sendHiyoko = 10;
                    mogNumAdmin.mograCountAddOrCut(1);
                    showMsgCanvas.msgType(0);
                }
            }else{
                sendScore = 10;
                sendHiyoko = 0;
                showMsgCanvas.msgType(1);
            }
        }
        mogAni = ObjectAnimator.ofFloat(this, "translationY", fromY,toY);
        mogAni.setDuration(300);
        mogAni.start();

        mogNumAdmin.scoreAddOrCut(sendScore);
        mogNumAdmin.attackHiyokoCountAddOrCut(sendHiyoko);
        if(type == "up"){ upMog = true; }
        else{
            upMog = false;
            goldMogra = false;
        }
        coolTime = true;

        return true;
    }

    public void piyokana(boolean piyoinMog)
    {
        // モグラ以外を表示するか
        //Log.d("moveMog","\npiyoinMog:" + piyoinMog);
        ngMog = piyoinMog;
        if(ngMog){
            //ステージ3以上のときに他のどうぶつも出す
            Random kindOfMog = new Random();
            int rareInt = kindOfMog.nextInt(100);
            if(rareInt < 5){
                outThingBit = smallGoldMograBit;
                mogNumAdmin.hiyokoCountAddOrCut(-1);
                mogNumAdmin.mograCountAddOrCut(5);
                goldMogra = true;
            }else{
                int random = 0;
                if(mogNumAdmin.stage < 3){ random = kindOfMog.nextInt(1); }
                else if(mogNumAdmin.stage == 3){ random = kindOfMog.nextInt(2); }
                else{ random = kindOfMog.nextInt(3); }
                switch (random) {
                    case 0:
                        outThingBit = smallHiyokoBit;
                        break;
                    case 1:
                        outThingBit = smallLemmingBit;
                        break;
                    case 2:
                        outThingBit = smallTenregBit;
                        break;
                    default:
                        outThingBit = smallHiyokoBit;
                        break;
                }
            }
        }else{
            outThingBit = smallMograBit;
        }

        invalidate();
    }

    public void animCancel(){ mogAni.cancel(); }

    public void coolTimeFlag(boolean flag){ coolTime = flag; }

    public boolean checkCoolTImeFlag(){ return coolTime; }

    public void upMogFlag(boolean bolUpMog){ upMog = bolUpMog; }

    public boolean checkUpMogFlag(){ return upMog; }

    public void attackFlag(boolean attackMog){ attackMogra = attackMog; }

    public boolean checkAttackFlag(){ return attackMogra; }

    public int checkMograHeight(){ return mograHeight; }
}
