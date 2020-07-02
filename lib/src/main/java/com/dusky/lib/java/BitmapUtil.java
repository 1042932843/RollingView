package com.dusky.lib.java;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * 群组头像处理工具
 */
public class BitmapUtil  {
    /**
     * @param size 生成后整张大小 单位px
     * @param divider 图片之间的分割宽度 单位px
     * @param dividerColor 图片分割颜色 Color.parseColor("#000000")
     * @param bitmaps 最大支持9图
     * @return
     */
    public Bitmap combineBitmap(int size, int divider, int dividerColor, Bitmap[] bitmaps) {
        Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        int subSize=0;
        if (dividerColor == 0) {
            dividerColor = Color.WHITE;
        }
        canvas.drawColor(dividerColor);

        int count = bitmaps.length;

        //根据生成bitmap大小，计算单个大小
        if (count < 2) {
            subSize = size;
        } else if (count < 5) {
            subSize = (size - 3 * divider) / 2;
        } else if (count < 10) {
            subSize = (size - 4 * divider) / 3;
        }
        Bitmap subBitmap;

        for (int i = 0; i < count; i++) {
            if (bitmaps[i] == null) {
                continue;
            }
            subBitmap = Bitmap.createScaledBitmap(bitmaps[i], subSize, subSize, true);

            float x = 0;
            float y = 0;

            if (count == 2) {
                x = divider + i * (subSize + divider);
                y = (size - subSize) / 2.0f;
            } else if (count == 3) {
                if (i == 0) {
                    x = (size - subSize) / 2.0f;
                    y = divider;
                } else {
                    x = divider + (i - 1) * (subSize + divider);
                    y = subSize + 2 * divider;
                }
            } else if (count == 4) {
                x = divider + (i % 2) * (subSize + divider);
                if (i < 2) {
                    y = divider;
                } else {
                    y = subSize + 2 * divider;
                }
            } else if (count == 5) {
                if (i == 0) {
                    x = y = (size - 2 * subSize - divider) / 2.0f;
                } else if (i == 1) {
                    x = (size + divider) / 2.0f;
                    y = (size - 2 * subSize - divider) / 2.0f;
                } else if (i > 1) {
                    x = divider + (i - 2) * (subSize + divider);
                    y = (size + divider) / 2.0f;
                }
            } else if (count == 6) {
                x = divider + (i % 3) * (subSize + divider);
                if (i < 3) {
                    y = (size - 2 * subSize - divider) / 2.0f;
                } else {
                    y = (size + divider) / 2.0f;
                }
            } else if (count == 7) {
                if (i == 0) {
                    x = (size - subSize) / 2.0f;
                    y = divider;
                } else if (i < 4) {
                    x = divider + (i - 1) * (subSize + divider);
                    y = subSize + 2 * divider;
                } else {
                    x = divider + (i - 4) * (subSize + divider);
                    y = divider + 2 * (subSize + divider);
                }
            } else if (count == 8) {
                if (i == 0) {
                    x = (size - 2 * subSize - divider) / 2.0f;
                    y = divider;
                } else if (i == 1) {
                    x = (size + divider) / 2.0f;
                    y = divider;
                } else if (i < 5) {
                    x = divider + (i - 2) * (subSize + divider);
                    y = subSize + 2 * divider;
                } else {
                    x = divider + (i - 5) * (subSize + divider);
                    y = divider + 2 * (subSize + divider);
                }
            } else if (count == 9) {
                x = divider + (i % 3) * (subSize + divider);
                if (i < 3) {
                    y = divider;
                } else if (i < 6) {
                    y = subSize + 2 * divider;
                } else {
                    y = divider + 2 * (subSize + divider);
                }
            }
            canvas.drawBitmap(subBitmap, x, y, null);
        }
        return result;
    }
}
