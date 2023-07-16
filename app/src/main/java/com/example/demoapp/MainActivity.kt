package com.example.demoapp

import ListingFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demoapp.addProduct.AddProductFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ListingFragment())
                .commit()
        }
    }
    fun goToAddDetails(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AddProductFragment())
            .commit()
    }
}
