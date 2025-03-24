package com.example.mvvm.data.repo

import androidx.collection.emptyLongSet
import com.example.mvvm.data.local.LocalDataSource
import com.example.mvvm.data.models.Product
import com.example.mvvm.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImplementation private constructor(
    private val remoteDataSource:RemoteDataSource,
    private val localDataSource: LocalDataSource
):ProductRepository{
    override suspend fun getAllProducts():Flow<List<Product>?>{
        return remoteDataSource.getAllProducts()// if(isOnline){
          //  remoteDataSource.getAllProducts()
 //       }
//        else{
//            localDataSource.getAllProducts()
//        }
    }
    override suspend fun getStoredProducts(): Flow<List<Product>>?{
        return localDataSource.getAllProducts()

    }
    override suspend fun addProduct(product: Product):Long{
        return localDataSource.insertProduct(product)
    }
    override suspend fun removeProduct(product: Product):Int{
        return localDataSource.deleteProduct(product)
    }
    companion object{
        private var INSTANCE:ProductRepositoryImplementation?=null
        fun getInstance(remoteDataSource: RemoteDataSource,localDataSource: LocalDataSource):ProductRepository{
            return INSTANCE?: synchronized(this){
                val temp=ProductRepositoryImplementation(remoteDataSource,localDataSource)
                INSTANCE=temp
                temp
            }
        }
    }

}