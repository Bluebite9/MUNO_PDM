package ro.pdm.muno_pdm.search.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Deferred
import ro.pdm.muno_pdm.product.models.Product
import ro.pdm.muno_pdm.search.service.SearchService
import ro.pdm.muno_pdm.utils.http.MunoResponse

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    var productList: List<Product>? = null
    private val searchService = SearchService()

    fun searchClick(query: String): Deferred<MunoResponse<List<Product>>> {
       return searchService.search(query)
    }
}