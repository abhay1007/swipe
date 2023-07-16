package com.example.demoapp.addProduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.example.demoapp.listing.ProductRepository
import com.example.demoapp.listing.di.MyApp
import okhttp3.RequestBody.Companion.toRequestBody

class AddProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    companion object {
        private const val REQUEST_IMAGE_PICKER = 100
    }

    private val _imagePreviewUri = MutableLiveData<Uri?>()
    val imagePreviewUri: LiveData<Uri?> get() = _imagePreviewUri

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
                setImagePreview(it) // Set the selected image Uri for preview
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

    fun setImagePreview(uri: Uri?) {
        _imagePreviewUri.value = uri
    }

    private fun isValidData(): Boolean {
        if (productName.isBlank()) {
            showMessage("Product name cannot be empty.")
            return false
        }

        if (productType.isBlank()) {
            showMessage("Product type cannot be empty.")
            return false
        }

        if (price.isBlank()) {
            showMessage("Selling price cannot be empty.")
            return false
        }

        if (tax.isBlank()) {
            showMessage("Tax rate cannot be empty.")
            return false
        }

        // Add more validation as needed

        return true
    }
    private fun showMessage(message: String) {
        // Show a beautiful toast message
        // You can use any custom toast library or create your own beautiful toast UI here
        // For simplicity, we'll use a regular Toast message
        Toast.makeText(MyApp.MyapplicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
