package ro.pdm.muno_pdm.account.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.account.models.User
import ro.pdm.muno_pdm.account.service.AccountService
import ro.pdm.muno_pdm.utils.session.MunoDatabaseObject
import ro.pdm.muno_pdm.utils.session.SessionService

class CreateAccountFragment : Fragment() {

    private val accountService = AccountService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEt: EditText = view.findViewById(R.id.emailEt)
        val passwordEt: EditText = view.findViewById(R.id.passwordEt)
        val confirmPasswordEt: EditText = view.findViewById(R.id.confirmPasswordEt)
        val phoneEt: EditText = view.findViewById(R.id.phoneEt)
        val firstNameEt: EditText = view.findViewById(R.id.firstNameEt)
        val lastNameEt: EditText = view.findViewById(R.id.lastNameEt)
        val registerBt: Button = view.findViewById(R.id.registerBt)

        registerBt.setOnClickListener {

            // TODO validations!!!

            if (emailEt.text != null && emailEt.text.toString().trim() != "" &&
                passwordEt.text != null && passwordEt.text.toString().trim() != "" &&
                confirmPasswordEt.text != null && confirmPasswordEt.text.toString().trim() != "" &&
                phoneEt.text != null && phoneEt.text.toString().trim() != "" &&
                firstNameEt.text != null && firstNameEt.text.toString().trim() != "" &&
                lastNameEt.text != null && lastNameEt.text.toString().trim() != ""
            ) {
                val user = User()
                user.email = emailEt.text.toString()
                user.password = passwordEt.text.toString()
                user.phone = phoneEt.text.toString()
                user.firstName = firstNameEt.text.toString()
                user.lastName = lastNameEt.text.toString()

                viewLifecycleOwner.lifecycleScope.launch {
                    val munoResponse = accountService.register(user).await()

                    if (munoResponse.errorMessage != null) {
                        AlertDialog.Builder(context).setTitle("Atentie!")
                            .setMessage(munoResponse.errorMessage)
                            .setPositiveButton("OK", null)
                            .create()
                            .show()

                        return@launch
                    }

                    val userIdMunoDatabaseObject = MunoDatabaseObject()
                    // save user id
                    userIdMunoDatabaseObject.key = "id"
                    userIdMunoDatabaseObject.value = munoResponse.value?.user?.id.toString()
                    SessionService(requireActivity().application).insert(userIdMunoDatabaseObject)

                    println("------------- REGISTER --------------")
                    println(munoResponse.value?.user?.id)
                    println(munoResponse.value?.token)
                    println(munoResponse.errorMessage)
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

    private fun changeNavigationContext() {
        val navigationView : NavigationView? = activity?.findViewById(R.id.nav_view)
        if (navigationView != null) {
            navigationView.menu.findItem(R.id.createProductFragment).isVisible = true
            navigationView.menu.findItem(R.id.viewProductListFragment).isVisible = true
            navigationView.menu.findItem(R.id.viewAccountFragment).isVisible = true
            navigationView.menu.findItem(R.id.loginFragment).isVisible = false
            navigationView.menu.findItem(R.id.createAccountFragment).isVisible = false
        }
    }
}