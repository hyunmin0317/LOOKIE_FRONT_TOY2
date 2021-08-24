package com.example.myapplication.timer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity() {

    var timeThread: Thread? = null
    var isRunning: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)


        btn_start.setOnClickListener {
            btn_stop.visibility = View.VISIBLE
            btn_record.visibility = View.VISIBLE
            btn_pause.visibility = View.VISIBLE

            timeThread = Thread(timeThread)
            timeThread!!.start()
        }

        btn_stop.setOnClickListener {
            btn_stop.visibility = View.GONE
            btn_record.visibility = View.VISIBLE
            btn_pause.visibility = View.GONE
            recordView.text = ""
            timeThread?.interrupt()
        }

        btn_record.setOnClickListener {
            recordView.setText(recordView.text.toString() + timeView.text.toString() + "\n")
        }

        btn_pause.setOnClickListener {
            isRunning = !isRunning
            if (isRunning)
                btn_pause.setText("일시정지")
            else
                btn_pause.setText("시작")
        }

        val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                val mSec = msg.arg1 % 100
                val sec = msg.arg1 / 100 % 60
                val min = msg.arg1 / 100 / 60
                val hour = msg.arg1 / 100 / 360
                //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간
                @SuppressLint("DefaultLocale") val result =
                    String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec)
                if (result == "00:01:15:00") {
                    Toast.makeText(this@TimerActivity, "1분 15초가 지났습니다.", Toast.LENGTH_SHORT).show()
                }
                timeView.setText(result)
            }
        }

        class timeThread : Runnable {
            override fun run() {
                var i = 0
                while (true) {
                    while (isRunning) { //일시정지를 누르면 멈춤
                        val msg = Message()
                        msg.arg1 = i++
                        handler.sendMessage(msg)
                        try {
                            Thread.sleep(10)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                            runOnUiThread {
                                timeView.setText("")
                                timeView.setText("00:00:00:00")
                            }
                            return  // 인터럽트 받을 경우 return
                        }
                    }
                }
            }
        }




    }
}