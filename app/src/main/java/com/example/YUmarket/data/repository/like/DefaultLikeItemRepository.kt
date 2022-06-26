package com.example.YUmarket.data.repository.like

import com.example.YUmarket.data.db.dao.LikeItemDao
import com.example.YUmarket.data.entity.room.LikeItemEntity
import com.example.YUmarket.model.like.LikeItemModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultLikeItemRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val likeItemDao: LikeItemDao
) : LikeListRepository<LikeItemModel> {
    override suspend fun getAll(): List<LikeItemModel> = withContext(ioDispatcher) {
        likeItemDao.getAll().map(LikeItemModel::fromEntity)
    }

    override suspend fun insert(model: LikeItemModel) = withContext(ioDispatcher) {
        likeItemDao.insert(toEntity(model, 0L))
    }

    override suspend fun delete(model: LikeItemModel) = withContext(ioDispatcher) {
        likeItemDao.delete(toEntity(model))
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        likeItemDao.deleteAll()
    }

    private fun toEntity(model: LikeItemModel, id: Long = model.id) =
        LikeItemEntity(
            id = id,
            itemName = model.itemName,
            marketName = model.marketName,
            marketDistance = model.marketDistance,
            saleRatio = model.saleRatio,
            price = model.price,
            imageUrl = model.imageUrl
        )
}