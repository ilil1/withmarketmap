package com.example.YUmarket.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.YUmarket.data.db.dao.LikeItemDao
import com.example.YUmarket.data.entity.room.LikeItemEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class LikeItemDaoTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var database: YUMarketDB
    private lateinit var likeItemDao: LikeItemDao

    private val testList = (1..5).map {
        LikeItemEntity(
            id = it.toLong(),
            itemName = "item$it",
            marketName = "market$it",
            marketDistance = it.toFloat(),
            saleRatio = it,
            price = it,
            imageUrl = "image$it"
        )
    }

    @Before
    fun setUp() {
        database =
            Room.inMemoryDatabaseBuilder(context, YUMarketDB::class.java).build()

        likeItemDao = database.likeItemDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllTest_returnCorrect() = runBlocking {
        var result = likeItemDao.getAll()
        assertThat(result).isEmpty()

        for (likeItemEntity in testList) {
            likeItemDao.insert(likeItemEntity.copy(id = 0))
        }

        result = likeItemDao.getAll()
        assertThat(result).isEqualTo(testList.reversed())
    }

    @Test
    fun deleteTest_returnCorrect() = runBlocking {
        for (likeItemEntity in testList) {
            likeItemDao.insert(likeItemEntity.copy(id = 0))
        }

        var result = likeItemDao.getAll()
        assertThat(result).isEqualTo(testList.reversed())

        likeItemDao.delete(testList[0])
        result = likeItemDao.getAll()
        assertThat(result).isEqualTo(testList.drop(1).reversed())
    }

    @Test
    fun deleteAllTest_returnEmpty() = runBlocking {
        for (likeItemEntity in testList) {
            likeItemDao.insert(likeItemEntity.copy(id = 0))
        }

        var result = likeItemDao.getAll()
        assertThat(result).isEqualTo(testList.reversed())

        likeItemDao.deleteAll()
        result = likeItemDao.getAll()
        assertThat(result).isEmpty()
    }

}