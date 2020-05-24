package com.example.mogratatakigame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class PointTestCanvas extends View {

    private Paint paint;
    private int lineCounting;
    private boolean first;
    private float[][] line;

    public PointTestCanvas(Context context){
        super(context);

        paint = new Paint();
        paint.setStrokeWidth(10);
        line = new float[10][];
        first = true;
    }

    @Override
    public void onDraw(Canvas canvas){
        if(!first) {
            for(int i = 0; i < lineCounting; i++) {
                final float startX = line[i][0];
                final float startY = line[i][1];
                final float endX   = line[i][2];
                final float endY   = line[i][3];
                canvas.drawLine(startX, startY, endX, endY, paint);
            }
        }
    }

    public void addLineInfo(float[][] settingLine, int lineCount){
        first = false;
        if(settingLine.length <= 10) {
            line = settingLine;
            lineCounting = lineCount;
            invalidate();
        }
    }
}
