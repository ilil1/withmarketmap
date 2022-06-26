package com.example.YUmarket.model.homelist.category

import androidx.annotation.StringRes
import com.example.YUmarket.R

enum class HomeMarketDetailCategory(
    @StringRes val categoryNameId: Int
) {
    MENU(R.string.menu)
    ,INFORM(R.string.shopinform)
    ,REVIEW(R.string.review)
}