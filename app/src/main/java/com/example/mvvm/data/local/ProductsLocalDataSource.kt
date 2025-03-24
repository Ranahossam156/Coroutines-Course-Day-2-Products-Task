package com.example.mvvm.data.local

import com.example.mvvm.data.models.Product
import kotlinx.coroutines.flow.Flow

class ProductsLocalDataSource(private val dao:ProductDao):LocalDataSource{
   // override suspend fun getAllProducts():List<Product>{
   override suspend fun getAllProducts(): Flow<List<Product>> {
        return dao.getAllFavoriteProducts()
    }
    override suspend fun insertProduct(product: Product):Long{
        return dao.insertProduct(product)
    }
    override suspend fun deleteProduct(product: Product?):Int{
        return if(product!=null)
            dao.deleteProduct(product)
        else
            -1
    }
}