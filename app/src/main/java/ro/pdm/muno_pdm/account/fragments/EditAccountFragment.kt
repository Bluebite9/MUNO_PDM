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
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.account.models.User
import ro.pdm.muno_pdm.account.service.AccountService
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.session.SessionService

class EditAccountFragment : Fragment() {

    private val accountService = AccountService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstNameEt = view.findViewById<EditText>(R.id.firstNameEt)
        var munoResponse: MunoResponse<User> = MunoResponse()
        val lastNameEt = view.findViewById<EditText>(R.id.lastNameEt)
        var userId = 0
        var token = ""

        viewLifecycleOwner.lifecycleScope.launch {
            userId =
                SessionService(requireActivity().application).get("id").await().value?.toInt()!!
            token =
                SessionService(requireActivity().application).get("token").await().value!!
            munoResponse = accountService.getUserById(userId.toLong(), token).await()

            if (munoResponse.errorMessage != null) {
                AlertDialog.Builder(context).setTitle("Atentie!")
                    .setMessage(munoResponse.errorMessage)
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
            }

            firstNameEt.text.insert(0, munoResponse.value?.firstName)
            lastNameEt.text.insert(0, munoResponse.value?.lastName)
        }

        view.findViewById<Button>(R.id.saveBt).setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val user = User()

                user.id = userId
                user.email = munoResponse.value?.email
                user.password = munoResponse.value?.password
                user.phone = munoResponse.value?.phone
                user.role = munoResponse.value?.role
                user.firstName = firstNameEt.text.toString()
                user.lastName = lastNameEt.text.toString()

                accountService.editUser(user, token).await()
                findNavController().popBackStack()
            }
        }
    }
}