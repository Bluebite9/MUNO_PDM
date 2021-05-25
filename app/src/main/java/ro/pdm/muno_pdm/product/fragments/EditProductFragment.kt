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
import ro.pdm.muno_pdm.product.service.ProductService
import ro.pdm.muno_pdm.utils.session.SessionService

class EditProductFragment : Fragment() {

    private val productService = ProductService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product, container, false)
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
                val nameEt = view.findViewById<EditText>(R.id.nameEt)
                nameEt.text.insert(0, munoResponse.value?.name)
                val descriptionEt = view.findViewById<EditText>(R.id.descriptionEt)
                descriptionEt.text.insert(0, munoResponse.value?.description)
                val priceEt = view.findViewById<EditText>(R.id.priceEt)
                priceEt.text.insert(
                    0,
                    munoResponse.value?.price.toString()
                )
                val unitEt = view.findViewById<EditText>(R.id.unitEt)
                unitEt.text.insert(0, munoResponse.value?.unit)

                view.findViewById<Button>(R.id.saveBt).setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val product = munoResponse.value

                        // TODO validations!!!

                        product?.name = nameEt.text.toString()
                        product?.description = descriptionEt.text.toString()
                        product?.price = priceEt.text.toString().toFloat()
                        product?.unit = unitEt.text.toString()

                        val token =
                            SessionService(requireActivity().application).get("token").await().value
                        if (product != null) {
                            if (token != null) {
                                productService.editProduct(product, token).await()
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}