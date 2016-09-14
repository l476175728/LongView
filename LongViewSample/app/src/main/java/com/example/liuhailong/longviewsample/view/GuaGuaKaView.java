package com.example.liuhailong.longviewsample.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.liuhailong.longviewsample.R;

import java.util.Random;

/**
 * Created by liuhailong on 16/9/7.
 */
public class GuaGuaKaView extends View {


    private Random rnd;

    private Paint paint;

    private Paint clearPaint;
    //随机生成中奖信息
    private static final String[] PRIZE = {
            "恭喜!您中了一等奖,奖金1亿元 ", " 恭喜!您中了二等奖,奖金500万元",
            "恭喜!您中了三等奖,奖金20万元", "很遗憾,您没有中奖,谢谢参与!"
    };
    //手指的宽度
    private static final int FINGER = 60;
    private Bitmap bmpBuffer;
    private Canvas cvsBuffer;
    private int curX, curY;

    private Context mContext;
    public GuaGuaKaView(Context context, AttributeSet attrs) { super(context, attrs);
        mContext=context;
        rnd = new Random();
        //写出来字体的画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(dp2px(30));
        paint.setColor(Color.WHITE);

        //清除上面的覆盖物的画笔
        clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置图像的装换模式 最关键
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        //设置画笔有圆弧的形状
       clearPaint.setStrokeJoin(Paint.Join.ROUND);
       clearPaint.setStrokeCap(Paint.Cap.ROUND);
        //设置画笔宽度
        clearPaint.setStrokeWidth(FINGER);
        drawBackground();

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//
        bmpBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        cvsBuffer = new Canvas(bmpBuffer);
        cvsBuffer.drawColor(Color.parseColor("#FF808080"));
    }
    /**
     *
     * @return    PRIZE     */
    private int getPrizeIndex(){

        return rnd.nextInt(PRIZE.length);
    }
    /**
     *  画出来中奖信息的背景 */
    private void drawBackground(){

        //背景图片的dialog
        Bitmap bmpBackground = BitmapFactory.decodeResource(
                getResources(), R.mipmap.pandar);
        //从资源文件中读出的的bitmap文件不可以修改 所以这里复制了一张可以修改的图片
        Bitmap bmpBackgroundMutable = bmpBackground.copy(Bitmap.Config.ARGB_8888, true);
        Canvas cvsBackground = new Canvas(bmpBackgroundMutable);

        String text = PRIZE[getPrizeIndex()];
        Rect rect = new Rect();
        //下面代码是将字画在图片上 返回text的宽度和高度
        paint.getTextBounds(text,  0, text.length(), rect);

        int x = (bmpBackgroundMutable.getWidth() - rect.width()) / 2;
        int y = (bmpBackgroundMutable.getHeight() - rect.height()) / 2;
        //设置字体的阴影部分
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
        paint.setShadowLayer(10, 0, 0, Color.GREEN);
        cvsBackground.drawText(text, x, y, paint);
        paint.setShadowLayer(0, 0, 0, Color.YELLOW);
        cvsBackground.drawText(text, x, y, paint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(new BitmapDrawable( getResources(), bmpBackgroundMutable));
        }
        else{
            this.setBackgroundDrawable(new BitmapDrawable(bmpBackgroundMutable));
        }

    }
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.drawBitmap(bmpBuffer, 0, 0, paint); }

    //手指滑动时重hui
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){
            //手指按下时记录当前的位置
            case MotionEvent.ACTION_DOWN:
                curX = x;
                curY = y;
                break;
            //移动的时候画出来移动的区域  讲移动的区域上面的图层去掉
            case MotionEvent.ACTION_MOVE:
                cvsBuffer.drawLine(curX, curY, x, y, clearPaint);
                invalidate();
                curX = x;
                curY = y;
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
            default:
                break;
        }
        return true;

    }


    //讲dp装换成px
    public  int dp2px(int dp) {

        float density =mContext.getResources().getDisplayMetrics().density;
        if (dp == 0) {
            return 0;
        }
        return (int) (dp * density + 0.5f);

    }

}
