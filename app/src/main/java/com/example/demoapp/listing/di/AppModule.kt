package com.example.demoapp.listing.di

import com.example.demoapp.addProduct.AddProductViewModel
import com.example.demoapp.listing.ListingViewModel
import com.example.demoapp.listing.ProductRepository
import com.example.demoapp.listing.RetrofitService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetrofitService.productService }
    single { ProductRepository(get()) }
    viewModel { ListingViewModel(get()) }
    viewModel{AddProductViewModel(get())}
}
