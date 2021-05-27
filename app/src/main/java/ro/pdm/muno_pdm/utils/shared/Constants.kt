package ro.pdm.muno_pdm.utils.shared

/**
 * Constants for the url and paths (the list can be extended)
 */
class Constants {
    private val baseUrl: String = "http://172.26.16.1:8080"

    val accountUrl: String = "$baseUrl/rest/authenticate"
    val userUrl: String = "$baseUrl/rest/users"
    val productUrl: String = "$baseUrl/rest/product"
    val countyUrl: String = "https://roloca.coldfuse.io/judete"
    val cityUrl: String = "https://roloca.coldfuse.io/orase"
}