package com.example.YUmarket.data.repository.like

import android.util.Log
import com.example.YUmarket.data.db.dao.LikeMarketDao
import com.example.YUmarket.data.entity.room.LikeMarketEntity
import com.example.YUmarket.model.like.LikeMarketModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultLikeMarketRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val likeMarketDao: LikeMarketDao
) : LikeListRepository<LikeMarketModel> {
    override suspend fun getAll(): List<LikeMarketModel> = withContext(ioDispatcher) {
        likeMarketDao.getAll().map(LikeMarketModel::fromEntity)
    }

    override suspend fun insert(model: LikeMarketModel) = withContext(ioDispatcher) {
        likeMarketDao.insert(toEntity(model, 0L))
    }

    override suspend fun delete(model: LikeMarketModel) = withContext(ioDispatcher) {
        likeMarketDao.delete(toEntity(model))
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        likeMarketDao.deleteAll()
    }

    private fun toEntity(model: LikeMarketModel, id: Long = model.id) =
        LikeMarketEntity(
            id = id,
            marketName = model.marketName,
            distance = model.distance,
            isOpen = model.isOpen,
            imageUrl = model.imageUrl
        )
}