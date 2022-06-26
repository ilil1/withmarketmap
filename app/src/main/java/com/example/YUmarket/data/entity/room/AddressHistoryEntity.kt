package com.example.YUmarket.data.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AddressHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val lat: Double,
    val lng: Double
)
