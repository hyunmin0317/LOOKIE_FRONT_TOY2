package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.todo.Todo_main
import kotlinx.android.synthetic.main.mypage.*

class MypageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage)

        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        var email = sp.getString("email", "null")
        user_email.setText(email)

        //todo list 화면으로 이동
        todolist_button.setOnClickListener{
            startActivity(Intent(this, Todo_main::class.java))
            finish()
        }
        //홈 화면으로 이동
        home_button.setOnClickListener{
            startActivity(Intent(this, TimerActivity::class.java))
            finish()
        }

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
    }
}