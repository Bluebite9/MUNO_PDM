package ro.pdm.muno_pdm.product.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import ro.pdm.muno_pdm.account.models.User
import ro.pdm.muno_pdm.utils.http.MunoSerializable

/**
 * Product model
 */
@Keep
@Serializable
class Product(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("price") var price: Float? = null,
    @SerializedName("unit") var unit: String? = null,
    @SerializedName("user") var user: User? = null,
    @SerializedName("category") var category: Category? = null
) : MunoSerializable()