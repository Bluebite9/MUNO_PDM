package ro.pdm.muno_pdm.product.fragments

import android.app.AlertDialog
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.account.models.User
import ro.pdm.muno_pdm.account.service.AccountService
import ro.pdm.muno_pdm.product.service.ProductService
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.session.SessionService
import java.io.IOException

class ViewProductFragment : Fragment(), OnMapReadyCallback {

    private val productService = ProductService()
    private val accountService = AccountService()

    private lateinit var mapView: MapView
    private lateinit var user: User

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

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        viewLifecycleOwner.lifecycleScope.launch {
            val munoResponse = productService.getProduct(productId).await()

            if (munoResponse.errorMessage != null) {
                AlertDialog.Builder(context).setTitle("Atentie!")
                    .setMessage(munoResponse.errorMessage)
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
            } else {
                user = munoResponse.value?.user!!

                view.findViewById<TextView>(R.id.productNameTv).text =
                    munoResponse.value?.name ?: ""
                view.findViewById<TextView>(R.id.productDescriptionTv).text =
                    munoResponse.value?.description ?: ""
                view.findViewById<TextView>(R.id.productPriceTv).text =
                    munoResponse.value?.price.toString()
                view.findViewById<TextView>(R.id.productUnitTv).text =
                    munoResponse.value?.unit ?: ""
                view.findViewById<TextView>(R.id.firstNameTv).text =
                    user.firstName ?: ""
                view.findViewById<TextView>(R.id.lastNameTv).text =
                    user.lastName ?: ""
                view.findViewById<TextView>(R.id.phoneTv).text =
                    user.phone ?: ""
                view.findViewById<TextView>(R.id.emailTv).text =
                    user.email ?: ""
                view.findViewById<TextView>(R.id.countyTv).text =
                    user.county ?: ""
                view.findViewById<TextView>(R.id.cityTv).text =
                    user.city ?: ""

                geoLocate()

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

    override fun onMapReady(p0: GoogleMap) {

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView.onResume()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    private fun geoLocate() {
        println("---------- GEOLOCATION VIEW PRODUCT ----------")

        val county = user.county
        val city = user.city

        val geocoder = Geocoder(requireActivity())
        var list: MutableList<Address> = mutableListOf()

        try {
            list = geocoder.getFromLocationName("$county, $city", 1)
        } catch (e: IOException) {
            println(e.message)
        }

        if (list.size > 0) {
            val address = list[0]
            println("geoLocate: found a location: $address")
            val latLng = LatLng(address.latitude, address.longitude)
            val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(12F)
                .build()

            mapView.getMapAsync {
                it.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }
}