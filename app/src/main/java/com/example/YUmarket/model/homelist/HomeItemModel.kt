package com.example.YUmarket.model.homelist

import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.Model
import com.example.YUmarket.model.homelist.category.HomeListCategory
import com.example.YUmarket.model.homelist.category.HomeListDetailCategory

data class HomeItemModel(
    override val id: Long,
    val homeListCategory: HomeListCategory,
    val homeListDetailCategory: HomeListDetailCategory,
    val itemImageUrl: String,
    val townMarketModel: TownMarketModel,
    val itemName: String,
    val originalPrice: Int,
    val salePrice: Int,
    val stockQuantity: Int,
    val likeQuantity: Int,
    val reviewQuantity: Int,
    override val type: CellType = CellType.HOME_ITEM_CELL
): Model(id, type) {

    /**
     * [Model.isTheSame]을 Override
     */
    override fun isTheSame(item: Model) =
        if (item is HomeItemModel) {
            super.isTheSame(item) && this.homeListCategory == item.homeListCategory
        } else {
            false
        }
}