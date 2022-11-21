package com.example.mylocation.ui.location.views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mylocation.R
import com.example.mylocation.databinding.FragmentLocationBinding
import com.example.mylocation.domian.model.ConstantGeneral.Companion.DOUBLE_ZERO
import com.example.mylocation.domian.model.ConstantGeneral.Companion.MILISEG
import com.example.mylocation.domian.model.ConstantGeneral.Companion.MIN
import com.example.mylocation.domian.model.ConstantGeneral.Companion.VALID_PERMISSION
import com.example.mylocation.domian.model.ConstantGeneral.Companion.ZOOM
import com.example.mylocation.domian.model.ConstantGeneral.Companion.ZOOM_15F
import com.example.mylocation.ui.MainActivity
import com.example.mylocation.ui.component.Screen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : Fragment(), OnMapReadyCallback {

    var binding: FragmentLocationBinding? = null
    private lateinit var  mapFragment : SupportMapFragment
    private lateinit var map :GoogleMap
    var locationManager: LocationManager? = null
    var longitudeGPS = DOUBLE_ZERO
    var latitudeGPS:Double = DOUBLE_ZERO


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableMyLocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            binding = null
            (activity as MainActivity)
                .changeScreen(Screen.HomeFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLocationBinding.inflate(LayoutInflater.from(context),null,false)

        buildMapFragment()

        return binding?.root
    }

    private fun validatePermission(){
        if (isPermissionsGranted()) {
            map.isMyLocationEnabled = true
            getLocation()
        }else {
            requestLocationPermission()
        }
    }

    private fun getLocation(){
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (checkLocation()){
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN * MILISEG,
                ZOOM,
                locationListenerGPS)
        }
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled()) showIntentLocationEnable()
        return isLocationEnabled()
    }

    private fun showIntentLocationEnable() {
        Toast.makeText(requireContext(),getString(R.string.request_permission_gps), Toast.LENGTH_SHORT).show()
        val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(myIntent)
    }

    private fun isPermissionsGranted() = ContextCompat.checkSelfPermission(requireActivity(),
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private val locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            longitudeGPS = location.longitude
            latitudeGPS = location.latitude

            binding?.progressBar?.visibility = View.GONE
            binding?.tvCoordinates?.text = location.latitude.toString() + ", " + location.longitude.toString()

            MarketPoint(latitudeGPS,longitudeGPS)
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }

    private fun MarketPoint(latitud : Double,longitud:Double){
        val ubication = LatLng(latitud, longitud)
        map.addMarker(
            MarkerOptions()
                .position(ubication)
                .title(getString(R.string.msg)))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubication, ZOOM_15F))
    }

    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        validatePermission()
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(requireContext(), getString(R.string.request_permission), Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                VALID_PERMISSION)
        }
    }

    private fun buildMapFragment() {
        mapFragment  = requireActivity().supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            VALID_PERMISSION -> if(grantResults.isNotEmpty() && grantResults[VALID_PERMISSION] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
                getLocation()
            }else{ ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                VALID_PERMISSION)
                Toast.makeText(requireContext(), getString(R.string.msg_request_permission), Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        enableMyLocation()
    }

    companion object {
        @JvmStatic
        fun newInstance() = LocationFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentManager?.beginTransaction()?.remove(mapFragment)?.commit();
        binding = null
    }
}

