package com.example.YUmarket.screen.map.MapLocationSetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.R
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.data.entity.location.MapSearchInfoEntity
import com.example.YUmarket.data.repository.map.MapApiRepository
import com.example.YUmarket.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class MapLocationSettingViewModel(
    private val mapApiRepository: MapApiRepository)
    : BaseViewModel() {

    private val _mapLocation = MutableLiveData<MapLocationSetting>(MapLocationSetting.Uninitialized)
    val mapLocation: LiveData<MapLocationSetting> = _mapLocation

    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (val data = mapLocation.value) {
            is MapLocationSetting.Success -> {
                return data.mapSearchInfoEntity
            }
        }
        return null
    }

    fun getReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {

        val currentLocation = locationLatLngEntity
        val addressInfo = mapApiRepository.getReverseGeoInformation(locationLatLngEntity)

        addressInfo?.let { _addressInfo ->
            _mapLocation.value = MapLocationSetting.Success(
                MapSearchInfoEntity(
                    fullAddress = _addressInfo.fullAddress ?: "주소 정보 없음",
                    name = _addressInfo.buildingName ?: "주소 정보 없음",
                    locationLatLng = currentLocation
                )
            )
        } ?: kotlin.run {
            R.string.cannot_load_address_info
        }
    }
}