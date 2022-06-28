package com.example.YUmarket.screen.map

import android.graphics.Color
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.screen.base.BaseViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons

class MapViewModel : BaseViewModel() {

    private var naverMap: NaverMap? = null
    lateinit var destLocation: LocationLatLngEntity
    private var markers = mutableListOf<Marker>()

    private var destMarker: Marker = Marker(
        MarkerIcons.BLACK
    ).apply {
        zIndex = 111
        iconTintColor = Color.parseColor("#FA295B")
        width = 100
        height = 125
    }

    fun setMarkers(list: List<Marker>) {
        markers.clear()
        markers = list as MutableList
        //Log.d("FDSAFSDAFSD", markers.size.toString())
    }

    fun getMarkers(): List<Marker>? {
        return markers
    }

    fun setMap(m: NaverMap) {
        naverMap = m
    }

    private fun deleteMarkers() {
        if (markers.isNullOrEmpty())
            return
        for (marker in markers!!) {
            marker.map = null
        }
    }

    fun setDestinationLocation(loc: LocationLatLngEntity) {
        destLocation = loc
    }

    fun firstupdateLocation() {
        naverMap?.cameraPosition = CameraPosition(
            LatLng(
                destLocation.latitude,
                destLocation.longitude
            ), 15.0
        )
        destMarker.position = LatLng(destLocation.latitude, destLocation.longitude)
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
        destMarker.position = LatLng(destLocation.latitude, destLocation.longitude)
        destMarker.map = naverMap
    }
}