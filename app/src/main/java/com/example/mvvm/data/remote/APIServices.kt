package com.example.mvvm.data.remote
import retrofit2.Response
import retrofit2.http.GET

interface APIService {
    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>
}




