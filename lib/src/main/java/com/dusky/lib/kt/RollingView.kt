package com.dusky.lib.kt

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.dusky.lib.R
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by dusky on 2020/06/30.
 */
class RollingView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    var speed = 1
    private var mwidth = 0
    private var mheight = 0
    private var initialState = 0
    var bitmap: Bitmap? = null
    private var x = 0
    private var j = 0
    var degrees = 0f
    private val filter //canvas抗锯齿
            : PaintFlagsDrawFilter

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(bitmap!=null){
            canvas.drawFilter = filter
            if(degrees==0f){
                drawBitmapHorizontal(bitmap, canvas)
            }else{
                drawBitmapAngel(bitmap, canvas)
            }
        }


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mwidth = w
        mheight = h
    }

     fun loadBitmap(context: Context, resourceId: Int): Bitmap {
        val options = BitmapFactory.Options()
        BitmapFactory.decodeResource(context.resources, resourceId, options)
        options.inSampleSize = 1 //压缩采样率
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(context.resources, resourceId, options)
    }

    /**
     * 水平无限滚动
     *
     * @param bitmap
     * @param canvas
     */
    private fun drawBitmapHorizontal(
        bitmap: Bitmap?,
        canvas: Canvas
    ) {
        if (x >= 0 && x <= bitmap!!.width - getSmallWidth(
                bitmap,
                mwidth,
                mheight
            ).toInt()
        ) { //第一阶段 单个图移动
            val mSrcRect = Rect(
                x,
                0,
                getSmallWidth(bitmap, mwidth, mheight).toInt() + x,
                bitmap.height
            ) //截取图片
            val mDestRect = Rect(
                0,
                0,
                this.measuredWidth,
                this.measuredHeight
            ) //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null)
            x += speed
            invalidate()
        } else if (x > bitmap!!.width - getSmallWidth(
                bitmap,
                mwidth,
                mheight
            ).toInt() && x <= bitmap.width
        ) { //第二阶段 一个出去 一个接上来
            val mSrcRect = Rect(
                bitmap.width - getSmallWidth(
                    bitmap,
                    mwidth,
                    mheight
                ).toInt() + j,
                0,
                getSmallWidth(bitmap, mwidth, mheight).toInt() + bitmap.width - getSmallWidth(
                    bitmap,
                    mwidth,
                    mheight
                ).toInt(),
                bitmap.height
            ) //截取图片
            val mDestRect = Rect(
                0,
                0,
                getMinWidth(
                    getSmallWidth(bitmap, mwidth, mheight).toInt() - j,
                    bitmap.height,
                    this.measuredHeight
                ).toInt(),
                this.measuredHeight
            ) //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null)
            //同时这里有一张图片接上
            val mSrcRect2 =
                Rect(0, 0, j, bitmap.height) //截取图片
            val mDestRect2 = Rect(
                mDestRect.right,
                0,
                this.measuredWidth,
                this.measuredHeight
            ) //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect2, mDestRect2, null)
            x += speed
            j += speed
            invalidate()
        } else { //回到第一阶段 这里要补一帧 防止闪烁
            x = 0
            j = 0
            val mSrcRect = Rect(
                x,
                0,
                getSmallWidth(bitmap, mwidth, mheight).toInt() + x,
                bitmap.height
            ) //截取图片
            val mDestRect = Rect(
                0,
                0,
                this.measuredWidth,
                this.measuredHeight
            ) //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null)
            invalidate()
        }
    }

    /**
     * @param bitmap
     * @param canvas
     */
    private fun drawBitmapAngel(bitmap: Bitmap?, canvas: Canvas) {
        if (degrees > 90 || degrees < 0) {
            throw UnsupportedOperationException("degrees仅支持大于0度且小于90度！")
        }
        canvas.rotate(degrees)
        canvas.translate(0f, (-getHY(degrees)).toFloat())
        if (x >= 0 && x <= bitmap!!.width - getSmallWidth(
                bitmap,
                mwidth,
                mheight
            ).toInt()
        ) { //第一阶段 单个图移动
            val mSrcRect = Rect(
                x,
                0,
                getSmallWidth(bitmap, mwidth, mheight).toInt() + x,
                bitmap.height
            ) //截取图片
            val mDestRect = Rect(
                0,
                0,
                this.measuredWidth * 2,
                this.measuredHeight * 2
            ) //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null)
            x += speed
            invalidate()
        } else if (x > bitmap!!.width - getSmallWidth(
                bitmap,
                width,
                height
            ).toInt() && x <= bitmap.width
        ) { //第二阶段 一个出去 一个接上来
            val mSrcRect = Rect(
                bitmap.width - getSmallWidth(
                    bitmap,
                    mwidth,
                    mheight
                ).toInt() + j,
                0,
                getSmallWidth(bitmap, mwidth, mheight).toInt() + bitmap.width - getSmallWidth(
                    bitmap,
                    mwidth,
                    mheight
                ).toInt(),
                bitmap.height
            ) //截取图片
            val mDestRect = Rect(
                0,
                0,
                getMinWidth(
                    getSmallWidth(bitmap, width, height).toInt() - j,
                    bitmap.height,
                    2 * this.measuredHeight
                ).toInt(),
                2 * this.measuredHeight
            ) //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null)
            //同时这里有一张图片接上
            val mSrcRect2 =
                Rect(0, 0, j, bitmap.height) //截取图片
            val mDestRect2 = Rect(
                mDestRect.right,
                0,
                2 * measuredWidth,
                2 * measuredHeight
            ) //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect2, mDestRect2, null)
            x += speed
            j += speed
            invalidate()
        } else { //回到第一阶段 这里要补一帧 防止闪烁
            x = 0
            j = 0
            val mSrcRect = Rect(
                x,
                0,
                getSmallWidth(bitmap, mwidth, mheight).toInt() + x,
                bitmap.height
            ) //截取图片
            val mDestRect = Rect(
                0,
                0,
                2 * this.measuredWidth,
                2 * this.measuredHeight
            ) //放到控件中
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null)
            invalidate()
        }
    }

    private fun getHY(degrees: Float): Int {
        return (cos(degrees / 180f * Math.PI) * mheight).toInt()
    }

    private fun getHX(degrees: Float): Int {
        return (sin(degrees / 180f * Math.PI) * mheight).toInt()
    }

    /**
     * 按控件宽高比例算出的图片显示宽度，因为横向滚动高是等于控件高度的
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    private fun getSmallWidth(bitmap: Bitmap?, width: Int, height: Int): Float {
        return bitmap!!.height.toFloat() * width.toFloat() / height.toFloat()
    }

    private fun getMinWidth(mixW: Int, mixH: Int, height: Int): Float {
        return mixW.toFloat() * height.toFloat() / mixH.toFloat()
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RollingView, 0, 0)
        for (i in 0 until a.indexCount) {
            when (a.getIndex(i)) {
                R.styleable.RollingView_initialState -> {
                    initialState = a.getInt(R.styleable.RollingView_initialState, 0)
                }
                R.styleable.RollingView_speed -> {
                    speed = a.getInt(R.styleable.RollingView_speed, 1)
                }
                R.styleable.RollingView_degrees -> {
                    degrees = a.getDimension(R.styleable.RollingView_degrees, 0f)
                }
            }
        }
        a.recycle()
        filter = PaintFlagsDrawFilter(
            0,
            Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
        )
    }
}