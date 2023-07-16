package com.example.demoapp.addProduct.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("message") val message: String,
    @SerializedName("product_details") val productDetails: ProductDetails,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("success") val success: Boolean
)

data class ProductDetails(
    // Define properties of the product details here
    // For example:
    @SerializedName("product_name") val productName: String,
    @SerializedName("product_type") val productType: String,
    @SerializedName("price") val price: String,
    @SerializedName("tax") val tax: String
)
