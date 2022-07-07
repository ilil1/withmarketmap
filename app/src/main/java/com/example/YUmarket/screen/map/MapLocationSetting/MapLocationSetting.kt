package com.example.YUmarket.screen.map.MapLocationSetting

import androidx.annotation.StringRes
import com.example.YUmarket.data.entity.location.MapSearchInfoEntity

sealed class MapLocationSetting {
    object Uninitialized : MapLocationSetting()
    object Loading : MapLocationSetting()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ) : MapLocationSetting()

    data class Error(
        @StringRes val errorMessage: Int
    ) : MapLocationSetting()
}
