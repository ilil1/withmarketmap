package com.example.YUmarket.screen.map.MapLocationSetting

import androidx.annotation.StringRes
import com.example.YUmarket.data.entity.location.MapSearchInfoEntity

sealed class MapLocationSettingState {
    object Uninitialized : MapLocationSettingState()
    object Loading : MapLocationSettingState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ) : MapLocationSettingState()

    data class Error(
        @StringRes val errorMessage: Int
    ) : MapLocationSettingState()
}
