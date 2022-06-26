package com.example.YUmarket.model.homelist

import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.Model

data class HomeReviewModel(
    override val id: Long,
    override val type: CellType = CellType.HOME_REVIEW_ITEM_CELL,
    val title : String = "",
    val content : String = "",
    val itemName : String = "",
    val rating: Int = 0
) : Model(id, type) {

}