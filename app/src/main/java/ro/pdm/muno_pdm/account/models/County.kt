package ro.pdm.muno_pdm.account.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import ro.pdm.muno_pdm.utils.http.MunoSerializable

@Keep
@Serializable
class County(
    @SerializedName("auto") var auto: String,
    @SerializedName("nume") var nume: String
) : MunoSerializable()