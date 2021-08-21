package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup)
        signup.setOnClickListener { register(this@SignupActivity) }
    }

    fun register(activity: Activity) {
        val username = email_inputbox.text.toString()
        val password1 = password1_inputbox.text.toString()
        val password2 = password2_inputbox.text.toString()

        (application as MasterApplication).service.register(
            username, password1, password2
        ).enqueue(object :
            Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "가입에 성공하였습니다.", Toast.LENGTH_LONG).show()
                    val user = response.body()
                    val token = user!!.token!!
                    saveUserToken(username, token, activity)
                    (application as MasterApplication).createRetrofit()
                    activity.startActivity(Intent(activity, MypageActivity::class.java))
                } else {
                    if (password1!=password2) {
                        Toast.makeText(activity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(activity, "사용할 수 없는 아이디입니다.", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(activity, "서버 오류", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun saveUserToken(email: String, token: String, activity: Activity) {
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("email", email)
        editor.putString("token", token)
        editor.commit()
    }
}