package com.dusky.lib.java;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.dusky.lib.R;

/**
 *Created by dusky on 2020/06/30.
 */
public class RollingView extends View {
    private int speed = 1;
    private int width, height, initialState;
    private Bitmap bitmap;
    private int x = 0, j = 0;

    private int degrees = 0;
    private PaintFlagsDrawFilter filter;//canvas抗锯齿

    public RollingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RollingView, 0, 0);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.RollingView_initialState) {
                initialState = a.getInt(R.styleable.RollingView_initialState, 0);
            } else if (attr == R.styleable.RollingView_speed) {
                speed = a.getInt(R.styleable.RollingView_speed, 1);

            }
        }
        a.recycle();
        filter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setDegrees(int degrees) {
        if (degrees>90||degrees<0){
            throw new UnsupportedOperationException("degrees仅支持大于等于0度且小等于90度！");
        }
        this.degrees = degrees;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bitmap!=null){
            canvas.setDrawFilter(filter);
            if (degrees > 0&&degrees<90) {
                drawBitmapAngel(bitmap, canvas);
            }
            if(degrees==0||degrees==90){
                drawBitmap(bitmap, canvas);
            }

        }


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    public Bitmap loadBitmap(Context context, int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        options.inSampleSize = 1;//压缩采样率
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), resourceId, options);
    }


    /**
     * 水平或垂直无限滚动
     *
     * @param bitmap
     * @param canvas
     */
    private void drawBitmap(Bitmap bitmap, Canvas canvas) {
        canvas.rotate(degrees);
        if (x >= 0 && x <= bitmap.getWidth() - (int) getSmallWidth(bitmap, width, height)) {
            //第一阶段 单个图移动
            Rect mSrcRect = new Rect(x, 0, (int) getSmallWidth(bitmap, width, height) + x, bitmap.getHeight());//截取图片
            Rect mDestRect = new Rect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight()); //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null);
            x = x + speed;
            invalidate();
        } else if (x > bitmap.getWidth() - (int) getSmallWidth(bitmap, width, height) && x <= bitmap.getWidth()) {
            //第二阶段 一个出去 一个接上来
            Rect mSrcRect = new Rect(bitmap.getWidth() - (int) getSmallWidth(bitmap, width, height) + j, 0, (int) getSmallWidth(bitmap, width, height) + bitmap.getWidth() - (int) getSmallWidth(bitmap, width, height), bitmap.getHeight());//截取图片
            Rect mDestRect = new Rect(0, 0, (int) getMinWidth((int) getSmallWidth(bitmap, width, height) - j, bitmap.getHeight(), this.getMeasuredHeight()), this.getMeasuredHeight()); //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null);

            //同时这里有一张图片接上
            Rect mSrcRect2 = new Rect(0, 0, j, bitmap.getHeight());//截取图片
            Rect mDestRect2 = new Rect(mDestRect.right, 0, getMeasuredWidth(), getMeasuredHeight()); //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect2, mDestRect2, null);

            x = x + speed;
            j = j + speed;
            invalidate();
        } else {
            //回到第一阶段 这里要补一帧 防止闪烁
            x = 0;
            j = 0;

            Rect mSrcRect = new Rect(x, 0, (int) getSmallWidth(bitmap, width, height) + x, bitmap.getHeight());//截取图片
            Rect mDestRect = new Rect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight()); //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null);
            invalidate();
        }

    }

    /**
     * @param bitmap
     * @param canvas
     */
    private void drawBitmapAngel(Bitmap bitmap, Canvas canvas) {
        if (degrees != 0) {
            canvas.rotate(degrees);

            canvas.translate(0,-getB(degrees));
            if (x >= 0 && x <= (bitmap.getWidth() - (int) getSmallWidth(bitmap, width, height))) {
                //第一阶段 单个图移动
                Rect mSrcRect = new Rect(x, 0, (int) getSmallWidth(bitmap, width, height) + x, bitmap.getHeight());//截取图片
                Rect mDestRect = new Rect(0, 0, 2*this.width, 2*this.height); //放到控件中
                canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null);
                x = x + speed;
                invalidate();
            } else if (x > bitmap.getWidth() - (int) getSmallWidth(bitmap, width, height) && x <= bitmap.getWidth()) {
                //第二阶段 一个出去 一个接上来
                Rect mSrcRect = new Rect(bitmap.getWidth() - (int) getSmallWidth(bitmap, width, height) + j, 0, (int) getSmallWidth(bitmap, width, height) + bitmap.getWidth() - (int) getSmallWidth(bitmap, width, height), bitmap.getHeight());//截取图片
                Rect mDestRect = new Rect(0, 0, (int) getMinWidth((int) getSmallWidth(bitmap, width, height) - j, bitmap.getHeight(), 2*this.height), 2*this.height); //放到控件中
                canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null);

                //同时这里有一张图片接上
                Rect mSrcRect2 = new Rect(0, 0, j, bitmap.getHeight());//截取图片
                Rect mDestRect2 = new Rect(mDestRect.right, 0, 2*this.width, 2*this.height); //放到控件中
                canvas.drawBitmap(bitmap, mSrcRect2, mDestRect2, null);

                x = x + speed;
                j = j + speed;
                invalidate();
            } else {
                //回到第一阶段 这里要补一帧 防止闪烁
                x = 0;
                j = 0;

                Rect mSrcRect = new Rect(x, 0, (int) getSmallWidth(bitmap, width, height) + x, bitmap.getHeight());//截取图片
                Rect mDestRect = new Rect(0, 0, 2*this.width, 2*this.height); //放到控件中
                canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null);
                invalidate();
            }
        }

    }



    private int getA(float degrees) {
        return (int) (Math.cos((degrees) / 180f * Math.PI) * this.getMeasuredWidth());
    }

    private int getB(float degrees) {
        return (int) (Math.sin((degrees) / 180f * Math.PI) * this.getMeasuredWidth());
    }

    public double getMaxWidth(int width, int height){
        return Math.sqrt(width*width+width*height);

    }

    public double getMaxHeight(int maxWidth,int width, int height){
        float c=(float) width/(float)height;
        return maxWidth/c;

    }

    /**
     * 按控件宽高比例算出的图片显示宽度，因为横向滚动高是等于控件高度的
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    private float getSmallWidth(Bitmap bitmap, int width, int height) {
        return ((float) bitmap.getHeight()) * ((float) width) / ((float) height);
    }

    private float getMinWidth(int mixW, int mixH, int height) {
        return ((float) mixW) * ((float) height) / ((float) mixH);
    }
}
