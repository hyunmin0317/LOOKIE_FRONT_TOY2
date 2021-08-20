package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if ((application as MasterApplication).checkIsLogin()) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            setContentView(R.layout.activity_login)
            setupListener(this@LoginActivity)
        }
    }

    fun setupListener(activity: Activity) {
        signup.setOnClickListener { startActivity(Intent(activity, SignupActivity::class.java)) }

        login.setOnClickListener {
            val username = email_inputbox.text.toString()
            val password = password_inputbox.text.toString()
            (application as MasterApplication).service.login(
                username, password
            ).enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        val token = user!!.token!!
                        saveUserToken(token, activity)
                        (application as MasterApplication).createRetrofit()

                        Toast.makeText(
                            activity,
                            "로그인 하셨습니다", Toast.LENGTH_LONG
                        ).show()
                        startActivity(
                            Intent(activity, MainActivity::class.java)
                        )
                    }

                }
            })
        }
    }

    fun saveUserToken(token: String, activity: Activity) {
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("login_sp", token)
        editor.commit()
    }
}