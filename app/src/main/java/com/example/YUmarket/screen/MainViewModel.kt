package com.example.YUmarket.screen

import android.graphics.Color
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.R
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.data.entity.location.MapSearchInfoEntity
import com.example.YUmarket.data.repository.map.MapRepository
import com.example.YUmarket.screen.base.BaseViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.data.entity.shop.ShopInfoEntity
import com.example.YUmarket.data.repository.map.MapApiRepository
import com.example.YUmarket.data.repository.shop.ShopApiRepository
import com.example.YUmarket.data.response.shop.ShopData
import com.example.YUmarket.screen.home.homemain.HomeMainState
import com.example.YUmarket.screen.map.MapState
import com.example.YUmarket.screen.map.ShopApiState
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainViewModel(
    private val mapApiRepository: MapApiRepository,
    private val shopApiRepository: ShopApiRepository,
    private val resourcesProvider: ResourcesProvider,
    private val mapRepository: MapRepository
) : BaseViewModel() {

    var shopList: MutableList<ShopInfoEntity> = mutableListOf()

    private var map: NaverMap? = null

    private val _locationData = MutableLiveData<MainState>(MainState.Uninitialized)
    val locationData: LiveData<MainState> = _locationData

    private val _shopData = MutableLiveData<ShopApiState>(ShopApiState.Uninitialized)
    val shopData: LiveData<ShopApiState> = _shopData

    lateinit var destLocation: LocationLatLngEntity
    lateinit var curLocation: Location
    private var markers = mutableListOf<Marker>()

    private var destMarker: Marker = Marker(
        MarkerIcons.BLACK
    ).apply {
        zIndex = 111
        iconTintColor = Color.parseColor("#FA295B")
        width = 100
        height = 125
    }
        /**
         * destMarker를 가져올 때마다 위치를 destLocation으로 설정
         */
        get() = field.apply {
            position = LatLng(
                destLocation.latitude,
                destLocation.longitude
            )
        }

    fun setCurrentLocation(loc: Location) {
        curLocation = loc
    }

    fun getCurrentLocation(): Location {
        return curLocation
    }

    fun setDestinationLocation(loc: LocationLatLngEntity) {
        destLocation = loc
    }

    fun getDestinationLocation(): LocationLatLngEntity {
        return destLocation
    }

    fun getDestinationMarker(): Marker {
        return destMarker
    }

    fun setDestinationMarker(dest: Marker) {
        destMarker = dest
    }

    private val _data = MutableLiveData<MapState>(MapState.Uninitialized)
    val data: LiveData<MapState> = _data

    override fun fetchData(): Job = viewModelScope.launch {
        _shopData.value = ShopApiState.Success(getShopEntityList())
    }

    fun getMap(): NaverMap? {
        return map
    }

    fun setMap(m: NaverMap) {
        map = m
    }

    fun setMarkers(list: List<Marker>) {
        markers.clear()
        markers = list as MutableList
        //Log.d("FDSAFSDAFSD", markers.size.toString())
    }

    fun getMarkers(): List<Marker>? {
        return markers
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (locationData.value) {
            is MainState.Success -> {
                return (locationData.value as MainState.Success).mapSearchInfoEntity
            }
        }
        return null
    }

    fun getShopEntityList(): List<ShopInfoEntity>?
    {
        when (shopData.value) {
            is ShopApiState.Success -> {
                return (shopData.value as ShopApiState.Success).shopInfoList
            }
        }
        return null
    }

    fun getApiShopList() = viewModelScope.launch {
        val list = shopApiRepository.getShopList()?.shopList

        list?.let { shopInfoResult ->
            for (i: Int in 0..list.size - 1) {
                shopList.add(
                    ShopInfoEntity(
                        shop_id = shopInfoResult[i].shop_id,
                        shop_name = shopInfoResult[i].shop_name,
                        is_open = shopInfoResult[i].is_open,
                        lot_number_address = shopInfoResult[i].lot_number_address,
                        road_name_address = shopInfoResult[i].road_name_address,
                        latitude = shopInfoResult[i].latitude,
                        longitude = shopInfoResult[i].longitude,
                        average_score = shopInfoResult[i].average_score,
                        review_number = shopInfoResult[i].review_number,
                        main_image = shopInfoResult[i].main_image,
                        description = shopInfoResult[i].description,
                        category = shopInfoResult[i].category,
                        detail_category = shopInfoResult[i].detail_category,
                        is_branch = shopInfoResult[i].is_branch,
                        branch_name = shopInfoResult[i].branch_name
                    )
                )
            }

            _shopData.value = ShopApiState.Success(shopList)
        }


    }

    fun getReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {

        val currentLocation = locationLatLngEntity

        val addressInfo = mapApiRepository.getReverseGeoInformation(locationLatLngEntity)

        addressInfo?.let { addressInfoResult ->
            _locationData.value = MainState.Success(
                mapSearchInfoEntity = MapSearchInfoEntity(
                    fullAddress = addressInfoResult.fullAddress
                        ?: resourcesProvider.getString(R.string.no_address_info_found),
                    name = addressInfoResult.buildingName
                        ?: resourcesProvider.getString(R.string.no_address_info_found),
                    locationLatLng = currentLocation
                ),
                isLocationSame = false
            )
        } ?: MainState.Error(
            R.string.cannot_load_address_info
        )
    }

    private fun deleteMarkers() {

        if (markers.isNullOrEmpty())
            return

        for (marker in markers!!) {
            marker.map = null
        }
    }

    public fun updateLocation(location: LocationLatLngEntity) {
        // 위치 업데이트 될 때마다 목적지 마커 초기화
        destLocation = location

        deleteMarkers()

        map?.cameraPosition = CameraPosition(
            LatLng(
                destLocation.latitude,
                destLocation.longitude
            ), 15.0
        )

        destMarker.map = map
    }
}