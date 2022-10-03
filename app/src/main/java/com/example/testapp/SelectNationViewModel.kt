package com.example.testapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sryang.torang_core.data.NationItem
import com.sryang.torang_core.data.NationLocation
import com.sryang.torang_repository.data.entity.RestaurantEntity
import com.sryang.torang_repository.repository.NationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectNationViewModel @Inject constructor(val nationRepository: NationRepository) :
    ViewModel() {
    private val _selectdNation = MutableLiveData<NationItem>()
    val selected: LiveData<NationItem> = _selectdNation

    /** 현재 맛집 리스트 */
    private val _restaurants = MutableLiveData<List<RestaurantEntity>>()
    val restaurants: LiveData<List<RestaurantEntity>> = _restaurants

    fun select(nationItem: NationItem) {
        _selectdNation.postValue(nationItem)
    }

    fun findRestaurant(
        latitudeNorthWest: Double,
        latitudeSouthEast: Double,
        longitudeNorthWest: Double,
        longitudeSouthEast: Double
    ) {
        viewModelScope.launch {
            delay(7000)
            try {
                /*val filter = Filter().apply {
                    searchType = SearchType.BOUND
                }*/
                //_restaurants.postValue(nationRepository.getFilterRestaurant(filter))

            } catch (e: Exception) {

            }
        }
    }

    private val _nationItems = MutableLiveData<ArrayList<NationItem>>().apply {
        value = ArrayList()
        value.apply {
            this?.add(
                NationItem(
                    R.drawable.brazil,
                    "brazil",
                    NationLocation(-15.783454397264483, -47.874050758127595)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.canada,
                    "canada",
                    NationLocation(45.4250860262407, -75.70045694798073)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.china,
                    "china",
                    NationLocation(39.88255421230174, 116.4064242999756)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.greece,
                    "greece",
                    NationLocation(37.98892837826318, 23.732605497802364)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.hongkong,
                    "hongkong",
                    NationLocation(22.29753969263335, 114.17215632753884)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.hungary,
                    "hungary",
                    NationLocation(47.49631879682233, 19.03992565978393)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.india,
                    "india",
                    NationLocation(28.61427711934326, 77.19935203335108)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.indonesia,
                    "indonesia",
                    NationLocation(-6.172737100934231, 106.83500496378585)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.japan,
                    "japan",
                    NationLocation(35.685355894040114, 139.7529295567316)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.laos,
                    "laos",
                    NationLocation(17.97104559449185, 102.61856095543412)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.malaysia,
                    "malaysia",
                    NationLocation(3.1383303434154786, 101.68698887532281)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.mexico,
                    "mexico",
                    NationLocation(19.422144328843867, -99.18067296180162)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.namibia,
                    "namibia",
                    NationLocation(-22.567800331187698, 17.087158484978882)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.netherlands,
                    "netherlands",
                    NationLocation(52.35880710045552, 4.879654220583688)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.newzealand,
                    "new zealand",
                    NationLocation(-41.290141953404685, 174.78214237751914)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.norway,
                    "norway",
                    NationLocation(59.91743263567293, 10.728297479063489)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.philippines,
                    "philippines",
                    NationLocation(14.599136612998063, 120.97544020140991)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.portugal,
                    "portugal",
                    NationLocation(38.71246927681961, -9.139329905738416)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.romania,
                    "romania",
                    NationLocation(44.428383664232136, 26.0879440789849)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.sanmarino,
                    "san marino",
                    NationLocation(43.93490774407734, 12.447414340844801)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.southkorea,
                    "south korea",
                    NationLocation(37.55136363234369, 126.99115206585131)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.sweden,
                    "sweden",
                    NationLocation(59.32715024864944, 18.071433368065797)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.switzerland,
                    "switzerland",
                    NationLocation(46.9489662118128, 7.4499490734826965)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.thailand,
                    "thailand",
                    NationLocation(13.776973444553207, 100.49853863654901)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.ukraine,
                    "ukraine",
                    NationLocation(50.437748897015965, 30.553636048252706)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.uruguay,
                    "uruguay",
                    NationLocation(-34.89933713637825, -56.1963900948514)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.usa,
                    "usa",
                    NationLocation(38.892021555011965, -77.04887800729838)
                )
            )
            this?.add(
                NationItem(
                    R.drawable.vietnam,
                    "vietnam",
                    NationLocation(10.78387890704643, 106.67841904951607)
                )
            )
        }
    }
    val nationItems: LiveData<ArrayList<NationItem>> = _nationItems
}