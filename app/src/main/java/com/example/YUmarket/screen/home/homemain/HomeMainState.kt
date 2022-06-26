package com.example.YUmarket.screen.home.homemain


import androidx.annotation.StringRes
import com.example.YUmarket.model.Model

sealed class HomeMainState {
    object Uninitialized : HomeMainState()

    object Loading : HomeMainState()

    object ListLoaded : HomeMainState()

    data class Success<T : Model>(
        val modelList: List<T>
    ) : HomeMainState()

    data class Error(
        @StringRes val errorMessage: Int
    ) : HomeMainState()
}