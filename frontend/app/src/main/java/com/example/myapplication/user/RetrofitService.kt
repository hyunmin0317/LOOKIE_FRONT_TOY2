package com.example.myapplication

import retrofit2.Call
import retrofit2.http.*


interface RetrofitService {

    @POST("user/signup/")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<User>


    @POST("user/login/")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<User>

}