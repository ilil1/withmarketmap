package com.example.YUmarket.screen

import android.location.Location
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.R
import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.data.entity.location.MapSearchInfoEntity
import com.example.YUmarket.screen.base.BaseViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.data.repository.map.MapApiRepository
import com.example.YUmarket.screen.map.MapState
import kotlinx.coroutines.launch


class MainViewModel(
    private val mapApiRepository: MapApiRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {

    private val _locationData = MutableLiveData<MainState>(MainState.Uninitialized)
    val locationData: LiveData<MainState> = _locationData

    private lateinit var destLocation: LocationLatLngEntity

    lateinit var curLocation: Location

    fun setCurrentLocation(loc: Location) { curLocation = loc }

    fun getCurrentLocation() : Location { return curLocation }

    fun setDestinationLocation(loc: LocationLatLngEntity) { destLocation = loc }

    fun getDestinationLocation(): LocationLatLngEntity { return destLocation }

    @Suppress("CAST_NEVER_SUCCEEDS")
    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (locationData.value) {
            is MainState.Success -> {
                return (locationData.value as MainState.Success).mapSearchInfoEntity
            }
        }
        return null
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
}