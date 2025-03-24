package com.example.mvvm.data.remote

import com.example.mvvm.data.models.Product
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getAllProducts(): Flow<List<Product>?>
}