import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.demoapp.R
import com.example.demoapp.databinding.FragmentListingBinding
import com.example.demoapp.listing.ListingViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoapp.MainActivity
import com.example.demoapp.listing.Product
import org.koin.androidx.viewmodel.ext.android.viewModel
class ListingFragment : Fragment(R.layout.fragment_listing) {

    private lateinit var binding: FragmentListingBinding
    private lateinit var adapter: ProductAdapter
    private val viewModel: ListingViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListingBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        adapter = ProductAdapter()
        binding.productRecyclerView.adapter = adapter
        binding.productRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.productsLiveData.observe(viewLifecycleOwner) { products ->
            val productsWithImages = products.filter { it.image.isNotBlank() }
            adapter.products = productsWithImages
            printProductList(products)
        }
        binding.addProductButton.setOnClickListener{
            (activity as MainActivity).goToAddDetails()
        }

        viewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.fetchProducts()
    }

    private fun printProductList(products: List<Product>) {
        for (product in products) {
            Log.d("ListingFragment", "Product Name: ${product.product_name}")
            Log.d("ListingFragment", "Product Type: ${product.product_type}")
            Log.d("ListingFragment", "Price: ${product.price}")
            Log.d("ListingFragment", "Tax: ${product.tax}")
            Log.d("ListingFragment", "Image URL: ${product.image}")
            Log.d("ListingFragment", "------------------------------------")
        }
    }
}
