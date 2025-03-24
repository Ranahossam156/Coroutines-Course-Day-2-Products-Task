package com.example.mvvm

import com.example.mvvm.data.models.Product

sealed class Response {
    data object Loading:Response()
    data class Success(val data: List<Product>?):Response()
    data class Failure(val error:Throwable):Response()

}