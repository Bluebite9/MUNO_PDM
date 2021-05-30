package ro.pdm.muno_pdm.search.service

import kotlinx.coroutines.Deferred
import ro.pdm.muno_pdm.product.models.Product
import ro.pdm.muno_pdm.utils.http.HttpService
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.shared.Constants

class SearchService {
    private val httpService = HttpService()

    fun search(query: String): Deferred<MunoResponse<List<Product>>> {
        val url = Constants.productUrl() + "/search?query=" + query
        return httpService.get(url)
    }
}