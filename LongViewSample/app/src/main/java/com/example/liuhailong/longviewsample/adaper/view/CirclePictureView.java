package com.example.liuhailong.longviewsample.adaper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.example.liuhailong.longviewsample.R;

/**
 * Created by liuhailong on 16/9/6.
 */
public class CirclePictureView extends View {


    private Bitmap bmpDog;

    private Bitmap circleMaskBitMap;
    private Paint paint;
    private Canvas drawCircleCanvas;


    public CirclePictureView(Context context) {
        super(context);
    }

    public CirclePictureView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //设置画笔的风格
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bmpDog= BitmapFactory.decodeResource(getResources(), R.mipmap.pandar);
        //获取图片的宽度
        int width=Math.min(bmpDog.getHeight(),bmpDog.getWidth());
        //创建一个能画圆形小狗的bitMap
        circleMaskBitMap=Bitmap.createBitmap(width,width, Bitmap.Config.ARGB_8888);

        drawCircleCanvas=new Canvas(circleMaskBitMap);
        //设置圆心和半径
        int r=width/2;
        drawCircleCanvas.drawCircle(r,r,r,paint);
    }

    public CirclePictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
//        canvas.drawBitmap(bmpDog,0,0,null);
        int circieWidth=circleMaskBitMap.getWidth();
        //建立图层
        int layer=canvas.saveLayer(0,0,circieWidth,circieWidth,null,Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(bmpDog,0,0,null);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        canvas.drawBitmap(circleMaskBitMap,0,0,paint);

        canvas.restoreToCount(layer);
    }
}
