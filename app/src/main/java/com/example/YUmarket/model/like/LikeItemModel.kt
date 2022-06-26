package com.example.YUmarket.model.like

import com.example.YUmarket.data.entity.room.LikeItemEntity
import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.Model

data class LikeItemModel(
    override val id: Long,
    val itemName: String,
    val marketName: String,
    val marketDistance: Float,
    val saleRatio: Int,
    val price: Int,
    val imageUrl: String
) : Model(id, CellType.LIKE_ITEM_CELL) {
    companion object {
        fun fromEntity(entity: LikeItemEntity) =
            LikeItemModel(
                id = entity.id,
                itemName = entity.itemName,
                marketName = entity.marketName,
                marketDistance = entity.marketDistance,
                saleRatio = entity.saleRatio,
                price = entity.price,
                imageUrl = entity.imageUrl
            )
    }
}
