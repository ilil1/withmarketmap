package com.example.YUmarket.screen.map

import androidx.annotation.StringRes
import com.example.YUmarket.data.entity.location.MapSearchInfoEntity
import com.example.YUmarket.data.entity.shop.ShopInfoEntity
import com.example.YUmarket.screen.MainState

sealed class ShopApiState {
    object Uninitialized : ShopApiState()
    object Loading : ShopApiState()

    data class Success(
        val shopInfoList: List<ShopInfoEntity>?
    ) : ShopApiState()

    data class Error(
        @StringRes val errorMessage: Int
    ) : ShopApiState()
}