package com.example.myapplication

import com.example.myapplication.user.User
import retrofit2.Call
import retrofit2.http.*


interface RetrofitService {

    @POST("user/signup")
    fun signup(
        @Body user: User
    ): Call<Login>


    @POST("user/signin")
    fun login(
        @Body user: User
    ): Call<Login>

    @POST("timer")
    fun timer(
        @Body timer: Timer
    ): Call<Timer>

    @PATCH("timer")
    fun status(
        @Query("status") status: String,
    ): Call<Timer>

    @GET("timer")
    fun gettime(): Call<Timer>

}