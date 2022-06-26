package com.example.YUmarket.model.homelist.category

import androidx.annotation.StringRes
import com.example.YUmarket.R

enum class HomeListCategory(
    @StringRes val categoryNameId: Int,
    @StringRes val categoryTypeId: Int
) {
    TOWN_MARKET(R.string.all, R.string.all_type),
    FOOD(R.string.food, R.string.food_type),
    MART(R.string.mart, R.string.mart_type),
    SERVICE(R.string.service, R.string.service_type),
    FASHION(R.string.fashion, R.string.fashion_type),
    ACCESSORY(R.string.accessory, R.string.accessory_type),
    ETC(R.string.cs_etc, R.string.cs_etc_type)
}