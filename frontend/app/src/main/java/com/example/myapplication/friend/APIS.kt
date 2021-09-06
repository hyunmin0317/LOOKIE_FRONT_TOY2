package com.example.myapplication.friend

import com.example.myapplication.RetrofitService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*



interface APIS : RetrofitService {

    // 친구 추가 기능
    @POST("follow/{memberId}")
    fun post_users(@Path("memberId") memberId: Long): Call<PostModel>

    // 친구 삭제 기능
    @DELETE("follow/{memberId}")
    fun delete_users(@Path("memberId") memberId: Long): Call<DeleteModel>


    // 친구 검색 기능 (완료)
    @GET("follow/search")
    fun get_users(@Query("name") name: String): Call<CheckGetModel>


    // 친구 목록 조회 기능 (완료)
    @GET("follow")
    fun get2_users(): Call<CheckGetModel2>



    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "http://180.230.121.23/" // 주소

        fun create(): APIS {


            val gson :Gson =   GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
        }
    }
}