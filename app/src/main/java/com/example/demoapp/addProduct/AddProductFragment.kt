package com.example.demoapp.addProduct


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.demoapp.MainActivity
import com.example.demoapp.R
import com.example.demoapp.databinding.FragmentAddProductBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddProductFragment : Fragment() {
    private lateinit var binding: FragmentAddProductBinding
    private val viewModel: AddProductViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentAddProductBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity=requireActivity() as MainActivity

        return binding.root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val viewModel: AddProductViewModel by viewModels()
        viewModel.onActivityResult(requestCode, resultCode, data)

        // Set the image preview when the image URI is available
    }

}


