package com.example.YUmarket.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.YUmarket.data.db.dao.AddressHistoryDao
import com.example.YUmarket.data.db.dao.BasketDao
import com.example.YUmarket.data.db.dao.LikeItemDao
import com.example.YUmarket.data.db.dao.LikeMarketDao
import com.example.YUmarket.data.entity.room.AddressHistoryEntity
import com.example.YUmarket.data.entity.room.BasketEntity
import com.example.YUmarket.data.entity.room.LikeItemEntity
import com.example.YUmarket.data.entity.room.LikeMarketEntity


@Database(
    entities = [
        BasketEntity::class, LikeItemEntity::class, LikeMarketEntity::class,
        AddressHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class YUMarketDB : RoomDatabase() {

    abstract val basketDao: BasketDao
    abstract val likeMarketDao: LikeMarketDao
    abstract val likeItemDao: LikeItemDao
    abstract val addressHistoryDao: AddressHistoryDao

    companion object {
        const val DB_NAME = "YUMarketDB.db"
    }
}