package com.leesb.surfacestarcraft;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;


public class Dubble extends View {


    public Dubble(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas buffCanvas = new Canvas(bitmap);

        for (int i=0 ; i<100 ; i++)
        {
            buffCanvas.drawRect(i*10, i*10, i*10, i*10, paint);
            buffCanvas.drawCircle(1000-(i*10), 1000-(i*10), 30, paint);
        }

        /*  무언가를 계속 그리는데 메인캔버스에 그리기 함수를 호출하는 것은
            즉시 화면에 반영되므로 그리는 과정이 보여질 수 밖에 없다
            그러므로 메모리상의 비트맵에서만 그리기 작업을 하는것이다
        */


        canvas.drawBitmap(bitmap, 0, 0, null);
        //모든 그리기작업이 끝나면 그때서야 그렸던 비트맵을 메인 캔버스에 그린다
        //즉, 도중에 그려지는 작업이 화면에 표시되지 않기위해 더블버퍼링을 하는 것
    }
}
