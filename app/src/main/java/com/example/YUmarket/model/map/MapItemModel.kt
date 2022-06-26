package com.example.YUmarket.model.map

import android.os.Parcelable
import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.Model
import kotlinx.parcelize.Parcelize


// 가게마다 여러개의 MapItemModel
@Parcelize
data class MapItemModel(
    override val id: Long,
    val originalPrice: Int,
    val discountRate: Int,
    val stockQuantity: Int,
    val itemName: String,
    // val branch : String,
    val itemImageUrl: String,
    // 소속 가게
    override val type: CellType = CellType.MAP_ITEM_CELL
) : Model(id, type), Parcelable