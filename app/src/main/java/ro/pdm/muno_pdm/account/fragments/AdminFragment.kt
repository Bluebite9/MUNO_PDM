package ro.pdm.muno_pdm.account.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.account.adaptor.AdminAdaptor
import ro.pdm.muno_pdm.account.models.User
import ro.pdm.muno_pdm.account.service.AccountService
import ro.pdm.muno_pdm.account.viewModel.AdminViewModel
import ro.pdm.muno_pdm.product.adaptor.MyProductsAdaptor
import ro.pdm.muno_pdm.product.models.Product
import ro.pdm.muno_pdm.product.service.ProductService
import ro.pdm.muno_pdm.product.viewModel.MyProductsViewModel
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.session.SessionService

class AdminFragment : Fragment() {

    private val viewModel: AdminViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.adminRecyclerView)

        viewLifecycleOwner.lifecycleScope.launch {
            val token = SessionService(requireActivity().application).get("token").await().value
            val accountService = AccountService()
            var munoResponse: MunoResponse<List<User>>? = null;

            if (token != null) {
                munoResponse = accountService.getUserList(token).await()
            }

            if (munoResponse?.errorMessage != null) {
                AlertDialog.Builder(context).setTitle("Atentie!")
                    .setMessage(munoResponse.errorMessage)
                    .setPositiveButton("OK", null)
                    .create()
                    .show()

                return@launch
            }

            viewModel.userList = munoResponse?.value
            recyclerView.adapter = AdminAdaptor(
                viewModel.userList as MutableList<User>?,
                viewLifecycleOwner,
                requireActivity().application
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)


        }
    }
}