package com.example.YUmarket.data.repository.myinfo

import com.example.YUmarket.model.customerservicelist.CSModel
import com.example.YUmarket.screen.myinfo.customerservice.list.CSCategory


/**
 * @주 허희태
 * @부 김건우 정남진 김도엽 배은호
 * @since
 * @throws
 * @description
 */


interface CSRepository {


     fun findCsByCategory(csCategory: CSCategory) : List<CSModel>
}