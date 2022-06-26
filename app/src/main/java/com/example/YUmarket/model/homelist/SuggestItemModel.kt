package com.example.YUmarket.model.homelist


import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.Model
import com.example.YUmarket.model.homelist.category.SuggestCategory

data class SuggestItemModel(
    override val id: Long,
    override val type: CellType = CellType.SUGGEST_CELL,
    val marketImageUrl : String,
    val marketName: String,
    val distance:Float,
    val category: SuggestCategory
):Model(id, type)