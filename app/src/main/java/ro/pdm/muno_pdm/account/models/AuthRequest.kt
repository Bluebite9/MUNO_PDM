package ro.pdm.muno_pdm.account.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import ro.pdm.muno_pdm.utils.http.MunoSerializable

/**
 *  Object used for the request when log in
 */
@Keep
@Serializable
class AuthRequest (
    var email: String,
    var password: String
) : MunoSerializable()