package com.example.YUmarket.screen.map

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.R
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.data.entity.shop.ShopInfoEntity
import com.example.YUmarket.data.repository.shop.ShopApiRepository
import com.example.YUmarket.screen.base.BaseViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapViewModel(
    private val shopApiRepository: ShopApiRepository
    ) : BaseViewModel() {

    private var naverMap: NaverMap? = null
    lateinit var destLocation: LocationLatLngEntity
    private var markers = mutableListOf<Marker>()

    private var shopList: MutableList<ShopInfoEntity> = mutableListOf()

    private val _data = MutableLiveData<MapState>(MapState.Uninitialized)
    val data: LiveData<MapState> = _data

    private var destMarker: Marker = Marker(
        MarkerIcons.BLACK
    ).apply {
        zIndex = 111
        iconTintColor = Color.parseColor("#FA295B")
        width = 100
        height = 125
    }
        get() = field.apply {
            position = LatLng(
                destLocation.latitude,
                destLocation.longitude
            )
        }

    fun setMarkers(list: List<Marker>) {
        markers.clear()
        markers = list as MutableList
    }

    fun setDestinationLocation(loc: LocationLatLngEntity) { destLocation = loc }
    fun getMarkers(): List<Marker>? { return markers }
    fun getMap(): NaverMap? { return naverMap }
    fun setMap(m: NaverMap) { naverMap = m }

    private fun deleteMarkers() {
        if (markers.isNullOrEmpty())
            return
        for (marker in markers!!) {
            marker.map = null
        }
    }

    fun firstupdateLocation() {
        naverMap?.cameraPosition = CameraPosition(
            LatLng(
                destLocation.latitude,
                destLocation.longitude
            ), 15.0
        )
        destMarker.map = naverMap
    }

    fun updateLocation(location: LocationLatLngEntity) {
        // 위치 업데이트 될 때마다 목적지 마커 초기화
        destLocation = location
        deleteMarkers()
        naverMap?.cameraPosition = CameraPosition(
            LatLng(
                destLocation.latitude,
                destLocation.longitude
            ), 15.0
        )
        destMarker.map = naverMap
    }

    fun calDist(lat1:Double, lon1:Double, lat2:Double, lon2:Double) : Long {
        val EARTH_R = 6371000.0
        val rad = Math.PI / 180
        val radLat1 = rad * lat1
        val radLat2 = rad * lat2
        val radDist = rad * (lon1 - lon2)

        var distance = Math.sin(radLat1) * Math.sin(radLat2)
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist)
        val ret = EARTH_R * Math.acos(distance)

        return Math.round(ret)
    }

    fun getCategoryNum(category: String): Int =
        when(category) {
            "FOOD_BEVERAGE" -> 0
            "SERVICE" -> 1
            "ACCESSORY" -> 2
            "MART" -> 3
            "FASHION" -> 4
            else -> 5
        }

    override fun fetchData(): Job = viewModelScope.launch {
        _data.value = MapState.Success(getShopEntityList())
    }

    fun getShopEntityList(): List<ShopInfoEntity>? {
        when (data.value) {
            is MapState.Success -> {
                return (data.value as MapState.Success).shopInfoList
            }
        }
        return null
    }

    /**
     * 카테고리에 맞는 아이콘과 색깔을 마커에 적용
     * @param marker 적용할 마커
     * @param category 마켓의 카테고리
     */
    fun setMarkerIconAndColor(marker: Marker, category: Int) = with(marker) {
        when (category) {
            0 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_m)
                iconTintColor = Color.parseColor("#46F5FF")
            }
            1 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_r)
                iconTintColor = Color.parseColor("#FFCB41")
            }
            2 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_s)
                iconTintColor = Color.parseColor("#886AFF")
            }
            3 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_e)
                iconTintColor = Color.parseColor("#04B404")
            }
            4 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_f)
                iconTintColor = Color.parseColor("#8A0886")
            }
            5 -> {
                icon = OverlayImage.fromResource(R.drawable.marker_f)
                iconTintColor = Color.parseColor("#0B2F3A")
            }
        }
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
            _data.value = MapState.Success(shopList)
        }
    }
}