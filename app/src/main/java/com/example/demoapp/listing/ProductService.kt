package com.example.demoapp.listing

import com.example.demoapp.addProduct.model.ProductResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
interface ProductService {
    @GET("public/get")
    suspend fun getProducts(): List<Product>

    @POST("public/add")
    @Multipart
    suspend fun addProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part image: MultipartBody.Part? // Optional image
    ): Response<ProductResponse>
}

class ProductRepository(private val productService: ProductService) {
    suspend fun getProducts(): List<Product> {
        return productService.getProducts()
    }

    suspend fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        imageFile: MultipartBody.Part? = null
    ): Result<ProductResponse> {
        try {
            val productNameBody = createRequestBody(productName)
            val productTypeBody = createRequestBody(productType)
            val priceBody = createRequestBody(price)
            val taxBody = createRequestBody(tax)


            val response = productService.addProduct(productNameBody, productTypeBody, priceBody, taxBody, imageFile)

            if (response.isSuccessful) {
                response.body()?.let {
                    return Result.success(it)
                } ?: run {
                    return Result.failure(java.lang.Exception("On failed"))
                }
            } else {
                return Result.failure(java.lang.Exception("Failed to add product. Please try again."))
            }
        } catch (e: Exception) {
            return Result.failure(java.lang.Exception("An error occurred. Please try again."))
        }
    }

    private fun createRequestBody(value: String): RequestBody {
        return RequestBody.create(okhttp3.MultipartBody.FORM, value)
    }
}