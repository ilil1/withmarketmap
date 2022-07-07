package com.example.YUmarket.screen.map

import androidx.annotation.StringRes
import com.example.YUmarket.data.entity.shop.ShopInfoEntity
import com.example.YUmarket.model.map.MapMarketModel

sealed class MapState {
    object Uninitialized : MapState()
    object Loading : MapState()

    data class Success(
        val shopInfoList: List<ShopInfoEntity>?
    ) : MapState()

    data class Error(
        @StringRes val id: Int
    ) : MapState()
}
