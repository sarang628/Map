package com.example.screen_map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.screen_map.databinding.FragmentMapsBinding
import com.example.torang_core.data.model.Restaurant
import com.example.torang_core.util.ITorangLocationManager
import com.example.torang_core.util.Logger
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * @See MapViewModel
 * [FragmentMapsBinding]
 */
@AndroidEntryPoint
class MapsFragment : Fragment() {
    @Inject
    lateinit var locationManager: ITorangLocationManager

    /** 뷰모델 */
    private val viewModel: MapViewModel by viewModels()

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
        (fragment as SupportMapFragment).getMapAsync {
            onMapReady(it)
        }
        return binding.root
    }

    @OptIn(InternalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    private fun subScribeUI(googleMap: GoogleMap) {
        //검색 반경 변경 시
        /*filterViewModel.selectedDistances.observe(viewLifecycleOwner) {
            lastCircle?.remove()
            lastCircle = googleMap.drawCircle(mapSharedViewModel.myLocation.value, it)
        }*/

        viewModel.cameraUpdate.observe(viewLifecycleOwner) {
            moveCamera(googleMap, it)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.map {
                    it.currentLocation
                }.distinctUntilChanged()
                    .collect(FlowCollector {
                        it?.let {
                            moveCamera(googleMap, it.latitude, it.longitude, 12f)
                        }
                    })
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect {
                    if (it.requestMyLocation) {
                        requestMyLocation(googleMap)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.map { it.searchedRestaurants }
                    .distinctUntilChanged()
                    .collect {
                        markRestaurnats(googleMap, it)
                        moveMarker(googleMap, 0)
                    }
            }
        }

        //포커스된 맛집으로 위치이동
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.map { it.position }
                    .distinctUntilChanged()
                    .collect {
                        Logger.d("move position: ${it}")
                        moveMarker(googleMap, it)
                    }
            }
        }
    }

    /**
     * 내 위치 요청하기
     */
    private fun requestMyLocation(googleMap: GoogleMap) {
        Logger.d("requestMyLocation")
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
    }


    private fun onMapReady(googleMap: GoogleMap) {
        //지도에 줌 화면 추가하기
        googleMap.uiSettings.isZoomControlsEnabled = true

        googleMap.setOnMarkerClickListener(onMarkerClickListener)
        googleMap.setOnMapClickListener {
            viewModel.clickMap()
        }

        googleMap.setOnCameraIdleListener {
            viewModel.setNorthEastLatitude(googleMap.projection.visibleRegion.latLngBounds.northeast.latitude)
            viewModel.setNorthEastLongitude(googleMap.projection.visibleRegion.latLngBounds.northeast.longitude)
            viewModel.setSouthWestLatitude(googleMap.projection.visibleRegion.latLngBounds.southwest.latitude)
            viewModel.setSouthWestLongitude(googleMap.projection.visibleRegion.latLngBounds.southwest.longitude)
        }

        subScribeUI(googleMap)
    }


    private fun renewLocation(googleMap: GoogleMap) {
        viewModel.onReceiveLocation() // 위치를 받으면 뷰모델에 위치 받았다고 전달
        moveCamera(
            googleMap,
            locationManager.getLastLatitude(),
            locationManager.getLastLongitude(),
        )
    }

    private fun moveCamera(
        map: GoogleMap?,
        latitute: Double,
        longituge: Double,
        zoomLevel: Float = 16f
    ) {
        map?.let {
            isMoving = true
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latitute,
                    longituge
                ), zoomLevel
            ),
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

        Logger.d("move ${marker.title}")

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
        val b = ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        Logger.d("hasLocationPermission : $b")
        return b
    }

    /** 마커 클릭 리스너 */
    private
    val onMarkerClickListener = OnMarkerClickListener {
        //선택한 마커의 포지션을 공유 뷰모델로 전달
        viewModel.selectPosition(markers.indexOf(it))
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

    private fun resizeBitmap(
        drawableName: String?,
        width: Int,
        height: Int
    ): Bitmap? {
        val imageBitmap = BitmapFactory.decodeResource(
            resources,
            resources.getIdentifier(
                drawableName,
                "drawable",
                requireContext().packageName
            )
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

    private fun markRestaurnats(
        googleMap: GoogleMap,
        restaurants: List<Restaurant>
    ) {
        for (marker in markers) {
            marker.remove()
        }
        markers.removeAll(markers)
        for (restaurant in restaurants) {
            try {
                val markerOption =
                    MarkerOptions().title(restaurant.restaurant_name)
                        .position(LatLng(restaurant.lat, restaurant.lon))
                        .icon(getRestaurantIcon1(restaurant.restaurant_type.name))
                markers.add(googleMap.addMarker(markerOption))
            } catch (e: Exception) {

            }
        }
    }

    private fun moveMarker(googleMap: GoogleMap, position: Int) {
        if (position < markers.size)
            moveCamera(googleMap, markers[position])
    }
}