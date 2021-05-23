package ro.pdm.muno_pdm.product.service

import android.provider.SyncStateContract
import kotlinx.coroutines.Deferred
import ro.pdm.muno_pdm.product.models.Product
import ro.pdm.muno_pdm.utils.http.HttpService
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.shared.Constants

class ProductService {
    private val httpService = HttpService()

    fun getProduct(id: Int): Deferred<MunoResponse<Product>> {
        return httpService.get(Constants().productUrl + "/myProduct/$id")
    }
}