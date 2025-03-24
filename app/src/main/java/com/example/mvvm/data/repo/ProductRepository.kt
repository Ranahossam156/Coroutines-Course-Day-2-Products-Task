package com.example.mvvm.data.repo

import com.example.mvvm.data.local.LocalDataSource
import com.example.mvvm.data.models.Product
import com.example.mvvm.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    //suspend fun getAllProducts(isOnline:Boolean):List<Product>?
    suspend fun addProduct(product: Product):Long
    suspend fun removeProduct(product: Product):Int

    suspend fun getStoredProducts(): Flow<List<Product>>?
    suspend fun getAllProducts(): Flow<List<Product>?>
}