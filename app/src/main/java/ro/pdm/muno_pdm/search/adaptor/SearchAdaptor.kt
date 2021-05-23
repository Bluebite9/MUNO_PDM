package ro.pdm.muno_pdm.search.adaptor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.product.models.Product

class SearchAdaptor(var productList: List<Product>?) : RecyclerView.Adapter<SearchAdaptor.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        var nameTv: TextView = view.findViewById(R.id.nameTv)
        var descriptionTv: TextView = view.findViewById(R.id.descriptionTv)
        var priceTv: TextView = view.findViewById(R.id.priceTv)
        var unitTv: TextView = view.findViewById(R.id.unitTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.product_line, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val product = productList?.get(position)

        if (product != null) {
            holder.nameTv.text = product.name
            holder.descriptionTv.text = product.description
            holder.priceTv.text = product.price.toString()
            holder.unitTv.text = product.unit
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("product", product)
//            it.findNavController().navigate(R.id.detaliiTraseuFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }
}