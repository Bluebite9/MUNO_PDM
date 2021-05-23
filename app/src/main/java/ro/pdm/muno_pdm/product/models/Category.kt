package ro.pdm.muno_pdm.product.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import ro.pdm.muno_pdm.utils.http.MunoSerializable

@Keep
@Serializable
class Category(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null
) : MunoSerializable()