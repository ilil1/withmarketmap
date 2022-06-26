package com.example.YUmarket.model.homelist.category

import androidx.annotation.StringRes
import com.example.YUmarket.R

enum class SuggestCategory (
    @StringRes val categoryNameId: Int,
    @StringRes val categoryTypeid: Int
)
{
    HOBBY(R.string.hobby,R.string.hobby_type),
    SEASON_SPRING(R.string.season_spring,R.string.season_spring_type),
    ANNIVERSARY(R.string.anniversary,R.string.anniversary_type),
    FIX(R.string.fix,R.string.fix_type),
    BIRTH(R.string.birth,R.string.birth_type)

}