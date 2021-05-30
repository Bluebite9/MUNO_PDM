package ro.pdm.muno_pdm.account.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.account.models.User
import ro.pdm.muno_pdm.account.service.AccountService
import ro.pdm.muno_pdm.product.adaptor.MyProductsAdaptor
import ro.pdm.muno_pdm.product.models.Product
import ro.pdm.muno_pdm.product.service.ProductService
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.session.SessionService

class AdminFragment : Fragment() {

    private lateinit var user: User;
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

//            view.findViewById<TextView>(R.id.firstNameTv).text =
//                user.firstName ?: ""
//            view.findViewById<TextView>(R.id.lastNameTv).text =
//                user.lastName ?: ""
//            view.findViewById<TextView>(R.id.phoneTv).text =
//                user.phone ?: ""
//            view.findViewById<TextView>(R.id.emailTv).text =
//                user.email ?: ""
//            view.findViewById<TextView>(R.id.countyTv).text =
//                user.county ?: ""
//            view.findViewById<TextView>(R.id.cityTv).text =
//                user.city ?: ""

            val userId = SessionService(requireActivity().application).get("id").await().value
            val accountService = AccountService();
            val munoResponse = MunoResponse<User>()
            munoResponse.value = accountService.getUserById(userId.toString());

        }
    }
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val recyclerView = view.findViewById<RecyclerView>(R.id.myProductsRecyclerView)

    viewLifecycleOwner.lifecycleScope.launch {
        val userId = SessionService(requireActivity().application).get("id").await().value
        val token = SessionService(requireActivity().application).get("token").await().value
        val productService = ProductService()
        var munoResponse: MunoResponse<List<Product>>? = null
        if (userId != null) {
            if (token != null) {
                munoResponse = productService.getMyProducts(userId, token).await()
            }
        }

        if (munoResponse?.errorMessage != null) {
            AlertDialog.Builder(context).setTitle("Atentie!")
                .setMessage(munoResponse.errorMessage)
                .setPositiveButton("OK", null)
                .create()
                .show()

            return@launch
        }

        viewModel.productList = munoResponse?.value
        recyclerView.adapter = MyProductsAdaptor(
            viewModel.productList as MutableList<Product>?,
            viewLifecycleOwner,
            requireActivity().application
        )
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }
}
}