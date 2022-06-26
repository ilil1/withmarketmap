package com.example.YUmarket.screen.myinfo.like

import androidx.annotation.StringRes
import com.example.YUmarket.model.Model

sealed class LikeState {
    object Uninitialized : LikeState()
    object Loading : LikeState()

    data class Success<T : Model>(
        val modelList: List<T>
    ) : LikeState()

    data class Error(
        @StringRes val resId: Int
    ) : LikeState()
}
