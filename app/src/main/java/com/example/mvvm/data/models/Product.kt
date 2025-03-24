package com.example.mvvm.data.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Long,
    val brand: String?,
    val sku: String,
    val weight: Long,
    val warrantyInformation: String,
    val shippingInformation: String,
    val availabilityStatus: String,
    val returnPolicy: String,
    val minimumOrderQuantity: Long,
    val thumbnail: String
){
    @Ignore
    var tags: List<String> = emptyList()

    @Ignore
    var dimensions: Dimensions? = null

    @Ignore
    var reviews: List<Review> = emptyList()

    @Ignore
    var meta: Meta? = null

    @Ignore
    var images: List<String> = emptyList()
}


data class Dimensions(
    val width: Double,
    val height: Double,
    val depth: Double,
)

data class Review(
    val rating: Long,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String,
)

data class Meta(
    val createdAt: String,
    val updatedAt: String,
    val barcode: String,
    val qrCode: String,
)
