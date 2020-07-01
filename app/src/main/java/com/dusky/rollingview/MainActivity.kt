package com.dusky.rollingview

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView() {

        //本项目不提供图片合成功能
        rollingView_java.setBitmap(rollingView_java.loadBitmap(this, R.drawable.test))
        rollingView_java.setDegrees(30)

        rollingView.bitmap = rollingView.loadBitmap(this, R.drawable.test)
        rollingView.degrees = 30f
        num.text = "当前倾斜角度：" + 30
        seekBar.max = 52//在164dp宽，200dp高的情况下倾斜超过52度图片无法盖住控件，需要更好的实现请自行调整canvas原点位置和绘制的Rect大小。
        seekBar.progress = 30
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                rollingView.degrees = p1.toFloat()
                rollingView_java.setDegrees(p1)
                num.text = "当前倾斜角度：$p1"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        seekbar_speed.max = 10
        seekbar_speed.progress = 1
        speed.text = "当前速度：" + 1
        seekbar_speed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                rollingView.speed = p1
                rollingView_java.setSpeed(p1)
                speed.text = "当前速度：$p1"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
    }
}
