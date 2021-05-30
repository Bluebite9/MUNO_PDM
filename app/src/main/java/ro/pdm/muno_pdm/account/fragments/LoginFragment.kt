package ro.pdm.muno_pdm.account.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.account.models.AuthRequest
import ro.pdm.muno_pdm.account.models.AuthResponse
import ro.pdm.muno_pdm.account.service.AccountService
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.session.MunoDatabaseObject
import ro.pdm.muno_pdm.utils.session.SessionService
import ro.pdm.muno_pdm.utils.shared.Constants


class LoginFragment : Fragment() {

    private val accountService = AccountService()

    lateinit var loginBt: Button
    lateinit var emailEt: EditText
    lateinit var passwordEt: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEt = view.findViewById(R.id.emailEt)
        passwordEt = view.findViewById(R.id.passwordEt)
        loginBt = view.findViewById(R.id.loginBt)

        loginBt.setOnClickListener {
            // trim the email box
            if (emailEt.text != null) {
                emailEt.text = emailEt.text.replace(
                    0,
                    emailEt.text.toString().length,
                    emailEt.text.toString().trim()
                )
            }

            // trim the password box
            if (passwordEt.text != null) {
                passwordEt.text = passwordEt.text.replace(
                    0,
                    passwordEt.text.toString().length,
                    passwordEt.text.toString().trim()
                )
            }

            if (emailEt.text != null && emailEt.text.toString().trim() != "" &&
                passwordEt.text != null && passwordEt.text.toString().trim() != ""
            ) {
                val authRequest = AuthRequest("", "")
                authRequest.email = emailEt.text.toString()
                authRequest.password = passwordEt.text.toString()

                viewLifecycleOwner.lifecycleScope.launch {
                    // api call
                    println("------------ LOGIN -----------")
//                    println(Constants.ip)
//                    println(Constants.baseUrl)
//                    println(Constants.accountUrl)
                    val munoResponse : MunoResponse<AuthResponse> = accountService.login(authRequest).await()

                    // if error -> popup with error message
                    if (munoResponse.errorMessage != null) {
                        AlertDialog.Builder(context).setTitle("Atentie!")
                            .setMessage(munoResponse.errorMessage)
                            .setPositiveButton("OK", null)
                            .create()
                            .show()
                    }
                    // if not error, save the token and id into a local database for later use then
                    // change the lateral menu and go to search page
                    else {
                        val userIdMunoDatabaseObject = MunoDatabaseObject()
                        // save user id
                        userIdMunoDatabaseObject.key = "id"
                        userIdMunoDatabaseObject.value = munoResponse.value?.user?.id.toString()
                        SessionService(requireActivity().application).insert(userIdMunoDatabaseObject)

                        // save token
                        val tokenMunoDatabaseObject = MunoDatabaseObject()
                        tokenMunoDatabaseObject.key = "token"
                        tokenMunoDatabaseObject.value = munoResponse.value?.token
                        SessionService(requireActivity().application).insert(tokenMunoDatabaseObject)

                        changeNavigationContext()

                        // set the start page & automatically go to it
                        val inflater = findNavController().navInflater
                        val graph = inflater.inflate(R.navigation.nav_graph)
                        graph.startDestination = R.id.searchFragment

                        findNavController().graph = graph
                    }
                }
            }
        }
    }

    /**
     * When user logs in, change the items in the lateral menu so he can create an add, view his/her
     * adds or view his/hers account details & cannot login again or create an account
     */
    private fun changeNavigationContext() {
        val navigationView : NavigationView? = activity?.findViewById(R.id.nav_view)
        if (navigationView != null) {
            navigationView.menu.findItem(R.id.createProductFragment).isVisible = true
            navigationView.menu.findItem(R.id.viewProductListFragment).isVisible = true
            navigationView.menu.findItem(R.id.logoutFragment).isVisible = true
            navigationView.menu.findItem(R.id.viewAccountFragment).isVisible = true
            navigationView.menu.findItem(R.id.loginFragment).isVisible = false
            navigationView.menu.findItem(R.id.createAccountFragment).isVisible = false
        }
    }
}