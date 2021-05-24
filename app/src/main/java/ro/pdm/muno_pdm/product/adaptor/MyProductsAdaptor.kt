package ro.pdm.muno_pdm.product.adaptor

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.product.models.Product
import ro.pdm.muno_pdm.product.service.ProductService
import ro.pdm.muno_pdm.utils.session.SessionService


class MyProductsAdaptor(
    var productList: MutableList<Product>?,
    private val viewLifecycleOwner: LifecycleOwner,
    val application: Application
) : RecyclerView.Adapter<MyProductsAdaptor.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        var nameTv: TextView = view.findViewById(R.id.nameTv)
        var descriptionTv: TextView = view.findViewById(R.id.descriptionTv)
        var priceTv: TextView = view.findViewById(R.id.priceTv)
        var unitTv: TextView = view.findViewById(R.id.unitTv)
        var deleteBtn: Button = view.findViewById(R.id.deleteProductBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductsAdaptor.VH {
        return MyProductsAdaptor.VH(
            LayoutInflater.from(parent.context).inflate(R.layout.my_product_line, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyProductsAdaptor.VH, position: Int) {
        val product = productList?.get(position)

        if (product != null) {
            holder.nameTv.text = product.name
            holder.descriptionTv.text = product.description
            holder.priceTv.text = product.price.toString()
            holder.unitTv.text = product.unit
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            if (product != null) {
                bundle.putSerializable("productId", product.id)
            }
            it.findNavController().navigate(R.id.viewProductFragment, bundle)
        }

        val productService = ProductService()

        holder.deleteBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val token = SessionService(application).get("token").await().value
                if (product != null) {
                    product.id?.let { it1 ->
                        if (token != null) {
                            productService.deleteProduct(
                                it1,
                                token
                            ).await()

                            productList?.removeAt(position)
                            notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }

}