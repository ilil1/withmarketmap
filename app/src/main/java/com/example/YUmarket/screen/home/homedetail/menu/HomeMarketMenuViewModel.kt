package com.example.YUmarket.screen.home.homedetail.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.data.entity.home.TownMarketEntity
import com.example.YUmarket.data.repository.restaurant.HomeRepository
import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.homelist.HomeItemModel
import com.example.YUmarket.model.homelist.TownMarketModel
import com.example.YUmarket.model.homelist.category.HomeListCategory
import com.example.YUmarket.screen.base.BaseViewModel
import com.example.YUmarket.screen.home.homemain.HomeMainState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeMarketMenuViewModel(
    private val homeRepository: HomeRepository
    // TODO 22.01.18 add item repository
) : BaseViewModel() {

    private val _itemData = MutableLiveData<HomeMarketMenuState>(HomeMarketMenuState.Uninitialized)
    //외부에서 수정못하고 내부에서만 바뀌게 하고싶다.
    val itemData: LiveData<HomeMarketMenuState> = _itemData

    lateinit var allNewSaleItemsList: List<HomeItemModel>

    override fun fetchData(): Job = viewModelScope.launch {
        fetchItemData()
    }

    private suspend fun fetchItemData() {
        if (itemData.value !is HomeMarketMenuState.Success<*>) {
            _itemData.value = HomeMarketMenuState.Loading

            allNewSaleItemsList = homeRepository.getAllNewSaleItems().map {
                it.copy(type = CellType.HOME_DETAIL_ITEM_CELL)
            }
            _itemData.value = HomeMarketMenuState.ListLoaded

            //Log.d("TAG", "allNewSaleItemsList: ${allNewSaleItemsList}")
        }
    }

    fun reloadData(): Job {
        _itemData.value = HomeMarketMenuState.Loading
        return fetchData()
    }

    fun setItemFilterTest(saleList: TownMarketEntity) {
        if (::allNewSaleItemsList.isInitialized) {
            _itemData.value = HomeMarketMenuState.Success(
                // 임시로 CellType을 ViewModel에서 변경
                modelList = allNewSaleItemsList.filter {
                    it.townMarketModel.marketName == saleList.marketName
                },
                allSaleList = allNewSaleItemsList.filter {
                    val disCountedPrice = it.originalPrice - it.salePrice
                    (it.townMarketModel.marketName == saleList.marketName &&  disCountedPrice != 0)
                },
                saleList = allNewSaleItemsList.filter {
                    val disCountedPrice = it.originalPrice - it.salePrice
                    (it.townMarketModel.marketName == saleList.marketName && it.stockQuantity > 0 && disCountedPrice != 0)
                }
            )
        }
    }
}