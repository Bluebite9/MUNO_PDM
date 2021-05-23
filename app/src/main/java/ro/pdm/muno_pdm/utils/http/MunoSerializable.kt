package ro.pdm.muno_pdm.utils.http

import java.io.Serializable

/**
 * Basic class that should be extended by all classes that will be used for http requests
 */
@kotlinx.serialization.Serializable
open class MunoSerializable : Serializable {
}