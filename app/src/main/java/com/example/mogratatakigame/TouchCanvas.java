package com.example.mogratatakigame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class TouchCanvas extends View {

    private Paint paint;
    private float touchX,touchY;

    public TouchCanvas(Context context){
        super(context);

        paint = new Paint();
        paint.setStrokeWidth(20);
        paint.setTextSize(50);
        paint.setColor(Color.RED);

        touchX = 0;
        touchY = 0;
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawPoint(touchX, touchY, paint);
        canvas.drawText("touchX:" + touchX, touchX + 100, touchY, paint);
        canvas.drawText("touchY:" + touchY, touchX + 100, touchY + 50, paint);
    }

    public void canvasTouch(float x, float y){
        touchX = x;
        touchY = y;

        invalidate();
    }
}
