package com.example.demoapp.listing.di

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    companion object {
      lateinit var  MyapplicationContext: Context
    }
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(appModule)

        }
        MyapplicationContext=applicationContext
    }

}
