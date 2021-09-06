package com.example.myapplication

import android.app.Application
import android.content.Context
import com.example.myapplication.friend.APIS
import com.example.myapplication.todo.APIS_todo
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MasterApplication : Application() {

    lateinit var service: RetrofitService
    lateinit var api: APIS
    lateinit var api_todo: APIS_todo

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        createRetrofit()
        //chrome://inspect/#devices
    }


    fun createRetrofit() {
        val header = Interceptor {
            val original = it.request()
            if (checkIsLogin()) {
                getUserToken()?.let { token ->
                    val requeset = original.newBuilder()
                        .header("X-AUTH-TOKEN", token)
                        .build()
                    it.proceed(requeset)
                }
            } else {
                it.proceed(original)
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

//        "http://180.230.121.23/"
//        "http://10.0.2.2:8000/"
        val retrofit = Retrofit.Builder()
            .baseUrl("http://180.230.121.23/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        service = retrofit.create(RetrofitService::class.java)
        api = retrofit.create(APIS::class.java)
        api_todo = retrofit.create(APIS_todo::class.java)
    }

    fun checkIsLogin(): Boolean {
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        var token = sp.getString("token", "null")
        if (token != "null") return true
        else return false
    }

    fun getUserToken(): String? {
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sp.getString("token", "null")
        if (token == "null") return null
        else return token
    }

}