package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.*
import com.example.myapplication.user.User
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        setupListener(this@LoginActivity)
    }

    fun setupListener(activity: Activity) {
        signup.setOnClickListener { startActivity(Intent(activity, SignupActivity::class.java)) }

        login.setOnClickListener {
            val username = username_inputbox.text.toString()
            val password = password_inputbox.text.toString()

            var user = User(username = username, password = password)
            (application as MasterApplication).service.login(user).enqueue(object : Callback<Login> {

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        val token = user!!.token!!
                        saveUserToken(username, token, activity)
                        (application as MasterApplication).createRetrofit()

                        Toast.makeText(activity, "로그인 하셨습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(activity, TimerActivity::class.java))

                    } else {
                        Toast.makeText(activity, "가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Toast.makeText(activity, "서버 오류", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun saveUserToken(username: String, token: String, activity: Activity) {
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("email", username)
        editor.putString("token", token)
        editor.commit()
    }
}