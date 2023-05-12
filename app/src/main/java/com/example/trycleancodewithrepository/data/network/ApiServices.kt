package com.example.trycleancodewithrepository.data.network

import com.example.trycleancodewithrepository.models.MealResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiServices {
    @GET("api/json/v1/1/random.php")
    suspend fun getMealApi() :Response<MealResponse>
}