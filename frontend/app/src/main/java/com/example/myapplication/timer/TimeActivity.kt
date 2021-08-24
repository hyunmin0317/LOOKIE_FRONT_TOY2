package com.example.myapplication.timer

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_time.*

class TimeActivity : AppCompatActivity() {

    // 핸들러사용
    val handler = Handler()
    var isRunning = true
    var timeValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        //핸들러 - 1초마다 실행되게 함
        val runnable = object : Runnable {
            override fun run() {
                timeValue ++
                //TextView 업데이트 하기
                timeToText(timeValue)?.let {

                    timeView.text = it
                }
                handler.postDelayed(this, 1000)
            }
        }

        btn_start.setOnClickListener {
            it.visibility = View.GONE
            btn_stop.visibility = View.VISIBLE
            btn_record.visibility = View.VISIBLE
            btn_pause.visibility = View.VISIBLE
            handler.post(runnable)
        }

        btn_stop.setOnClickListener {
            it.visibility = View.GONE
            btn_start.visibility = View.VISIBLE
            btn_record.visibility = View.GONE
            btn_pause.visibility = View.GONE
            recordView.text = ""
            handler.removeCallbacks(runnable)
            timeValue = 0
            timeToText()?.let {
                timeView.text = it
            }
        }

        btn_record.setOnClickListener {
            recordView.setText(recordView.text.toString() + timeView.text.toString() + "\n")
        }

        btn_pause.setOnClickListener {
            isRunning = !isRunning
            if (isRunning) {
                handler.post(runnable)
                btn_pause.setText("일시정지")
            } else {
                handler.removeCallbacks(runnable)
                btn_pause.setText("시작")
            }
        }
    }

    private fun timeToText(time: Int = 0) : String?{
        return if (time < 0) {
            null
        } else if (time == 0) {
            "00:00:00"
        } else {
            val h = time / 3600
            val m = time % 3600 / 60
            val s = time % 60
            "%1$02d:%2$02d:%3$02d".format(h, m, s)
        }
    }
}