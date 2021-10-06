package com.example.androidlocation_aware.ui.map

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.androidlocation_aware.BuildConfig
import com.example.androidlocation_aware.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MapsFragment : Fragment() {
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_maps, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapViewModel = ViewModelProvider(this)
            .get(MapViewModel::class.java)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map
            // Add a marker in Sydney and move the camera
            val home = LatLng(37.68, -122.42)
            map.moveCamera(CameraUpdateFactory.zoomTo(17f))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 10f))

            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isTiltGesturesEnabled = false

            mapViewModel.allLocations.observe(viewLifecycleOwner, Observer {
                for (location in it) {
                    val point = LatLng(location.latitude, location.longitude)
                    val maker = map.addMarker(
                        MarkerOptions()
                            .position(point)
                            .title(location.title)
                            .snippet("Hours: ${location.hours}")
                            .icon(
                                getBitmapFromVector(
                                    R.drawable.ic_star_black_24dp,
                                    R.color.colorAccent
                                )
                            )
                            .alpha(75f)
                    )
                    maker?.tag = location.locationId
                    if (BuildConfig.DEBUG) {
                        map.addCircle(
                            CircleOptions().center(point).radius(location.geofenceRadius.toDouble()))
                    }
                }
                map.setOnInfoWindowClickListener { maker ->
                    val action =
                        MapsFragmentDirections.actionNavigationMapToNavigationLocation(maker.tag as Int)
                    val navigationController = Navigation.findNavController(requireView())
                    navigationController.navigate(action)
                }
            })
            enableMyLocation()
        }
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION)
    private fun enableMyLocation() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            googleMap.isMyLocationEnabled = true

        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.map_snackbar),
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.ok) {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.map_rationale),
                        RC_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
                .show()
        }
    }

    private fun getBitmapFromVector(
        @DrawableRes vectorResourceId: Int,
        @ColorRes colorResourceId: Int
    ): BitmapDescriptor {
        val vectorDrawable = resources.getDrawable(vectorResourceId, requireContext().theme)
            ?: return BitmapDescriptorFactory.defaultMarker()

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(
            vectorDrawable,
            ResourcesCompat.getColor(
                resources,
                colorResourceId, requireContext().theme
            )
        )
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        const val RC_LOCATION = 10
    }
}
