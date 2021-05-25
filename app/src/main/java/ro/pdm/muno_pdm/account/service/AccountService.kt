package ro.pdm.muno_pdm.account.service

import kotlinx.coroutines.Deferred
import ro.pdm.muno_pdm.account.models.AuthRequest
import ro.pdm.muno_pdm.account.models.AuthResponse
import ro.pdm.muno_pdm.account.models.User
import ro.pdm.muno_pdm.utils.shared.Constants
import ro.pdm.muno_pdm.utils.http.HttpService
import ro.pdm.muno_pdm.utils.http.MunoResponse

/**
 * Service for account. Should be used for logging in, creating a new account or editing one
 */
class AccountService {

    private val httpService = HttpService()
    private val constants = Constants()

    fun login(authRequest: AuthRequest): Deferred<MunoResponse<AuthResponse>> {
        return httpService.post(constants.accountUrl, authRequest)
    }

    fun getUserById(id: Long, token: String): Deferred<MunoResponse<User>> {
        return httpService.get(constants.userUrl + "/$id", token)
    }

    fun register(user: User): Deferred<MunoResponse<AuthResponse>> {
        return httpService.post(constants.userUrl + "/add", user)
    }

    fun editUser(user: User, token: String): Deferred<MunoResponse<Any>> {
        return httpService.put(constants.userUrl + "/updateUser", user, token)
    }

}