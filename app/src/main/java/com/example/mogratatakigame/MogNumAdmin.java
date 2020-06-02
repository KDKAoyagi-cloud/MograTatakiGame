package com.example.mogratatakigame;

import android.media.SoundPool;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MogNumAdmin extends AppCompatActivity {

    private int sendScore,attackHiyoko,mograCount,hiyokoCount;
    private float screenDensity;
    private SoundPool soundPool;
    private int[] sounds;
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

        soundPool = null;
    }

    //スコア
    public void scoreAddOrCut(int scoreNum){
        if(scoreNum < 0 && sendScore == 0){
            //スコアをマイナスにしないため何もしない
        }else{
            sendScore += scoreNum;
        }
    }

    public void scoreAddOrCut(int scoreNum,int soundNum){
        /*
         * モード
         * 0 ボタン音
         * 1 中断
         * 2 クリア音
         * 3 ひよこ
         * 4 モグラ
         * 5 青い鳥(鳴声)
         * 6 青い鳥(紙音)
         * 7 死神(出現)
         * 8 死神(スワイプ)
         * 9 死神(消失)
         * 10 ゲームオーバー
         * 11 ゲームスタート
         */
        if(scoreNum < 0 && sendScore == 0){
            //スコアをマイナスにしないため何もしない
        }else{
            sendScore += scoreNum;
            if(soundPool != null){
                int playSound = 0;
                playSound = sounds[soundNum];
                if(playSound != 0){
                    soundPool.play(playSound,1.0f,1.0f,0,0,1);
                }
            }
        }
    }

    public void scoreAddOrCut(int scoreNum,int soundNum,int soundNum2){
        if(scoreNum < 0 && sendScore == 0){
            //スコアをマイナスにしないため何もしない
        }else{
            sendScore += scoreNum;
            if(soundPool != null){
                int playSound = 0;
                int playSound2 = 0;
                playSound = sounds[soundNum];
                playSound2 = sounds[soundNum2];
                if(playSound != 0 && playSound2 != 0){
                    soundPool.play(playSound,1.0f,1.0f,0,0,1);
                    soundPool.play(playSound2,1.0f,1.0f,0,0,1);
                }
            }
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

    public void setSound(SoundPool mograSoundPool, int[] gameSounds){
        soundPool = mograSoundPool;
        sounds = gameSounds;
    }
}
