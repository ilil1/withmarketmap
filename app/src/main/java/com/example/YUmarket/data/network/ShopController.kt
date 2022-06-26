package com.example.YUmarket.data.network

import com.example.YUmarket.data.response.shop.ShopInfo
import com.example.YUmarket.data.response.shop.ShopModelResponse
import com.example.YUmarket.data.url.Url
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ShopController {
    @GET(Url.GET_SHOP_LIST)
    suspend fun getList(): Response<ShopInfo>
}