package ro.pdm.muno_pdm.product.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ro.pdm.muno_pdm.product.models.Product

class MyProductsViewModel(application: Application) : AndroidViewModel(application) {
    var productList: List<Product>? = null
}