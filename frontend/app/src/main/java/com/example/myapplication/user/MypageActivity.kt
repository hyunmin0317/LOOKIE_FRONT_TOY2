package com.example.myapplication.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_mypage.*

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
    }

    fun getUserEmail(): String? {
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val email = sp.getString("email", "null")
        if (email == "null") return null
        else return email
    }
}