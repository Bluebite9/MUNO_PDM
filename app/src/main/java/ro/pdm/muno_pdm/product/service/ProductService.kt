package ro.pdm.muno_pdm.product.service

import kotlinx.coroutines.Deferred
import ro.pdm.muno_pdm.product.models.Product
import ro.pdm.muno_pdm.utils.http.HttpService
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.shared.Constants

class ProductService {
    private val httpService = HttpService()

    fun getProduct(id: Int): Deferred<MunoResponse<Product>> {
        return httpService.get(Constants.productUrl() + "/myProduct/$id")
    }

    fun editProduct(product: Product, token: String): Deferred<MunoResponse<Product>> {
        return httpService.put(Constants.productUrl() + "/updateProduct", product, token)
    }

    fun deleteProduct(productId: Int, token: String): Deferred<MunoResponse<Any>> {
        return httpService.delete(Constants.productUrl() + "/deleteProduct/$productId", token)
    }

    fun getMyProducts(userId: String, token: String): Deferred<MunoResponse<List<Product>>> {
        return httpService.get(Constants.productUrl() + "/myProducts/$userId", token)
    }

    fun addProduct(product: Product, token: String): Deferred<MunoResponse<Product>> {
        return httpService.post(Constants.productUrl() + "/saveProduct", product, token)
    }
}