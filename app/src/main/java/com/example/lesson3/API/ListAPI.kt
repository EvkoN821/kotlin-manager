package com.example.lesson3.API

import com.example.lesson3.data.Managers
import com.example.lesson3.data.Manager
import com.example.lesson3.data.Client
import com.example.lesson3.data.Clients
import com.example.lesson3.data.Invoice
import com.example.lesson3.data.Invoices
import com.example.lesson3.data.Spasibo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT

interface ListAPI{
    @GET("manager")
    fun getRestaurants(): Call<Managers>

    @Headers("Content-Type: application/json")
    @POST("manager")
    fun updateRestaurant(@Body faculty: Manager): Call<PostResult>

    @Headers("Content-Type: application/json")
    @POST("manager/delete")
    fun deleteRestaurant(@Body id: PostId): Call<PostResult>

    @Headers("Content-Type: application/json")
    @PUT("manager")
    fun insertRestaurant(@Body faculty: Manager): Call<PostResult>

    @GET("client")
    fun getCourses(): Call<Clients>
    @Headers("Content-Type: application/json")
    @POST("client")
    fun updateCourse(@Body client: Client): Call<PostResult>

    @Headers("Content-Type: application/json")
    @POST("client/delete")
    fun deleteCourse(@Body id: PostId): Call<PostResult>

    @Headers("Content-Type: application/json")
    @PUT("client")
    fun insertCourse(@Body client: Client): Call<PostResult>

    @GET("invoice")
    fun getFoods(): Call<Invoices>

    @Headers("Content-Type: application/json")
    @POST("invoice")
    fun updateFood(@Body invoice: Invoice): Call<PostResult>

    @Headers("Content-Type: application/json")
    @POST("invoice/delete")
    fun deleteFood(@Body id: PostId): Call<PostResult>

    @Headers("Content-Type: application/json")
    @PUT("invoice")
    fun insertFood(@Body invoice: Invoice): Call<PostResult>

    @Headers("Content-Type: application/json")
    @POST("user")
    fun login(@Body user: PostUser): Call<Spasibo>

}