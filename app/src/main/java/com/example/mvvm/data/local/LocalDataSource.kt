package com.example.mvvm.data.local

import com.example.mvvm.data.models.Product
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun insertProduct(product: Product):Long
    suspend fun deleteProduct(product: Product?):Int
}