package com.example.YUmarket.data.repository.map

import com.example.YUmarket.model.map.MapItemModel
import com.example.YUmarket.model.map.MapMarketModel
import com.naver.maps.geometry.LatLng

class DefaultMapRepository : MapRepository {
    // 상점마다 파는 여러 아이템
    private val testStoreItems0 = arrayListOf(
        MapItemModel(
            0, 1000, 10, 100, "너트",
            "https://kr.misumi-ec.com/linked/material/mech/SAI1/PHOTO/221005546274.jpg"
        ),
        MapItemModel(
            1, 10000, 20, 1000, "볼트",
            "http://www.suwonbolt.co/images/product/B-011.png"
        ),
        MapItemModel(
            2, 100000, 30, 10000, "야스리",
            "https://shop2.daumcdn.net/thumb/R500x500.q90/?fname=http%3A%2F%2Fshop2.daumcdn.net%2Fshophow%2Fp%2FC5093003239.jpg"
        )
    )

    private val testStoreItems1 = arrayListOf(
        MapItemModel(
            3, 1000, 10, 100, "너트1111",
            "https://kr.misumi-ec.com/linked/material/mech/SAI1/PHOTO/221005546274.jpg"
        )
    )

    private val testStoreItems2 = arrayListOf(
        MapItemModel(
            6, 1000, 10, 100, "너트2222",
            "https://kr.misumi-ec.com/linked/material/mech/SAI1/PHOTO/221005546274.jpg"
        ),
        MapItemModel(
            7, 10000, 20, 1000, "볼트2222",
            "http://www.suwonbolt.co/images/product/B-011.png"
        )
    )

    private val testStores = arrayListOf(
        MapMarketModel(
            0,
            "부부식당",
            LatLng(35.84276976076099, 128.75035319046196),
            10.0f,
            testStoreItems0
        ),
        MapMarketModel(
            1,
            "오빠야찜닭",
            LatLng(35.8431743483104, 128.74862612604866),
            10.0f,
            testStoreItems1
        ),
        MapMarketModel(
            2,
            "커피플레이스",
            LatLng(35.84115830558572, 128.7540212700628),
            10.0f,
            testStoreItems2,
            "영남대점"
        ),
        MapMarketModel(
            3,
            "오빠야찜닭3",
            LatLng(35.8421843483104, 128.74862612604866),
            10.0f,
            testStoreItems1
        ),
        MapMarketModel(
            4,
            "오빠야찜닭4",
            LatLng(35.8441943483104, 128.74862612604866),
            10.0f,
            testStoreItems2
        ),
        MapMarketModel(
            5,
            "오빠야찜닭5",
            LatLng(35.8451643483104, 128.74862612604866),
            10.0f,
            testStoreItems2
        ),
    )

    /**
     * 표시할 마켓의 마커들을 가져오는 method
     * @return 지도에 표시할 마커들의 list
     */
    override fun getMarkets(): List<MapMarketModel> = testStores
}