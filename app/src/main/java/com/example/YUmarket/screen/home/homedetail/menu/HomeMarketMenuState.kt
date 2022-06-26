package com.example.YUmarket.screen.home.homedetail.menu

import androidx.annotation.StringRes
import com.example.YUmarket.model.Model

sealed class HomeMarketMenuState {
    object Uninitialized : HomeMarketMenuState()

    object Loading : HomeMarketMenuState()

    object ListLoaded : HomeMarketMenuState()

    data class Success<T : Model>(
        val modelList: List<T>,
        val allSaleList : List<T>,
        val saleList : List<T>,
    ) : HomeMarketMenuState()

    data class Error(
        @StringRes val errorMessage: Int
    ) : HomeMarketMenuState()
}
