package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_mypage.*
import kotlinx.android.synthetic.main.activity_mypage.home
import kotlinx.android.synthetic.main.activity_mypage.mypage
import kotlinx.android.synthetic.main.activity_mypage.todo
import kotlinx.android.synthetic.main.activity_timer.*

class MypageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        logout.setOnClickListener {
            val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("email", "null")
            editor.putString("token", "null")
            editor.commit()
            (application as MasterApplication).createRetrofit()
            finish()
            startActivity(Intent(this, IntroActivity::class.java))
        }

        val email = getUserEmail()
        if (email != null)
            user_email.setText(""+email)

        todo.setOnClickListener { startActivity(Intent(this@MypageActivity, Todo_main::class.java)) }
        home.setOnClickListener { startActivity(Intent(this@MypageActivity, TimerActivity::class.java)) }
    }

    fun getUserEmail(): String? {
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val email = sp.getString("email", "null")
        if (email == "null") return null
        else return email
    }
}