package com.example.YUmarket.data.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikeMarketEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val marketName: String,
    val distance: Float,
    val isOpen: Boolean,
    val imageUrl: String
)

