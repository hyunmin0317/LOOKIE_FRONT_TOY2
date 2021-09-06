package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.user.Login
import com.example.myapplication.user.Signup
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
        val email = email_inputbox.text.toString()
        val password1 = password1_inputbox.text.toString()
        val password2 = password2_inputbox.text.toString()
        val username = username_inputbox.text.toString()

        var signup = Signup(email=email, password=password1, username=username)

        if (password1 == password2) {
            (application as MasterApplication).service.register(
                signup
            ).enqueue(object: Callback<Signup> {
                override fun onResponse(call: Call<Signup>, response: Response<Signup>) {
                    if (response.isSuccessful) {
                        Toast.makeText(activity, "가입에 성공하였습니다.", Toast.LENGTH_LONG).show()

//                        // 로그인
//                        var login = Login(email = email, password = password1)
//                        (application as MasterApplication).service.login(login).enqueue(object : Callback<User> {
//
//                            override fun onResponse(call: Call<User>, response: Response<User>) {
//                                if (response.isSuccessful) {
//                                    val user = response.body()
//                                    val token = user!!.token!!
//                                    saveUserToken(email, token, activity)
//                                    (application as MasterApplication).createRetrofit()
//                                    Toast.makeText(activity, "로그인 하셨습니다.", Toast.LENGTH_SHORT).show()
//                                    startActivity(Intent(activity, TimerActivity::class.java))
//                                }
//                            }
//
//                            override fun onFailure(call: Call<User>, t: Throwable) {
//                                Toast.makeText(activity, "서버 오류", Toast.LENGTH_SHORT).show()
//                            }
//                        })

                    } else {
                        Toast.makeText(activity, "사용할 수 없는 이메일입니다.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Signup>, t: Throwable) {
                    Toast.makeText(activity, "서버 오류", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(activity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
        }
    }

    fun saveUserToken(email: String, token: String, activity: Activity) {
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("email", email)
        editor.putString("token", token)
        editor.commit()
    }
}