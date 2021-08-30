package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_timer.*
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TimerActivity : AppCompatActivity() {
    // 핸들러사용
    val handler = Handler()
    var timeAll = 0
    var timeValue = 0
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        var now = System.currentTimeMillis()
        var date = Date(now)
        var format = SimpleDateFormat("yyyy.MM.dd")
        var todayDate = format.format(date)
        var strtime = "00:00:00"

        val sp = getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = sp.edit()

        var todaysp = sp.getString("date", "null")
        var timesp = sp.getString("time", "null")
        if (todaysp != "null") {
            if (todaysp != todayDate) {
                all_time.text = "00:00:00"
                editor.putString("date", todayDate)
                editor.commit()
            } else {
                if (timesp != "null") {
                    timeAll = timesp!!.toInt()
                }
            }
        }
        all_time.text = timeToText(timeAll)
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
            editor.putString("date", todayDate)
            editor.commit()
            it.visibility = View.GONE
            btn_stop.visibility = View.VISIBLE
            handler.post(runnable)

            (application as MasterApplication).service.status("true").enqueue(object : Callback<Timer> {
                override fun onResponse(call: Call<Timer>, response: Response<Timer>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@TimerActivity, "변경되었습니다.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@TimerActivity, "오류", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Timer>, t: Throwable) {
                    Toast.makeText(this@TimerActivity, "서버 오류", Toast.LENGTH_LONG).show()
                }
            })
        }

        btn_stop.setOnClickListener {
            it.visibility = View.GONE
            btn_start.visibility = View.VISIBLE
            count++
            recordView.text = recordView.text.toString()+ count.toString()+"회차: " + time.text.toString() + "\n"
            time.text = ""
            timeAll += timeValue
            timeValue = 0
            handler.removeCallbacks(runnable)
            timeToText()?.let {
                time.text = it
            }
            editor.putString("time", timeAll.toString())
            editor.commit()
            strtime = timeToText(timeAll)
            all_time.text = strtime
            var timer = Timer(time=strtime)

            (application as MasterApplication).service.timer(timer).enqueue(object : Callback<Timer> {
                override fun onResponse(call: Call<Timer>, response: Response<Timer>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@TimerActivity, "저장되었습니다.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@TimerActivity, strtime, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Timer>, t: Throwable) {
                    Toast.makeText(this@TimerActivity, "서버 오류", Toast.LENGTH_LONG).show()
                }
            })

            (application as MasterApplication).service.status("false").enqueue(object : Callback<Timer> {
                override fun onResponse(call: Call<Timer>, response: Response<Timer>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@TimerActivity, "변경되었습니다.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@TimerActivity, "오류", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Timer>, t: Throwable) {
                    Toast.makeText(this@TimerActivity, "서버 오류", Toast.LENGTH_LONG).show()
                }
            })
        }

        mypage.setOnClickListener { startActivity(Intent(this@TimerActivity, MypageActivity::class.java)) }
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