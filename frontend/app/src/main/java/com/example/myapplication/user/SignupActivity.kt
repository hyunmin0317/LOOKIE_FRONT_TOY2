package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.user.User
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup)
        signup.setOnClickListener { register(this@SignupActivity) }
        login.setOnClickListener { startActivity(Intent(this@SignupActivity, LoginActivity::class.java)) }
    }

    fun register(activity: Activity) {
        val username = username_inputbox.text.toString()
        val password1 = password1_inputbox.text.toString()
        val password2 = password2_inputbox.text.toString()

        var signup = User(username=username, password=password1)

        if (password1 == password2) {
            (application as MasterApplication).service.signup(
                signup
            ).enqueue(object: Callback<Login> {
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        val username = user!!.username!!
                        val token = user!!.token!!
                        saveUserToken(username, token, activity)
                        (application as MasterApplication).createRetrofit()

                        Toast.makeText(activity, "가입에 성공하였습니다.", Toast.LENGTH_LONG).show()
                        startActivity(Intent(activity, TimerActivity::class.java))
                    } else {
                        Toast.makeText(activity, "사용할 수 없는 아이디입니다.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Toast.makeText(activity, "서버 오류", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(activity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
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