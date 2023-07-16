package com.example.demoapp.addProduct

import androidx.lifecycle.ViewModel
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.demoapp.listing.di.MyApp
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.demoapp.listing.ProductRepository

import okhttp3.RequestBody.Companion.toRequestBody

class AddProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    companion object {
        private const val REQUEST_IMAGE_PICKER = 100
    }

    var productName: String = ""
    var productType: String = ""
    var price: String = ""
    var tax: String = ""
    var imageUri: Uri? = null

    fun pickImage(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            openImagePicker(activity)
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_IMAGE_PICKER
            )
        }
    }

    private fun openImagePicker(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, REQUEST_IMAGE_PICKER)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                imageUri = it
            }
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_IMAGE_PICKER && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, open image picker
            // You can call this method from the UI when the user clicks on the "Select Image" button
            // You can also use the imageUri to display a preview of the selected image to the user
            // in the UI.
            // For example, you can use Glide or Picasso to load the image into an ImageView
            // Here's an example using Glide:
            // Glide.with(activity)
            //     .load(imageUri)
            //     .into(imageView)
        } else {
            // Permission denied
            showMessage("Permission denied. Cannot pick an image.")
        }
    }

    fun onSubmitClicked() {
        if (isValidData()) {
            val imageFile = imageUri?.let { File(it.path) }
            val imagePart = imageFile?.let {
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("files[]", it.name, requestFile)
            }

            val productNameBody = productName.toRequestBody("text/plain".toMediaTypeOrNull())
            val productTypeBody = productType.toRequestBody("text/plain".toMediaTypeOrNull())
            val priceBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
            val taxBody = tax.toRequestBody("text/plain".toMediaTypeOrNull())

            viewModelScope.launch {
                try {
                    val response = productRepository.addProduct(
                        productNameBody.toString(),
                        productTypeBody.toString(),
                        priceBody.toString(),
                        taxBody.toString(),
                        imagePart
                    )
                    if (response.isSuccess) {
                        showMessage("Product added Successfully!")
                    } else {
                        showMessage("Failed to add product. Please try again.")
                    }
                } catch (e: Exception) {
                    showMessage("An error occurred. Please try again.")
                    e.printStackTrace()
                }
            }
        }
    }

    private fun isValidData(): Boolean {
        // Add your validation logic here, such as checking if fields are not empty, etc.
        return true
    }

    private fun showMessage(message: String) {
        // Show a beautiful toast message
        // You can use any custom toast library or create your own beautiful toast UI here
        // For simplicity, we'll use a regular Toast message
        Toast.makeText(MyApp.MyapplicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
