package com.example.YUmarket.model.store

import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.Model

data class StoreItemModel(
    override val id: Long,
    val name: String,
    val review : Int,
    val menu : String,
    val distance: String,
    val url :String,
    override val type: CellType = CellType.STORE_CELL  // TODO 수정
) : Model(id, type)