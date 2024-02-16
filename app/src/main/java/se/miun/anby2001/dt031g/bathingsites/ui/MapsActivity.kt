package se.miun.anby2001.dt031g.bathingsites.ui

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import se.miun.anby2001.dt031g.bathingsites.R
import se.miun.anby2001.dt031g.bathingsites.database.BathingSite
import se.miun.anby2001.dt031g.bathingsites.databinding.ActivityMapsBinding
import se.miun.anby2001.dt031g.bathingsites.models.BathingSiteViewModel

/**
 * maps activity used to display google map
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var bathingSite: BathingSite
    private lateinit var bathingLocation: LatLng
    private val viewModel: BathingSiteViewModel by viewModels()

    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // toolbar config
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Selected bathing site from resourceView
        bathingSite = intent.getParcelableExtra("bathingSite")!!
    }

    // displays menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }

    /*
    Creates functions for the items on the option menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_nearby -> {
                calcNearby()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        this.title = bathingSite.name
        bathingLocation = LatLng(bathingSite.latitude, bathingSite.longitude)
        mMap.addMarker(MarkerOptions().position(bathingLocation).title(bathingSite.name).snippet(bathingSite.address))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bathingLocation, 12f))


        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener {
            this.title = ""
            bathingLocation = it
            mMap.addMarker(MarkerOptions().position(bathingLocation))
        }
    }

    /**
     * calculates locations within 25km
     */
    private fun calcNearby() {
        mMap.clear() // clears map
        drawLocation() // draw location



        // gets all locations and sets markers for them withing 25km radius
        viewModel.bathingSites.observeForever { bathingSites ->
            bathingSites.forEach { bathSite ->
                val currentLatLng = LatLng(bathSite.latitude, bathSite.longitude)
                if (SphericalUtil.computeDistanceBetween(bathingLocation, currentLatLng) < 25000) { // locations within 25km
                    marker = mMap.addMarker(MarkerOptions().position(currentLatLng).title(bathSite.name).snippet(bathSite.address))
                    marker?.tag = bathSite
                }
            }
        }
    }



    /**
     * updates the bathingSite with selected marker
     */
    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker.tag != null) { // makes sure marker got a tag
            bathingSite = marker.tag as BathingSite
            this.title = bathingSite.name
            //Marks current location
            bathingLocation = LatLng(bathingSite.latitude, bathingSite.longitude)
            mMap.addMarker(MarkerOptions().position(bathingLocation).title(bathingSite.name).snippet(bathingSite.address))
        }
        return false
    }

    /**
     * marks the current location and bathing sites within 25km with a circle to mark the 25km area
     */
    private fun drawLocation(){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bathingLocation, 9f))
        mMap.addMarker(MarkerOptions().position(bathingLocation))


        // creates circle that displays a 25km radius
        val circle = CircleOptions()
            .center(bathingLocation)
            .radius(25000.0) // 25km radius
            .strokeWidth(10f)
            .strokeColor(Color.RED)
            .fillColor(Color.argb(30, 255, 0, 0))
        mMap.addCircle(circle)
    }

}