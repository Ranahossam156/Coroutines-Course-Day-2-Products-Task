package com.example.mvvm.data.remote

import com.example.mvvm.data.models.Product

data class ProductResponse(
    val products: List<Product>,
    val total: Long,
    val skip: Long,
    val limit: Long,
)
