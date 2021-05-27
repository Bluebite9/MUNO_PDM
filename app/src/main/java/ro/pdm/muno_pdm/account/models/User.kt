package ro.pdm.muno_pdm.account.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import com.google.gson.annotations.SerializedName
import ro.pdm.muno_pdm.utils.http.MunoSerializable

/**
 * User model
 */
@Keep
@Serializable
data class User(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("role") var role: String? = null,
    @SerializedName("county") var county: String? = null,
    @SerializedName("city") var city: String? = null
) : MunoSerializable()