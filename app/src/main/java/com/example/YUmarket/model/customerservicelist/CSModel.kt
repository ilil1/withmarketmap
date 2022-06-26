package com.example.YUmarket.model.customerservicelist

import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.Model
import com.example.YUmarket.screen.myinfo.customerservice.list.CSCategory

/**
 * @author HeeTae Heo(main),
 * Geonwoo Kim, Doyeop Kim, Namjin Jeong, Eunho Bae (sub)
 * @since
 * @throws
 * @description
 */


data class CSModel(
    override val id:Long,
    override val type: CellType = CellType.CUSTOMER_SERVICE_CELL,
    val csInfoId: Long,
    val csCategory: CSCategory,
    val csTitle: String,
    val csAuthor:String,
    val csContentTitle:String,
    val csContentBody:String
): Model(id,type)


