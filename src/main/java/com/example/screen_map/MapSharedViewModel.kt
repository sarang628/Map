package com.example.screen_map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.torang_core.data.model.*
import com.example.torang_core.repository.MapSharedRepository
import com.example.torang_core.util.Event
import com.example.torang_core.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 최초 진입 시 내위치와 가장 가까운 맛집이 선택된다.
 *
 * 검색 결과 첫번째 식당이 선택된다.
 */

@HiltViewModel
class MapSharedViewModel @Inject constructor(val repository: MapSharedRepository) : ViewModel() {

    private val _mapMode = MutableLiveData<MapMode>(MapMode.FINDING)
    val mapMode: LiveData<MapMode> = _mapMode

    var isMoved: Boolean = false;

    /** 권한 체크 플레그 */
    private val _checkPermission = MutableLiveData<Boolean>().apply {
        value = true
    }
    val checkPermission: LiveData<Boolean> = _checkPermission

    /** 최초 진입 플레그 */
    private val _requestLocation = MutableLiveData<Boolean>().apply {
        value = true
    }
    val requestLocation: LiveData<Boolean> = _requestLocation

    /** 현재 맛집 리스트 */
    private val _restaurants = MutableLiveData<List<RestaurantData>>()
    val restaurants: LiveData<List<RestaurantData>> = _restaurants

    /** 필터와 하단 정보 보여지는 여부 */
    val isExpended = MutableLiveData<Boolean>()

    /** 검색어 */
    private val _searchKeyword = MutableLiveData<String>()
    val searchKeyword: LiveData<String> = _searchKeyword

    /** 현재 선택된 맛집 포지션 */
    val currentRestaurantPosition = MutableLiveData<Int>()

    /** 위치 클릭 리스너 */
    private val _clickMyLocation = MutableLiveData<Event<Unit>>()
    val clickMyLocation: LiveData<Event<Unit>> = _clickMyLocation

    /** 내 위치 */
    private val _myLocation = MutableLiveData<Location>();
    val myLocation: LiveData<Location> = _myLocation

    /** 업로드 할 식당 정보 */
    private val _selectedRestaurant = MutableLiveData<RestaurantData>()
    val selectedRestaurant: LiveData<RestaurantData> = _selectedRestaurant

    /** 업로드 할 식당 정보 */
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg


    /**
     * 맛집 검색
     *
     * 맛집 검색 결과 시 첫번째 결과를 활성화 합니다.
     */
    fun search(keyword: String) {
        _searchKeyword.postValue(keyword)
        viewModelScope.launch {
            try {
                _restaurants.postValue(repository.searchRestaurant(keyword))
                currentRestaurantPosition.postValue(0)
            } catch (e: Exception) {
                Logger.d(e.toString())
            }
        }
    }

    /**
     * 현재 맵 필터 하단정보가 보여지는지 상태
     */
    private fun setIsExpended(isExpended: Boolean) {
        this.isExpended.value = isExpended
    }

    /**
     * 맵 클릭
     */
    fun mapClick() {
        var value = false
        if (isExpended.value != null) value = isExpended.value!!
        setIsExpended(!value)
    }

    /**
     * 맵 필터 하단 정보 보여지는 여부
     */
    fun mapExpand(b: Boolean) {
        isExpended.value = b
    }

    fun selectPosition(position: Int) {
        Logger.v(
            "select position $position" +
                    " current position  = ${currentRestaurantPosition.value}"
        )
        if (currentRestaurantPosition.value != null ||
            currentRestaurantPosition.value != position
        ) {
            currentRestaurantPosition.value = position
        }
    }

    /**
     * 위치 검색
     */
    private fun searchRestaurant(latitude: Double, longitude: Double) {
        searchFilterRestaurant(
            distances = Distances._300M,
            latitude = latitude,
            longitude = longitude,
            searchType = Filter.SearchType.AROUND
        )

        viewModelScope.launch {
            try {
                //_restaurants.postValue(repository.searchRestaurant(latitude, longitude))
            } catch (e: Exception) {
                Logger.e(e.toString())
            }
        }
    }

    /**
     * 내 위치 설정
     * 내 위치 설정시 최초 설정이라면 내 주변에 식당을 검색 한다.
     */
    fun setLocation(result: Location) {
        if (_myLocation.value == null) {
            searchRestaurant(result.latitude, result.longitude)
        }
        _myLocation.postValue(result)
    }

    /**
     * 내위치 클릭
     */
    fun clickMyLocation() {
        _clickMyLocation.value = Event(Unit)
    }

    /**
     * 내 위치 확인
     */
    fun confirmRequestLocation() {
        _requestLocation.postValue(false)
    }

    /**
     * 위치권한 확인
     */
    fun confirmCheckPermission() {
        _checkPermission.postValue(false)
    }

    fun selectRestaurant(restaurantData: RestaurantData) {
        _selectedRestaurant.postValue(restaurantData)
    }

    /**
     * 필터 검색
     */
    fun searchFilterRestaurant(
        distances: Distances? = null,
        restaurantType: ArrayList<RestaurantType>? = null,
        prices: Prices? = null,
        ratings: ArrayList<Ratings>? = null,
        latitude: Double = 0.0,
        longitude: Double = 0.0,
        northEastLatitude: Double = 0.0,
        northEastLongitude: Double = 0.0,
        southWestLatitude: Double = 0.0,
        southWestLongitude: Double = 0.0,
        searchType: Filter.SearchType
    ) {
        viewModelScope.launch {
            try {
                val filter = Filter().apply {
                    distances?.let { this.distances = it }
                    restaurantType?.let { this.restaurantTypes = restaurantType }
                    prices?.let { this.prices = prices }
                    ratings?.let { this.ratings = ratings }
                    this.lat = latitude
                    this.lon = longitude
                    this.searchType = searchType
                    this.northEastLongitude = northEastLongitude
                    this.northEastLatitude = northEastLatitude
                    this.southWestLongitude = southWestLongitude
                    this.southWestLatitude = southWestLatitude
                }
                _restaurants.postValue(repository.getFilterRestaurant(filter))

            } catch (e: Exception) {
                _errorMsg.postValue(e.toString())
            }
        }
    }

    fun clickTravelMode() {
        if (_mapMode.value == MapMode.FINDING) {
            _mapMode.postValue(MapMode.TRAVEL)
        } else {
            _mapMode.postValue(MapMode.FINDING)
        }
    }
}