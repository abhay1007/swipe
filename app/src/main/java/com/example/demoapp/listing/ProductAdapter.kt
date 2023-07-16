import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.demoapp.R
import com.example.demoapp.databinding.ItemProductBinding
import com.example.demoapp.listing.Product


class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var products: List<Product> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
            printProductList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.product = product

            // Load the product image using Glide
            Glide.with(binding.root)
                .load(product.image)
                .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                .error(R.drawable.error) // Error image if loading fails
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(binding.productImage)

            binding.executePendingBindings()
        }
    }

    private fun printProductList(products: List<Product>) {
        for (product in products) {
            Log.d("ProductAdapter", "Product Name: ${product.product_name}")
            Log.d("ProductAdapter", "Product Type: ${product.product_type}")
            Log.d("ProductAdapter", "Price: ${product.price}")
            Log.d("ProductAdapter", "Tax: ${product.tax}")
            Log.d("ProductAdapter", "Image URL: ${product.image}")
            Log.d("ProductAdapter", "------------------------------------")
        }
    }
}
