package com.example.YUmarket.data.repository.myinfo

import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.customerservicelist.CSModel
import com.example.YUmarket.screen.myinfo.customerservice.list.CSCategory
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @주 허희태
 * @부 김건우 정남진 김도엽
 * @since
 * @throws
 * @description
 */


class DefaultCSRepository(
    private val ioDispatcher: CoroutineDispatcher //나중에 api용
) : CSRepository {


    override fun findCsByCategory(csCategory: CSCategory): List<CSModel> {
        return when (csCategory) {
            CSCategory.LOGIN -> listOf(
                CSModel(
                    0,
                    CellType.CUSTOMER_SERVICE_CELL,
                    0,
                    csTitle = "   로그인 오류시 이용방법                                                ",
                    csCategory = CSCategory.LOGIN,
                    csAuthor = "위드마켓",
                    csContentTitle = "이 글은 로그인 오류시 이용방법 입니다",
                    csContentBody ="로그인 오류시 앱을 재부팅하여 사용하고" +
                            "그래도 로그인이 되지않는다면 고객센터로 연락바랍니다."
                )


            )

            CSCategory.USE -> listOf(
                CSModel(
                    1,
                    CellType.CUSTOMER_SERVICE_CELL,
                    0,
                    csTitle = "   애플리케이션 이용방법                                        ",
                    csCategory = CSCategory.USE,
                    csAuthor = "위드마켓",
                    csContentTitle = "이 글은 애플리케이션 이용방법 입니다",
                    csContentBody = "위드마켓은 사용자 주변의 있는 다양한 스토어의 정보들을 제공하고" +
                            "사용자에게 맞춤형 서비스를 제공해드립니다."
                )

            )

            CSCategory.ORDER -> listOf(
                CSModel(
                    2,
                    CellType.CUSTOMER_SERVICE_CELL,
                    0,
                    csTitle = "   주문오류시 이용방법                                        ",
                    csCategory = CSCategory.ORDER,
                    csAuthor = "위드마켓",
                    csContentTitle = "이 글은 주문오류시 이용방법 입니다",
                    csContentBody = "주문 오류로 인해 결제는 되었지만 알람 PUSH가 오지않는 경우는"
                + "고객센터에 연락해 주시고" +"<br>" + "결제 동작이 정상적이지 않았다면 재주문 부탁드립니다."

                )
            )

            CSCategory.REVIEW -> listOf(
                CSModel(
                    3,
                    CellType.CUSTOMER_SERVICE_CELL,
                    0,
                    csTitle = "   리뷰오류시 이용방법                                        ",
                    csCategory = CSCategory.REVIEW,
                    csAuthor = "위드마켓",
                    csContentTitle = "이 글은 리뷰오류시 이용방법 입니다",
                    csContentBody = "리뷰의 수정버튼을 통해 다시 재리뷰를 부탁드립니다."
                )
            )


            CSCategory.ETC -> listOf(
                CSModel(
                    4,
                    CellType.CUSTOMER_SERVICE_CELL,
                    0,
                    csTitle = "   기타 오류시 이용방법                                        ",
                    csCategory = CSCategory.ETC,
                    csAuthor = "위드마켓",
                    csContentTitle = "이 글은 기타오류시 이용방법 입니다",
                    csContentBody = "이러한 오류는 없으셨나요?"
                                    + "이러한 오류는 없으셨나요?"

                )
            )
        }


//        val csList = listOf(
//            CSModel(
//                0,
//                CellType.CUSTOMER_SERVICE_CELL,
//                0,
//                csTitle = "   고객센터 이용방법                                        ",
//                csCategory = CSCategory.USE,
//                csAuthor = "관리자",
//                csContent = "이 글은 고객센터 이용방법 입니다"
//            ),
//
//            CSModel(
//                1,
//                CellType.CUSTOMER_SERVICE_CELL,
//                1,
//                csTitle = "   주문오류시 이용방법                                        ",
//                csCategory = CSCategory.ORDER,
//                csAuthor = "관리자",
//                csContent = "이 글은 주문오류시 이용방법 입니다"
//            ),
//
//            CSModel(
//                2,
//                CellType.CUSTOMER_SERVICE_CELL,
//                2,
//                csTitle = "   리뷰 오류시 이용방법                                           ",
//                csCategory = CSCategory.REVIEW,
//                csAuthor = "관리자",
//                csContent = "이 글은 리뷰 오류시 입니다"
//            ),
//
//
//            CSModel(
//                3,
//                CellType.CUSTOMER_SERVICE_CELL,
//                3,
//                csTitle = "   로그인 오류시 이용방법                                                ",
//                csCategory = CSCategory.LOGIN,
//                csAuthor = "관리자",
//                csContent = "이 글은 로그인 오류시 이용방법 입니다"
//            ),
//
//            CSModel(
//                4,
//                CellType.CUSTOMER_SERVICE_CELL,
//                4,
//                csTitle = "   이용 오류시 이용방법                                           ",
//                csCategory = CSCategory.USE,
//                csAuthor = "관리자",
//                csContent = "이 글은 이용 오류시 이용방법 입니다"
//            ),
//
//            CSModel(
//                5,
//                CellType.CUSTOMER_SERVICE_CELL,
//                5,
//                csTitle = "   기타 오류시 이용방법                                           ",
//                csCategory = CSCategory.ETC,
//                csAuthor = "관리자",
//                csContent = "이 글은 기타 오류시 입니다"
//            ),
//        )
        return emptyList()
    }
}