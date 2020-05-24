package com.example.mogratatakigame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

//画面全体の演出Canvas
public class MograGameCanvas extends View {

    private Paint paint;
    private int showScore;

    public MograGameCanvas(Context context){
        super(context);

        paint = new Paint();

        showScore = 0;

    }

    @Override
    public void onDraw(Canvas canvas){

    }

}
