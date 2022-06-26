package com.example.YUmarket.data.repository.shop

import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.data.response.address.AddressInfo
import com.example.YUmarket.data.response.shop.ShopInfo

interface ShopApiRepository {
    suspend fun getShopList(
    ): ShopInfo?
}