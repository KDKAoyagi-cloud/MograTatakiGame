package com.example.mogratatakigame;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MogNumAdmin extends AppCompatActivity {

    private int sendScore,attackHiyoko,mograCount,hiyokoCount;
    private float screenDensity;
    public int stage;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        //作成時動作
        sendScore    = 0;
        attackHiyoko = 0;
        mograCount   = 0;
        hiyokoCount  = 0;
        stage        = 1;

    }

    //スコア
    public void scoreAddOrCut(int scoreNum){
        if(scoreNum < 0 && sendScore == 0){
            //スコアをマイナスにしないため何もしない
        }else{
            sendScore += scoreNum;
        }
    }

    public void scoreReset(){ sendScore = 0;}

    public int scoreCheck(){
        //Log.d("ScoreAdmin","Score:" + sendScore);
        return sendScore;
    }

    //アタックひよこさんカウント
    public void attackHiyokoCountAddOrCut(int hiyokoAttackCount){ attackHiyoko += hiyokoAttackCount; }

    public void attackHiyokoReset(){ attackHiyoko = 0; }

    public int checkAttackHiyoko(){ return attackHiyoko; }

    //出現もぐらさんカウント
    public void mograCountAddOrCut(int mograCountint){ mograCount += mograCountint; }

    public void mograCountReset(){ mograCount = 0; }

    public int mograCountCheck(){ return mograCount; }

    //出現ひよこさんカウント
    public void hiyokoCountAddOrCut(int hiyokoCountint){ hiyokoCount += hiyokoCountint; }

    public void hiyokoCountReset(){ hiyokoCount = 0; }

    public int hiyokoCountCheck(){ return hiyokoCount; }

    public void setDensity(float dispMetrics){ screenDensity = dispMetrics; }

    public int dpToPx(int px)
    {
        float d = screenDensity;
        return (int)((px * d) + 0.5);
    }


}
