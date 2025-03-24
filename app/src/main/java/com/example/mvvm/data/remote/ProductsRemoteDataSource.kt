package com.example.mvvm.data.remote

import com.example.mvvm.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProductsRemoteDataSource(private val service:APIService) :RemoteDataSource{
    override suspend fun getAllProducts(): Flow<List<Product>?> {
        val response= service.getProducts().body()?.products
        return flowOf(response)
    }
}