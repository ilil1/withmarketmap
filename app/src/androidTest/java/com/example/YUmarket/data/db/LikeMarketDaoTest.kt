package com.example.YUmarket.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.YUmarket.data.db.dao.LikeMarketDao
import com.example.YUmarket.data.entity.room.LikeMarketEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class LikeMarketDaoTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var database: YUMarketDB
    private lateinit var likeMarketDao: LikeMarketDao

    private val testList = (1..5).map {
        LikeMarketEntity(
            id = it.toLong(),
            marketName = "market $it",
            distance = it.toFloat(),
            isOpen = it % 2 == 0,
            imageUrl = "image$it"
        )
    }

    @Before
    fun setUp() {
        database =
            Room.inMemoryDatabaseBuilder(context, YUMarketDB::class.java).build()

        likeMarketDao = database.likeMarketDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllTest_returnCorrect() = runBlocking {
        var result = likeMarketDao.getAll()
        assertThat(result).isEmpty()

        for (likeMarketEntity in testList) {
            likeMarketDao.insert(likeMarketEntity.copy(id = 0))
        }

        result = likeMarketDao.getAll()
        assertThat(result).isEqualTo(testList.reversed())
    }

    @Test
    fun deleteTest_returnCorrect() = runBlocking {
        for (likeMarketEntity in testList) {
            likeMarketDao.insert(likeMarketEntity.copy(id = 0))
        }

        var result = likeMarketDao.getAll()
        assertThat(result).isEqualTo(testList.reversed())

        likeMarketDao.delete(testList[0])
        result = likeMarketDao.getAll()
        assertThat(result).isEqualTo(testList.drop(1).reversed())
    }

    @Test
    fun deleteAllTest_returnEmpty() = runBlocking {
        for (likeMarketEntity in testList) {
            likeMarketDao.insert(likeMarketEntity.copy(id = 0))
        }

        var result = likeMarketDao.getAll()
        assertThat(result).isEqualTo(testList.reversed())

        likeMarketDao.deleteAll()
        result = likeMarketDao.getAll()
        assertThat(result).isEmpty()
    }
}