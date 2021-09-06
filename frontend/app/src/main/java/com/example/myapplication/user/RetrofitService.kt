package com.example.myapplication

import com.example.myapplication.user.Login
import com.example.myapplication.user.Signup
import retrofit2.Call
import retrofit2.http.*


interface RetrofitService {

    @POST("user/signup")
    fun register(
        @Body signup: Signup
    ): Call<Signup>


    @POST("user/signin")
    fun login(
        @Body login: Login
    ): Call<User>

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