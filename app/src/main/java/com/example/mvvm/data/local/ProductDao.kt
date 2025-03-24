package com.example.mvvm.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvm.data.models.Product
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

   // @Query("SELECT * FROM products")
    //fun getAllProducts(): Flow<List<Product>>
    //suspend fun getAllProducts(): List<Product>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: Product):Long
    @Delete
    suspend fun deleteProduct(product: Product):Int
    @Query("Select * from products")
    fun getAllFavoriteProducts():Flow<List<Product>>
    //suspend fun getAllFavoriteProducts():List<Product>
}