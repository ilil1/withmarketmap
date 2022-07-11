package com.example.YUmarket.screen.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.data.repository.store.StoreRepository
import com.example.YUmarket.model.store.StoreItemModel
import com.example.YUmarket.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StoreViewModel(
    private val storeRepository: StoreRepository
):BaseViewModel(){
    private val _storeListData = MutableLiveData<List<StoreItemModel>>()
    val storeListData : LiveData<List<StoreItemModel>> = _storeListData

    override fun fetchData(): Job = viewModelScope.launch {
        _storeListData.value = storeRepository.findStore()
    }
}