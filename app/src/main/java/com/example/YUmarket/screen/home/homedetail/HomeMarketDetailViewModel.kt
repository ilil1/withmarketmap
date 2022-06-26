package com.example.YUmarket.screen.home.homedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.data.entity.home.TownMarketEntity
import com.example.YUmarket.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeMarketDetailViewModel(
    private val townMarketEntity: TownMarketEntity
) : BaseViewModel() {

    val HomeMarketDetailStateLiveData = MutableLiveData<HomeMarketDetailState>(HomeMarketDetailState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        HomeMarketDetailStateLiveData.value = HomeMarketDetailState.Success(
            townMarketEntity = townMarketEntity
        )
//        HomeMarketDetailStateLiveData.value = HomeMarketDetailState.Loading
    }

}