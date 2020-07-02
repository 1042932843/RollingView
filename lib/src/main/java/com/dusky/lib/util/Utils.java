package com.dusky.lib.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Utils {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    public static Bitmap loadBitmap(Context context, int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        options.inSampleSize = 1;//压缩采样率
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), resourceId, options);
    }
}
