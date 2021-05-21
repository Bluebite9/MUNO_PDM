package ro.pdm.muno_pdm.utils.http

import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ro.pdm.muno_pdm.account.models.AuthRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Http util for http requests
 * TODO get with token, post with token, edit, delete
 */
class HttpService {
    inline fun <reified T> get(url: String): T {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()

        val responseBody = response.body!!.string()

        val returnObject: T

        try {
            returnObject = Json.decodeFromString(responseBody)
        } catch (e: Exception) {
            throw Exception(responseBody)
        }

        return returnObject
    }

    @Throws(java.lang.Exception::class)
    inline fun <reified T> post(url: String, body: MunoSerializable): Deferred<MunoResponse<T>> {

        return GlobalScope.async {
            val client = OkHttpClient()
            val mapper = jacksonObjectMapper()
            val bodyJson = mapper.writeValueAsString(body)

            println("--------- POST ----------")
            println((body as AuthRequest).email)
            println(bodyJson)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val jsonBody = bodyJson.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(jsonBody)
                .build()

            val response = client.newCall(request).execute()

            val responseBody = response.body!!.string()

            println(responseBody)

            val munoResponse: MunoResponse<T> = MunoResponse()

            try {
                munoResponse.value = Json.decodeFromString(responseBody)
            } catch (e: Exception) {
                munoResponse.errorMessage = responseBody
            }

            return@async munoResponse
        }
    }
}