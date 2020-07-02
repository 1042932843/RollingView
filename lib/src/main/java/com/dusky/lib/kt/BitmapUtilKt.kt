package com.dusky.lib.kt

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color

/**
 * 群组头像处理工具
 */
class BitmapUtilKt {
    /**
     * @param size 生成后整张大小 传dp单位数值需要换算为px
     * @param divider 图片之间的分割宽度 传dp单位数值需要换算为px
     * @param dividerColor 图片分割颜色 Color.parseColor("#000000")
     * @param bitmaps 最大支持9图
     * @return
     */
    fun combineBitmap(
        size: Int,
        divider: Int,
        dividerColor: Int,
        bitmaps: Array<Bitmap?>
    ): Bitmap {
        val result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)//准备一张预期大小的画布
        if (dividerColor == 0) {
            canvas.drawColor(Color.WHITE)
        }else{
            canvas.drawColor(dividerColor)
        }
        val count = bitmaps.size
        if(count>9){
            throw UnsupportedOperationException("仅支持最大9张处理")
        }
        var subSize = 0
        //根据生成bitmap大小，计算单个大小
        when {
            count < 2 -> {
                subSize = size
            }
            count < 5 -> {
                subSize = (size - 3 * divider) / 2
            }
            count < 10 -> {
                subSize = (size - 4 * divider) / 3
            }
        }
        var subBitmap: Bitmap?
        for (i in 0 until count) {
            if (bitmaps[i] == null) {
                continue
            }
            subBitmap = Bitmap.createScaledBitmap(bitmaps[i]!!, subSize, subSize, true)
            var x = 0f
            var y = 0f
            when (count) {
                2 -> {
                    x = divider + i * (subSize + divider).toFloat()
                    y = (size - subSize) / 2.0f
                }
                3 -> {
                    when (i) {
                        0 -> {
                            x = (size - subSize) / 2.0f
                            y = divider.toFloat()
                        }
                        else -> {
                            x = divider + (i - 1) * (subSize + divider).toFloat()
                            y = subSize + 2 * divider.toFloat()
                        }
                    }
                }
                4 -> {
                    x = divider + i % 2 * (subSize + divider).toFloat()
                    y = if (i < 2) {
                        divider.toFloat()
                    } else {
                        subSize + 2 * divider.toFloat()
                    }
                }
                5 -> {
                    when {
                        i == 0 -> {
                            y = (size - 2 * subSize - divider) / 2.0f
                            x = y
                        }
                        i == 1 -> {
                            x = (size + divider) / 2.0f
                            y = (size - 2 * subSize - divider) / 2.0f
                        }
                        i > 1 -> {
                            x = divider + (i - 2) * (subSize + divider).toFloat()
                            y = (size + divider) / 2.0f
                        }
                    }
                }
                6 -> {
                    x = divider + i % 3 * (subSize + divider).toFloat()
                    y = if (i < 3) {
                        (size - 2 * subSize - divider) / 2.0f
                    } else {
                        (size + divider) / 2.0f
                    }
                }
                7 -> {
                    when {
                        i == 0 -> {
                            x = (size - subSize) / 2.0f
                            y = divider.toFloat()
                        }
                        i < 4 -> {
                            x = divider + (i - 1) * (subSize + divider).toFloat()
                            y = subSize + 2 * divider.toFloat()
                        }
                        else -> {
                            x = divider + (i - 4) * (subSize + divider).toFloat()
                            y = divider + 2 * (subSize + divider).toFloat()
                        }
                    }
                }
                8 -> {
                    when {
                        i == 0 -> {
                            x = (size - 2 * subSize - divider) / 2.0f
                            y = divider.toFloat()
                        }
                        i == 1 -> {
                            x = (size + divider) / 2.0f
                            y = divider.toFloat()
                        }
                        i < 5 -> {
                            x = divider + (i - 2) * (subSize + divider).toFloat()
                            y = subSize + 2 * divider.toFloat()
                        }
                        else -> {
                            x = divider + (i - 5) * (subSize + divider).toFloat()
                            y = divider + 2 * (subSize + divider).toFloat()
                        }
                    }
                }
                9 -> {
                    x = divider + i % 3 * (subSize + divider).toFloat()
                    y = when {
                        i < 3 -> {
                            divider.toFloat()
                        }
                        i < 6 -> {
                            subSize + 2 * divider.toFloat()
                        }
                        else -> {
                            divider + 2 * (subSize + divider).toFloat()
                        }
                    }
                }
            }
            canvas.drawBitmap(subBitmap, x, y, null)
        }
        return result
    }

}