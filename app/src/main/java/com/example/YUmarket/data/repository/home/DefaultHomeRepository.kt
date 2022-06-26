//package com.example.YUmarket.data.repository.home
//
//import com.example.YUmarket.data.entity.location.LocationLatLngEntity
//import com.example.YUmarket.model.homelist.HomeItemModel
//import com.example.YUmarket.model.homelist.TownMarketModel
//import com.example.YUmarket.model.homelist.category.HomeListCategory
//import com.example.YUmarket.model.homelist.category.HomeListDetailCategory
///**
// * HomeListFragment의 Item에 대한 repository
// * @author Doyeop Kim (main),
// * Geonwoo Kim, Heetae Heo, Namjin Jeong, Eunho Bae (sub)
// * @since 2022/01/18
// */class DefaultHomeRepository : HomeRepository {
//    override fun getAllMarketList(): List<TownMarketModel> {
//        // Mocking Data
//        // 22.01.18 거리 추가 by 정남진
//        val mockList = listOf(
//            TownMarketModel(
//                0,
//                "쥬얼리 샵",
//                true,
//                LocationLatLngEntity(128.0, 36.0),
//                "https://picsum.photos/200",
//                0.11f
//            ),
//            TownMarketModel(
//                1,
//                "영남대 옷가게",
//                true,
//                LocationLatLngEntity(128.0, 36.0),
//                "https://picsum.photos/200",
//                0.22f
//            ),
//            TownMarketModel(
//                2,
//                "피자스쿨 영남대점",
//                true,
//                LocationLatLngEntity(128.0, 36.0),
//                "https://picsum.photos/200",
//                0.33f
//            ),
//            TownMarketModel(
//                3,
//                "빅마트",
//                false,
//                LocationLatLngEntity(128.0, 36.0),
//                "https://picsum.photos/200",
//                0.44f
//            ),
//            TownMarketModel(
//                4,
//                "롯데리아 영남대 DT",
//                false,
//                LocationLatLngEntity(128.0, 36.0),
//                "https://picsum.photos/200",
//                0.10f
//            )
//        )
//
//        return mockList
//    }
//
//    override fun findItemsByCategory(homeListCategory: HomeListCategory): List<HomeItemModel> {
//        return when (homeListCategory) {
//            HomeListCategory.FOOD -> listOf(
//                HomeItemModel(
//                    0,
//                    HomeListCategory.FOOD,
//                    HomeListDetailCategory.FAST_FOOD,
//                    "https://picsum.photos/200",
//                    TownMarketModel(
//                        4,
//                        "롯데리아 영남대 DT",
//                        false,
//                        LocationLatLngEntity(128.0, 36.0),
//                        "https://picsum.photos/200",
//                        0.11f
//                    ),
//                    "폴더버거",
//                    5300,
//                    5100,
//                    30,
//                    5,
//                    5
//                ),
//                HomeItemModel(
//                    1,
//                    HomeListCategory.FOOD,
//                    HomeListDetailCategory.FAST_FOOD,
//                    "https://picsum.photos/200",
//                    TownMarketModel(
//                        4,
//                        "롯데리아 영남대 DT",
//                        false,
//                        LocationLatLngEntity(128.0, 36.0),
//                        "https://picsum.photos/200",
//                        0.11f
//                    ),
//                    "핫크리스피 버거",
//                    4800,
//                    4500,
//                    30,
//                    5,
//                    5
//                ),
//                HomeItemModel(
//                    2,
//                    HomeListCategory.FOOD,
//                    HomeListDetailCategory.FAST_FOOD,
//                    "https://picsum.photos/200",
//                    TownMarketModel(
//                        4,
//                        "롯데리아 영남대 DT",
//                        false,
//                        LocationLatLngEntity(128.0, 36.0),
//                        "https://picsum.photos/200",
//                        0.11f
//                    ),
//                    "불고기버거",
//                    3800,
//                    3800,
//                    30,
//                    5,
//                    5
//                )
//            )
//
//            HomeListCategory.MART -> listOf(
//                HomeItemModel(
//                    0,
//                    HomeListCategory.MART,
//                    HomeListDetailCategory.SNACK_AND_BREAD,
//                    "https://picsum.photos/200",
//                    TownMarketModel(
//                        3,
//                        "빅마트",
//                        true,
//                        LocationLatLngEntity(128.0, 36.0),
//                        "https://picsum.photos/200",
//                        0.11f
//                    ),
//                    "초코송이",
//                    1500,
//                    800,
//                    5,
//                    5,
//                    5
//                ),
//                HomeItemModel(
//                    1,
//                    HomeListCategory.MART,
//                    HomeListDetailCategory.WASHING_PRODUCTS,
//                    "https://picsum.photos/200",
//                    TownMarketModel(
//                        3,
//                        "빅마트",
//                        true,
//                        LocationLatLngEntity(128.0, 36.0),
//                        "https://picsum.photos/200",
//                        0.11f
//                    ),
//                    "샤프란",
//                    4000,
//                    3500,
//                    3,
//                    3,
//                    2
//                ),
//                HomeItemModel(
//                    2,
//                    HomeListCategory.MART,
//                    HomeListDetailCategory.MART_EXTRA,
//                    "https://picsum.photos/200",
//                    TownMarketModel(
//                        3,
//                        "빅마트",
//                        true,
//                        LocationLatLngEntity(128.0, 36.0),
//                        "https://picsum.photos/200",
//                        0.11f
//                    ),
//                    "아이스티 분말",
//                    5000,
//                    4300,
//                    7,
//                    5,
//                    2
//                ),
//                HomeItemModel(
//                    3,
//                    HomeListCategory.MART,
//                    HomeListDetailCategory.SNACK_AND_BREAD,
//                    "https://picsum.photos/200",
//                    TownMarketModel(
//                        3,
//                        "빅마트",
//                        true,
//                        LocationLatLngEntity(128.0, 36.0),
//                        "https://picsum.photos/200",
//                        0.11f
//                    ),
//                    "포테이토칩",
//                    1300,
//                    1200,
//                    5,
//                    5,
//                    5
//                )
//            )
//
//            HomeListCategory.FASHION -> listOf(
//
//            )
//
//            HomeListCategory.ACCESSORY -> listOf(
//
//            )
//
//            HomeListCategory.SERVICE -> listOf(
//
//            )
//
//            HomeListCategory.ETC -> listOf()
//
//            else -> listOf()
//        }
//    }
//
//    // TODO 22.01.25 임시로 만든 Method 나중에 제대로 구현
//    override fun getAllNewSaleItems(): List<HomeItemModel> {
//        val result = mutableListOf<HomeItemModel>()
//        val categories = HomeListCategory.values().drop(1)
//
//        categories.forEach {
//            result.addAll(findItemsByCategory(it))
//        }
//
//        return result
//    }
//}