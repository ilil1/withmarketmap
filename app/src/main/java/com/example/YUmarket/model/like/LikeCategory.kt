package com.example.YUmarket.model.like

import androidx.annotation.StringRes
import com.example.YUmarket.R

enum class LikeCategory(
    @StringRes val likeCategoryId: Int
) {
    MARKET(R.string.like_market),
    ITEM(R.string.like_item)
}