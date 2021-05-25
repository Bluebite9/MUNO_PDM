package ro.pdm.muno_pdm.account.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.account.models.User
import ro.pdm.muno_pdm.account.service.AccountService
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.session.SessionService

class ViewAccountFragment : Fragment() {

    private val accountService = AccountService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val userId =
                SessionService(requireActivity().application).get("id").await().value?.toLong()!!
            val token = SessionService(requireActivity().application).get("token").await().value
            var munoResponse = MunoResponse<User>()
            if (token != null) {
                munoResponse =
                    userId.let { accountService.getUserById(it, token).await() }
            }

            if (munoResponse.errorMessage != null) {
                AlertDialog.Builder(context).setTitle("Atentie!")
                    .setMessage(munoResponse.errorMessage)
                    .setPositiveButton("OK", null)
                    .create()
                    .show()

                return@launch
            }

            view.findViewById<TextView>(R.id.firstNameTv).text =
                munoResponse.value?.firstName ?: ""
            view.findViewById<TextView>(R.id.lastNameTv).text =
                munoResponse.value?.lastName ?: ""
            view.findViewById<TextView>(R.id.phoneTv).text =
                munoResponse.value?.phone ?: ""
            view.findViewById<TextView>(R.id.emailTv).text =
                munoResponse.value?.email ?: ""
        }

        view.findViewById<Button>(R.id.editBtn).setOnClickListener {
            findNavController().navigate(R.id.editAccountFragment)
        }
    }
}