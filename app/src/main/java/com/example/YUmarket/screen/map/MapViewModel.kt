package com.example.YUmarket.screen.map

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.data.entity.shop.ShopInfoEntity
import com.example.YUmarket.data.repository.shop.ShopApiRepository
import com.example.YUmarket.screen.base.BaseViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
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

    private val _shopData = MutableLiveData<ShopApiState>(ShopApiState.Uninitialized)
    val shopData: LiveData<ShopApiState> = _shopData

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

    override fun fetchData(): Job = viewModelScope.launch {
        _shopData.value = ShopApiState.Success(getShopEntityList())
    }

    fun getShopEntityList(): List<ShopInfoEntity>? {
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
}