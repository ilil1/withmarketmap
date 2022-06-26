package com.example.YUmarket.screen.myinfo.customerservice.list

import androidx.annotation.StringRes
import com.example.YUmarket.R

/**
 * @author HeeTae Heo(main),
 * Geonwoo Kim, Doyeop Kim, Namjin Jeong, Eunho Bae (sub)
 * @since
 * @throws
 * @description
 */

enum class CSCategory(
    @StringRes val categoryNameId: Int,
    @StringRes val categoryTypeId: Int
) {
    LOGIN(R.string.cs_login, R.string.cs_login_type),
    USE(R.string.cs_use, R.string.cs_use_type),
    ORDER(R.string.cs_order, R.string.cs_order_type),
    REVIEW(R.string.cs_review, R.string.cs_review_type),
    ETC(R.string.cs_etc, R.string.cs_etc_type)


}