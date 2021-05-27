package ro.pdm.muno_pdm.account.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import ro.pdm.muno_pdm.utils.http.MunoSerializable

@Keep
@Serializable
class City(
    @SerializedName("nume") var nume: String? = null,
    @SerializedName("comuna") var comuna: String? = null,
    @SerializedName("simplu") var simplu: String? = null
) : MunoSerializable()