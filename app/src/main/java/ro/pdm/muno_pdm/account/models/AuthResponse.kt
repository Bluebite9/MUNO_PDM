package ro.pdm.muno_pdm.account.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import ro.pdm.muno_pdm.utils.http.MunoSerializable

/**
 *  Object that represents the response when log in
 */
@Keep
@Serializable
data class AuthResponse (
    @SerializedName("user") var user: User? = null,
    @SerializedName("token") var token: String? = null
) : MunoSerializable()