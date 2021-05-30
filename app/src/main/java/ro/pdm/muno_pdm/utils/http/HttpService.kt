package ro.pdm.muno_pdm.utils.http

import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Http util for http requests
 * TODO edit, delete
 */
class HttpService {

    inline fun <reified T> get(url: String, token: String? = null): Deferred<MunoResponse<T>> {
        return GlobalScope.async {
            val client = OkHttpClient()
            val builder = Request.Builder()
                .url(url)
                .get()

            if (token != null) {
                builder.addHeader("Authorization", "Bearer $token")
            }

            val munoResponse: MunoResponse<T> = MunoResponse()
            val responseBody: String
            try {
                val request = builder.build()
                val response = client.newCall(request).execute()
                responseBody = response.body!!.string()

                try {
                    munoResponse.value = Json.decodeFromString(responseBody)
                } catch (e: Exception) {
                    println("----------------- GET EXCEPTION -------------------")
                    println(e.localizedMessage)
                    munoResponse.errorMessage = responseBody
                }
            } catch (e: java.lang.Exception) {
                munoResponse.errorMessage = "Nu s-a putut face legatura cu serverul. Verificati IP-ul din setari"
            }

            return@async munoResponse
        }
    }

    inline fun <reified T> post(url: String, body: MunoSerializable, token: String? = null): Deferred<MunoResponse<T>> {
        return GlobalScope.async {
            val client = OkHttpClient()
            val mapper = jacksonObjectMapper()
            val bodyJson = mapper.writeValueAsString(body)
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val jsonBody = bodyJson.toRequestBody(mediaType)
            val builder = Request.Builder()
                .url(url)
                .post(jsonBody)

            if (token != null) {
                builder.addHeader("Authorization", "Bearer $token")
            }

            val munoResponse: MunoResponse<T> = MunoResponse()
            val responseBody: String
            try {
                val request = builder.build()
                val response = client.newCall(request).execute()
                responseBody = response.body!!.string()

                try {
                    munoResponse.value = Json.decodeFromString(responseBody)
                } catch (e: Exception) {
                    println("----------------- POST EXCEPTION -------------------")
                    println(e.localizedMessage)
                    munoResponse.errorMessage = responseBody
                }
            } catch (e: java.lang.Exception) {
                munoResponse.errorMessage = "Nu s-a putut face legatura cu serverul. Verificati IP-ul din setari"
            }

            return@async munoResponse
        }
    }

    inline fun <reified T> put(url: String, body: MunoSerializable, token: String): Deferred<MunoResponse<T>> {
        return GlobalScope.async {
            val client = OkHttpClient()
            val mapper = jacksonObjectMapper()
            val bodyJson = mapper.writeValueAsString(body)
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val jsonBody = bodyJson.toRequestBody(mediaType)
            val builder = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $token")
                .put(jsonBody)

            val munoResponse: MunoResponse<T> = MunoResponse()
            val responseBody: String

            try {
                val request = builder.build()
                val response = client.newCall(request).execute()
                responseBody = response.body!!.string()

                try {
                    munoResponse.value = Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
                } catch (e: Exception) {
                    println("----------------- PUT EXCEPTION -------------------")
                    println(e.localizedMessage)
                    munoResponse.errorMessage = responseBody
                }
            } catch (e: java.lang.Exception) {
                munoResponse.errorMessage = "Nu s-a putut face legatura cu serverul. Verificati IP-ul din setari"
            }

            return@async munoResponse
        }
    }

    inline fun <reified T> delete(url: String, token: String? = null): Deferred<MunoResponse<T>> {
        return GlobalScope.async {
            val client = OkHttpClient()
            val builder = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $token")
                .delete()

            val munoResponse: MunoResponse<T> = MunoResponse()
            val responseBody: String

            try {
                val request = builder.build()
                val response = client.newCall(request).execute()
                responseBody = response.body!!.string()

                try {
                    munoResponse.value = Json { ignoreUnknownKeys = true }.decodeFromString(responseBody)
                } catch (e: Exception) {
                    println("----------------- DELETE EXCEPTION -------------------")
                    println(e.localizedMessage)
                    munoResponse.errorMessage = responseBody
                }
            } catch (e: java.lang.Exception) {
                munoResponse.errorMessage = "Nu s-a putut face legatura cu serverul. Verificati IP-ul din setari"
            }

            return@async munoResponse
        }
    }
}