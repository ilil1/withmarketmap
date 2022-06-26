package com.example.YUmarket.data.repository.shop

import com.example.YUmarket.data.entity.location.LocationLatLngEntity
import com.example.YUmarket.data.network.MapApiService
import com.example.YUmarket.data.network.ShopController
import com.example.YUmarket.data.repository.map.MapApiRepository
import com.example.YUmarket.data.response.address.AddressInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultShopApiRepository (
        private val shopController: ShopController,
        private val ioDispatcher: CoroutineDispatcher
    ) : ShopApiRepository {
    override suspend fun getShopList() =
        withContext(ioDispatcher) {
            val response = shopController.getList()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
}
