package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.example.myapplication.todo.Todo_main
import kotlinx.android.synthetic.main.activity_timer.*
import java.text.SimpleDateFormat
import java.util.*

class TimerActivity : AppCompatActivity() {
    // 핸들러사용
    val handler = Handler()
    var timeValue = 0
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        //todo list 화면으로 이동
        todolist_button.setOnClickListener{
            startActivity(Intent(this, Todo_main::class.java))
            finish()
        }
        //친구 화면으로 이동
        friend_button.setOnClickListener{
            startActivity(Intent(this, MypageActivity::class.java))
            finish()
        }

        var now = System.currentTimeMillis()
        var date = Date(now)
        var format = SimpleDateFormat("yyyy.MM.dd")
        var todayDate = format.format(date)
        var timeall = 0
        var strtime = "00:00:00"

        today.text = todayDate
        btn_stop.visibility = View.GONE

        //핸들러 - 1초마다 실행되게 함
        val runnable = object : Runnable {
            override fun run() {
                timeValue ++
                //TextView 업데이트 하기
                timeToText(timeValue)?.let {
                    time.text = it
                }
                handler.postDelayed(this, 1000)
            }
        }

        btn_start.setOnClickListener {
            it.visibility = View.GONE
            btn_stop.visibility = View.VISIBLE
            handler.post(runnable)
        }

        btn_stop.setOnClickListener {
            it.visibility = View.GONE
            btn_start.visibility = View.VISIBLE
            count++
            strtime = time.text.toString()
            recordView.text = recordView.text.toString()+ count.toString()+"회차 - " + strtime + "\n"
            time.text = ""
            timeall += timeValue
            timeValue = 0
            handler.removeCallbacks(runnable)
            timeToText()?.let {
                time.text = it
            }
            all_time.text = timeToText(timeall)
        }
    }

    private fun timeToText(time: Int = 0) : String{
        return if (time <= 0) {
            "00:00:00"
        } else {
            val h = time / 3600
            val m = time % 3600 / 60
            val s = time % 60
            "%1$02d:%2$02d:%3$02d".format(h, m, s)
        }
    }
}