package ro.pdm.muno_pdm.account.fragments

import android.app.AlertDialog
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.account.models.User
import ro.pdm.muno_pdm.account.service.AccountService
import ro.pdm.muno_pdm.account.service.LocationService
import ro.pdm.muno_pdm.utils.session.MunoDatabaseObject
import ro.pdm.muno_pdm.utils.session.SessionService
import java.io.IOException
import ro.pdm.muno_pdm.utils.shared.Validators

class CreateAccountFragment : Fragment(), OnMapReadyCallback {

    private val accountService = AccountService()
    private val locationService = LocationService()

    private lateinit var mapView: MapView
    private lateinit var countySpinner: Spinner
    private lateinit var citySpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)

        val emailEt: EditText = view.findViewById(R.id.emailEt)
        val passwordEt: EditText = view.findViewById(R.id.passwordEt)
        val confirmPasswordEt: EditText = view.findViewById(R.id.confirmPasswordEt)
        val phoneEt: EditText = view.findViewById(R.id.phoneEt)
        val firstNameEt: EditText = view.findViewById(R.id.firstNameEt)
        val lastNameEt: EditText = view.findViewById(R.id.lastNameEt)
        val registerBt: Button = view.findViewById(R.id.registerBt)
        countySpinner = view.findViewById(R.id.countySpinner)
        citySpinner = view.findViewById(R.id.citiesSpinner)

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        viewLifecycleOwner.lifecycleScope.launch {
            val countyList = locationService.getCounties().await().value?.toMutableList()!!
            val countyNameList = countyList.stream().map { it.nume }.toArray()

            val countyAdapter: ArrayAdapter<Any> = ArrayAdapter(
                requireActivity().applicationContext,
                android.R.layout.simple_spinner_item,
                countyNameList
            )

            countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            countySpinner.adapter = countyAdapter

            countySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
                AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val countyName = countyNameList[position]

                    if (countyName != null) {
                        val countyAuto =
                            countyList.stream().filter { it.nume == countyName }.findFirst()
                                .get().auto

                        viewLifecycleOwner.lifecycleScope.launch {
                            val cityList =
                                locationService.getCities(countyAuto)
                                    .await().value?.toMutableList()!!

                            val cityNameList = cityList.stream().map { it.nume }.toArray()

                            val cityAdapter: ArrayAdapter<Any> = ArrayAdapter(
                                requireActivity().applicationContext,
                                android.R.layout.simple_spinner_item,
                                cityNameList
                            )

                            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            citySpinner.adapter = cityAdapter
                        }

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

            citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
                AdapterView.OnItemClickListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    geoLocate()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    TODO("Not yet implemented")
                }

            }
        }

        registerBt.setOnClickListener {
            val user = User()
            //user is populated
            user.email = emailEt.text.toString().trim()
            user.password = passwordEt.text.toString().trim()
            user.phone = phoneEt.text.toString().trim()
            user.firstName = firstNameEt.text.toString().trim()
            user.lastName = lastNameEt.text.toString().trim()
            user.city = citySpinner.selectedItem.toString().trim()
            user.county = countySpinner.selectedItem.toString().trim()

            viewLifecycleOwner.lifecycleScope.launch {
                //user input is validated

                val munoValidateResoponse = Validators().validateUser(user)

                if(confirmPasswordEt.text.toString() != passwordEt.text.toString()) {
                    AlertDialog.Builder(context).setTitle("Atentie!")
                        .setMessage("parola nu este identica")
                        .setPositiveButton("OK", null)
                        .create()
                        .show()

                    return@launch
                }
                if (!munoValidateResoponse.isValid) {
                    AlertDialog.Builder(context).setTitle("Atentie!")
                        .setMessage(munoValidateResoponse.message)
                        .setPositiveButton("OK", null)
                        .create()
                        .show()

                    return@launch
                }
                if (!munoValidateResoponse.isValid) {
                    AlertDialog.Builder(context).setTitle("Atentie!")
                        .setMessage(munoValidateResoponse.message)
                        .setPositiveButton("OK", null)
                        .create()
                        .show()

                    return@launch
                }
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

    private fun changeNavigationContext() {
        val navigationView: NavigationView? = activity?.findViewById(R.id.nav_view)
        if (navigationView != null) {
            navigationView.menu.findItem(R.id.createProductFragment).isVisible = true
            navigationView.menu.findItem(R.id.viewProductListFragment).isVisible = true
            navigationView.menu.findItem(R.id.viewAccountFragment).isVisible = true
            navigationView.menu.findItem(R.id.loginFragment).isVisible = false
            navigationView.menu.findItem(R.id.createAccountFragment).isVisible = false
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
        println("---------- GEOLOCATION CREATE ACCOUNT ----------")

        val selectedCounty = countySpinner.selectedItem.toString()
        val selectedCity = citySpinner.selectedItem.toString()

        val geocoder = Geocoder(requireActivity())
        var list: MutableList<Address> = mutableListOf()

        try {
            list = geocoder.getFromLocationName("$selectedCity, $selectedCounty", 1)
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