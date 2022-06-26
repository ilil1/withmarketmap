package com.example.YUmarket.data.repository.store

import com.example.YUmarket.model.store.StoreItemModel

class DefaultStoreRepository : StoreRepository {


    override fun findStore(): List<StoreItemModel> {
        val mockList = listOf(
            StoreItemModel(
                0,
                "싱싱상회",
                40,
                "모듬 광어 우럭 삼치 참치 ",
                "4.7",
                "https://velog.velcdn.com/images/heetaeheo/post/aa67a287-54e0-40a0-bfe4-82ebf9095871/image.png"
        ),
            StoreItemModel(
                0,
                "이보크 가구",
                22,
                "침대 의자 식탁 소파 ",
                "3.5",
                "https://velog.velcdn.com/images/heetaeheo/post/33c8b2da-8701-4bc1-80f1-8a0223ccab26/image.png",
            ),
            StoreItemModel(
                0,
                "일로 홈케어",
                30,
                "진드기 침대 소독 및 방역 서비스 제공",
                "1.3",
                "https://velog.velcdn.com/images/heetaeheo/post/99fbe3d6-2a2f-41e6-87b5-d2084c5f7943/image.png",
            ),
            StoreItemModel(
                0,
                "포옹꽃",
                70,
                "장미 국화 수국 및 따로 예약 주문 가능",
                "1",
                "https://velog.velcdn.com/images/heetaeheo/post/15d68f46-d77d-4855-8f1c-b79cb2f344a5/image.png",
            )
        )
            return mockList

    }
}