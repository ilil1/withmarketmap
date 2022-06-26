package com.example.YUmarket.data.db.dao


import androidx.room.*
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.YUmarket.data.entity.room.LikeItemEntity
import com.example.YUmarket.data.entity.room.LikeMarketEntity

@Dao
interface LikeMarketDao {
    // 최근에 추가한 항목이 위로 오도록
    @Query("select * from LikeMarketEntity order by id desc")
    suspend fun getAll(): List<LikeMarketEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insert(item: LikeMarketEntity)

    @Delete
    suspend fun delete(item: LikeMarketEntity)

    @Query("delete from LikeMarketEntity")
    suspend fun deleteAll()
}