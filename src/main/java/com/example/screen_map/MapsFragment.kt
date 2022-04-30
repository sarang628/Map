package com.example.screen_map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.screen_map.databinding.FragmentMapsBinding
import com.example.torang_core.util.EventObserver
import com.example.torang_core.util.ITorangLocationManager
import com.example.torang_core.util.Logger
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * @See MapViewModel
 * [FragmentMapsBinding]
 */
@AndroidEntryPoint
class MapsFragment : Fragment()/*, OnMapReadyCallback*/ {
    @Inject
    lateinit var locationManager: ITorangLocationManager

    /** 뷰모델 */
    private val mViewModel: MapViewModel by activityViewModels()

    /** 공유 뷰모델 */
    private val mapSharedViewModel: MapSharedViewModel by activityViewModels()

    /** 마커 리스트 */
    private val markers = ArrayList<Marker>()

    /** 반경 변경 시 마지막 그려진 원을 지우기 위한 변수 */
    private var lastCircle: Circle? = null

    private var isMoving = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        val fragment = childFragmentManager.findFragmentById(R.id.map)
        (fragment as SupportMapFragment).getMapAsync() {
            onMapReady(it)
        }
        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun subScribeUI(googleMap: GoogleMap) {
        //검색 반경 변경 시
        /*filterViewModel.selectedDistances.observe(viewLifecycleOwner) {
            lastCircle?.remove()
            lastCircle = googleMap.drawCircle(mapSharedViewModel.myLocation.value, it)
        }*/

        //레스토링 리스트 변경 시
        mapSharedViewModel.restaurants.observe(viewLifecycleOwner) {
            //googleMap.clear()
            for (marker in markers) {
                Logger.v(marker)
                marker.remove()
            }
            markers.removeAll(markers)
            for (restaurantData in it) {
                try {
                    val markerOption = MarkerOptions().title(restaurantData.restaurant_name)
                        .position(LatLng(restaurantData.lat!!, restaurantData.lon!!))
                        .icon(getRestaurantIcon1(restaurantData.restaurant_type))
                    markers.add(googleMap.addMarker(markerOption))
                } catch (e: Exception) {

                }
            }
        }

        //내위치 클릭 시
        mapSharedViewModel.clickMyLocation.observe(viewLifecycleOwner, EventObserver {
            if (hasLocationPermission()) {
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = false

                if (locationManager.getLastLatitude() == 0.0) {
                    locationManager.requestLocation()

                    locationManager.setOnLocationListener {
                        renewLocation(googleMap)
                    }
                } else {
                    renewLocation(googleMap)
                }
            }
        })

        //맛집 포커스 변경 시
        mapSharedViewModel.currentRestaurantPosition.observe(viewLifecycleOwner) {
            try {
                if (!mapSharedViewModel.isMoved)
                    moveCamera(googleMap, markers[it])
            } catch (e: Exception) {
            }
        }

        mapSharedViewModel.checkPermission.observe(viewLifecycleOwner) {
            if (it && !hasLocationPermission()) {
                showLocationDialog()
            }

            if (it) {
                mapSharedViewModel.confirmCheckPermission()
            }
        }

        mViewModel.cameraUpdate.observe(viewLifecycleOwner) {
            moveCamera(googleMap, it)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mViewModel.selectdNationItem.collect {
                    Logger.d(it.toString())
                    it.nationLocation?.let {
                        moveCamera(googleMap, it.lat, it.lon)
                    }
                }
            }
        }

        mapSharedViewModel.requestLocation.observe(viewLifecycleOwner) {
            if (it) {
                Logger.d("init request my location")
                mapSharedViewModel.clickMyLocation()
                mapSharedViewModel.confirmRequestLocation()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mViewModel.clickMap.collect {
                    Logger.d(it.toString())
                }
            }
        }
    }


    fun onMapReady(googleMap: GoogleMap) {
        //지도에 줌 화면 추가하기
        googleMap.uiSettings.isZoomControlsEnabled = true

        googleMap.setOnMarkerClickListener(onMarkerClickListener)
        googleMap.setOnMapClickListener {
            mViewModel.clickMap()
        }

        googleMap.setOnCameraIdleListener {
            mViewModel.setNorthEastLatitude(googleMap.projection.visibleRegion.latLngBounds.northeast.latitude)
            mViewModel.setNorthEastLongitude(googleMap.projection.visibleRegion.latLngBounds.northeast.longitude)
            mViewModel.setSouthWestLatitude(googleMap.projection.visibleRegion.latLngBounds.southwest.latitude)
            mViewModel.setSouthWestLongitude(googleMap.projection.visibleRegion.latLngBounds.southwest.longitude)
            mapSharedViewModel.isMoved = true
        }

        subScribeUI(googleMap)
    }


    private fun renewLocation(googleMap: GoogleMap) {
        moveCamera(
            googleMap,
            locationManager.getLastLatitude(),
            locationManager.getLastLongitude(),
        )

        mapSharedViewModel.setLocation(Location("").apply {
            latitude = locationManager.getLastLatitude()
            longitude = locationManager.getLastLongitude()
        })
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1
        )
    }

    private fun moveCamera(map: GoogleMap?, latitute: Double, longituge: Double) {
        map?.let {
            isMoving = true
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitute, longituge), 16f),
                object : GoogleMap.CancelableCallback {
                    override fun onFinish() {
                        isMoving = false
                    }

                    override fun onCancel() {
                        isMoving = false
                    }
                })
        }
    }

    private fun moveCamera(map: GoogleMap?, cameraUpdate: CameraUpdate) {
        map?.let {
            isMoving = true
            it.animateCamera(cameraUpdate, object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    isMoving = false
                }

                override fun onCancel() {
                    isMoving = false
                }
            })
        }
    }

    private fun moveCamera(map: GoogleMap, marker: Marker) {
        if (isMoving) {
            Logger.d("camera is moving")
            return
        }

        map.animateCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    marker.position.latitude,
                    marker.position.longitude
                )
            )
        )
        marker.showInfoWindow()
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showLocationDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("지도 검색 기능을 사용하기위해서는 위치권한을 필요로 합니다. 위치권한을 허용하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                requestPermission()
            }
            .setNegativeButton("아니오", null)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.ACCESS_COARSE_LOCATION
                    &&
                    grantResults[i] == PackageManager.PERMISSION_GRANTED
                ) {
                    Logger.d("permission granted! request my location")
                    mapSharedViewModel.clickMyLocation()
                }
            }
        }
    }

    /** 마커 클릭 리스너 */
    private val onMarkerClickListener = OnMarkerClickListener {
        if (mapSharedViewModel.isExpended.value != null && mapSharedViewModel.isExpended.value!!) {
            mapSharedViewModel.mapExpand(
                false
            )
        }
        //선택한 마커의 포지션을 공유 뷰모델로 전달
        mapSharedViewModel.selectPosition(markers.indexOf(it))
        false
    }

    private fun getRestaurantIcon(restaurantType: String?): String {
        if ("한식" == restaurantType) {
            return "ic_kimbap"
        } else if ("양식" == restaurantType) {
            return "ic_worldwide"
        }
        return "ic_sushi"
    }

    private fun resizeBitmap(drawableName: String?, width: Int, height: Int): Bitmap? {
        val imageBitmap = BitmapFactory.decodeResource(
            resources,
            resources.getIdentifier(drawableName, "drawable", requireContext().getPackageName())
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    private fun getRestaurantIcon1(restaurantType: String?): BitmapDescriptor {
        return BitmapDescriptorFactory.fromBitmap(
            resizeBitmap(
                getRestaurantIcon(restaurantType), 100, 100
            )
        )
    }
}