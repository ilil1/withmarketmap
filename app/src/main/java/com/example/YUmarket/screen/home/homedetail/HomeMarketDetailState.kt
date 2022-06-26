package com.example.YUmarket.screen.home.homedetail

import com.example.YUmarket.data.entity.home.TownMarketEntity
import kotlinx.coroutines.delay

sealed class HomeMarketDetailState {

    object Uninitialized: HomeMarketDetailState()

    object Loading : HomeMarketDetailState()

    data class Success(
        val townMarketEntity: TownMarketEntity
    ): HomeMarketDetailState()

}
