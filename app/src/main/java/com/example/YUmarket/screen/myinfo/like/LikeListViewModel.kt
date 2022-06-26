package com.example.YUmarket.screen.myinfo.like

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.YUmarket.data.repository.like.LikeListRepository
import com.example.YUmarket.model.Model
import com.example.YUmarket.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LikeListViewModel<T : Model>(
    private val likeListRepository: LikeListRepository<T>
) : BaseViewModel() {
    private val _likeData = MutableLiveData<LikeState>(LikeState.Uninitialized)
    val likeData: LiveData<LikeState> = _likeData

    override fun fetchData(): Job = viewModelScope.launch {
        _likeData.value = LikeState.Loading
        _likeData.value = LikeState.Success(
            modelList = likeListRepository.getAll()
        )
    }

    fun delete(model: T) = viewModelScope.launch {
        likeListRepository.delete(model)
        fetchData()
    }
}