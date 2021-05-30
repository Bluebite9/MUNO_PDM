package ro.pdm.muno_pdm.product.fragments

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
import ro.pdm.muno_pdm.product.service.ProductService
import ro.pdm.muno_pdm.utils.session.SessionService
import ro.pdm.muno_pdm.utils.shared.Validators
import ro.pdm.muno_pdm.product.models.Product

class CreateProductFragment : Fragment() {

    private val productService = ProductService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEt = view.findViewById<EditText>(R.id.nameEt)
        val descriptionEt = view.findViewById<EditText>(R.id.descriptionEt)
        val priceEt = view.findViewById<EditText>(R.id.priceEt)
        val unitEt = view.findViewById<EditText>(R.id.unitEt)

        view.findViewById<Button>(R.id.saveBt).setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val product = Product()
                product.name = nameEt.text.toString().trim()
                product.description = descriptionEt.text.toString().trim()
                product.price = priceEt.text.toString().toFloat()
                product.unit = unitEt.text.toString().trim()

                val munoValidateResoponse = Validators().validateProduct(product)

                if (!munoValidateResoponse.isValid) {
                    AlertDialog.Builder(context).setTitle("Atentie!")
                        .setMessage(munoValidateResoponse.message)
                        .setPositiveButton("OK", null)
                        .create()
                        .show()

                    return@launch
                }
                val token =
                    SessionService(requireActivity().application).get("token").await().value
                val user = User()
                user.id = SessionService(requireActivity().application).get("id").await().value?.toInt()
                product.user = user
                if (token != null) {
                    val munoResponse = productService.addProduct(product, token).await()
                    val bundle = Bundle()
                    bundle.putSerializable("productId", munoResponse.value?.id)
                    findNavController().navigate(R.id.viewProductFragment, bundle)
                }
            }
        }
    }
}