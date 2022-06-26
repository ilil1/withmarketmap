package com.example.YUmarket.data.entity.home

import android.os.Parcelable
import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.homelist.category.HomeListCategory
import com.example.YUmarket.model.homelist.category.HomeListDetailCategory
import kotlinx.android.parcel.Parcelize


@Parcelize
data class HomeItemEntity(
    val id: Long,
    val homeListCategory: HomeListCategory,
    val homeListDetailCategory: HomeListDetailCategory,
    val itemImageUrl: String,
    val itemName: String,
    val originalPrice: Int,
    val salePrice: Int,
    val stockQuantity: Int,
    val likeQuantity: Int,
    val reviewQuantity: Int,
    val type: CellType = CellType.HOME_ITEM_CELL
) : Parcelable



