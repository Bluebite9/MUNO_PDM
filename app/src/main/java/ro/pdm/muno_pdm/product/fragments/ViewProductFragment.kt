package ro.pdm.muno_pdm.product.fragments

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
import ro.pdm.muno_pdm.product.service.ProductService
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.session.SessionService

class ViewProductFragment : Fragment() {

    private val productService = ProductService()
    private val accountService = AccountService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // populate product fields
        val productId = requireArguments()["productId"] as Int

        viewLifecycleOwner.lifecycleScope.launch {
            val munoResponse = productService.getProduct(productId).await()

            if (munoResponse.errorMessage != null) {
                AlertDialog.Builder(context).setTitle("Atentie!")
                    .setMessage(munoResponse.errorMessage)
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
            } else {
                view.findViewById<TextView>(R.id.productNameTv).text =
                    munoResponse.value?.name ?: ""
                view.findViewById<TextView>(R.id.productDescriptionTv).text =
                    munoResponse.value?.description ?: ""
                view.findViewById<TextView>(R.id.productPriceTv).text =
                    munoResponse.value?.price.toString()
                view.findViewById<TextView>(R.id.productUnitTv).text =
                    munoResponse.value?.unit ?: ""
                view.findViewById<TextView>(R.id.firstNameTv).text =
                    munoResponse.value?.user?.firstName ?: ""
                view.findViewById<TextView>(R.id.lastNameTv).text =
                    munoResponse.value?.user?.lastName ?: ""
                view.findViewById<TextView>(R.id.phoneTv).text =
                    munoResponse.value?.user?.phone ?: ""
                view.findViewById<TextView>(R.id.emailTv).text =
                    munoResponse.value?.user?.email ?: ""

                // hide edit button if logged user is not the parent user
                val userId =
                    SessionService(requireActivity().application).get("id").await().value?.toLong()
                val token = SessionService(requireActivity().application).get("token").await().value
                var userMunoResponse = MunoResponse<User>()
                if (token != null) {
                    userMunoResponse =
                        userId?.let { accountService.getUserById(it, token).await() }!!
                }
                if (userMunoResponse.errorMessage != null) {
                    AlertDialog.Builder(context).setTitle("Atentie!")
                        .setMessage(userMunoResponse.errorMessage)
                        .setPositiveButton("OK", null)
                        .create()
                        .show()
                } else {
                    val editButton = view.findViewById<Button>(R.id.editProductBtn)
                    if (munoResponse.value?.user?.id != userMunoResponse.value?.id) {
                        editButton.visibility = View.GONE
                    } else {
                        editButton.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putSerializable("productId", munoResponse.value?.id)
                            findNavController().navigate(R.id.editProductFragment, bundle)
                        }
                    }
                }
            }
        }
    }
}