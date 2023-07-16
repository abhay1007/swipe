package com.example.demoapp.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListingViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _productsLiveData = MutableLiveData<List<Product>>()
    val productsLiveData: LiveData<List<Product>> get() = _productsLiveData

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                _loadingLiveData.value = true

                // Fetch products using the productRepository
                val products = withContext(Dispatchers.IO) {
                    productRepository.getProducts()
                }

                _productsLiveData.value = products
            } catch (e: Exception) {
                // Handle error if necessary
            } finally {
                _loadingLiveData.value = false
            }
        }
    }


}
