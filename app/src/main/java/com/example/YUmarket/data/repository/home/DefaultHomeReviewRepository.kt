package com.example.YUmarket.data.repository.home

import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.homelist.HomeReviewModel
import kotlinx.coroutines.CoroutineDispatcher

class DefaultHomeReviewRepository(
    private val ioDispatcher: CoroutineDispatcher //나중에 api용
) : HomeReviewRepository {
    override fun findChatList(): List<HomeReviewModel> {
        val mockList = listOf(
            HomeReviewModel(
                0,
                CellType.HOME_REVIEW_ITEM_CELL,
                "맛있습니다",
                "1",
                "싱싱상회",
                5
            ),
            HomeReviewModel(
                0,
                CellType.HOME_REVIEW_ITEM_CELL,
                "맛없습니다",
                "2",
                "이보크 가구",
                1
            ),
            HomeReviewModel(
                0,
                CellType.HOME_REVIEW_ITEM_CELL,
                "그저 그렇습니다",
                "3",
                "일로 홈케어",
                3
            )
        )
        return mockList
    }
}