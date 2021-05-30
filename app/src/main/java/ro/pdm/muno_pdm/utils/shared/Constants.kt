package ro.pdm.muno_pdm.utils.shared

/**
 * Constants for the url and paths (the list can be extended)
 */
class Constants {
    companion object {
        var ip: String = "192.168.0.151"
        const val countyUrl: String = "https://roloca.coldfuse.io/judete"
        const val cityUrl: String = "https://roloca.coldfuse.io/orase"

        private fun baseUrl() = "http://$ip:8080"
        fun accountUrl() = "${baseUrl()}/rest/authenticate"
        fun userUrl() = "${baseUrl()}/rest/users"
        fun productUrl() = "${baseUrl()}/rest/product"
    }
}