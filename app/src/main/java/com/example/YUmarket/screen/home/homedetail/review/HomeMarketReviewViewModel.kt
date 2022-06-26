package com.example.YUmarket.screen.home.homedetail.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.data.repository.home.HomeReviewRepository
import com.example.YUmarket.model.homelist.HomeReviewModel
import com.example.YUmarket.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeMarketReviewViewModel(
    private val homeReviewRepository: HomeReviewRepository
    // TODO 22.01.18 add item repository
) : BaseViewModel() {

    private val _chatListData = MutableLiveData<List<HomeReviewModel>>()
    val chatListData: LiveData<List<HomeReviewModel>> = _chatListData

    override fun fetchData(): Job = viewModelScope.launch {
        _chatListData.value = homeReviewRepository.findChatList()
    }
}