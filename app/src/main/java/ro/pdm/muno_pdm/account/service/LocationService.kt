package ro.pdm.muno_pdm.account.service

import kotlinx.coroutines.Deferred
import ro.pdm.muno_pdm.account.models.City
import ro.pdm.muno_pdm.account.models.County
import ro.pdm.muno_pdm.utils.http.HttpService
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.shared.Constants

class LocationService {
    private val httpService = HttpService()

    fun getCounties(): Deferred<MunoResponse<List<County>>> {
        return httpService.get(Constants().countyUrl)
    }

    fun getCities(countyAuto: String): Deferred<MunoResponse<List<City>>> {
        return httpService.get(Constants().cityUrl + "/$countyAuto")
    }
}