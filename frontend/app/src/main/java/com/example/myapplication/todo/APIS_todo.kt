package com.example.myapplication.todo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface APIS_todo {

    // todo리스트 조회
    @GET("study")
    fun get_todo(): Call<Check_Get_Todo>

    // todo리스트 생성
    @POST("study")
    fun post_todo(
        @Body planClass: plan
    ): Call<Post_Todo>


    @PUT("/study/{studyId}")
    fun put_todo(
        @Path("studyId") studyId: Long,
        @Query("status") status: String,
    ): Call<Put_Todo>

    @DELETE("/study/{studyId}")
    fun delete_todo(@Path("studyId") studyId: Long): Call<Delete_Todo>

    companion object {
        private const val Base_URL = "http://180.230.121.23/"

        fun create(): APIS_todo {
            val gson: Gson = GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create((APIS_todo::class.java))
        }
    }
}