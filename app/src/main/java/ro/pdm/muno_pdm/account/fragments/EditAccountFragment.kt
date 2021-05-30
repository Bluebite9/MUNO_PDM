package ro.pdm.muno_pdm.account.fragments

import android.app.AlertDialog
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import ro.pdm.muno_pdm.account.service.LocationService
import ro.pdm.muno_pdm.utils.http.MunoResponse
import ro.pdm.muno_pdm.utils.session.SessionService
import java.io.IOException

class EditAccountFragment : Fragment(), OnMapReadyCallback {

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
        return inflater.inflate(R.layout.fragment_edit_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countySpinner = view.findViewById(R.id.countySpinner)
        citySpinner = view.findViewById(R.id.citiesSpinner)

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

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

            // populate county spinner
            val countyList = locationService.getCounties().await().value?.toMutableList()!!
            val countyNameList = countyList.stream().map { it.nume }.toArray()
            val countyAdapter: ArrayAdapter<Any> = ArrayAdapter(
                requireActivity().applicationContext,
                android.R.layout.simple_spinner_item,
                countyNameList
            )

            countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            countySpinner.adapter = countyAdapter

            // select the user county
            val selectedCountyIndex = countyNameList.indexOf(munoResponse.value?.county)
            countySpinner.setSelection(selectedCountyIndex)

            // populate the city spinner
            val selectedCountyAuto = countyList.first { it.nume == munoResponse.value?.county }.auto
            var cityList = locationService.getCities(selectedCountyAuto)
                .await().value?.toMutableList()!!
            var cityNameList = cityList.stream().map { it.nume }.toArray()
            var cityAdapter: ArrayAdapter<Any> = ArrayAdapter(
                requireActivity().applicationContext,
                android.R.layout.simple_spinner_item,
                cityNameList
            )
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            citySpinner.adapter = cityAdapter

            // select the user city
            val selectedCityIndex = cityNameList.indexOf(munoResponse.value?.city)
            citySpinner.setSelection(selectedCityIndex)

            // add select county event listener
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
                            cityList =
                                locationService.getCities(countyAuto)
                                    .await().value?.toMutableList()!!
                            cityNameList = cityList.stream().map { it.nume }.toArray()

                            cityAdapter = ArrayAdapter(
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

            // add select city event listener
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
                user.county = countySpinner.selectedItem.toString()
                user.city = citySpinner.selectedItem.toString()

                accountService.editUser(user, token).await()
                findNavController().popBackStack()
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