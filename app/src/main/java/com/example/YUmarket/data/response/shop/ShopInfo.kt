package com.example.YUmarket.data.response.shop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopInfo(
    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("data")
    val shopList: List<ShopData>
)

data class ShopData(
    @Expose
    @SerializedName("shop_id")
    val shop_id: String,
    @Expose
    @SerializedName("shop_name")
    val shop_name: String,
    @Expose
    @SerializedName("is_open")
    val is_open: Boolean,
    @Expose
    @SerializedName("lot_number_address")
    val lot_number_address: String,
    @Expose
    @SerializedName("road_name_address")
    val road_name_address: String,
    @Expose
    @SerializedName("latitude")
    val latitude: Double,
    @Expose
    @SerializedName("longitude")
    val longitude: Double,
    @Expose
    @SerializedName("average_score")
    val average_score: Int,
    @Expose
    @SerializedName("review_number")
    val review_number: Int,
    @Expose
    @SerializedName("main_image")
    val main_image: String,
    @Expose
    @SerializedName("description")
    val description: String,
    @Expose
    @SerializedName("category")
    val category: String,
    @Expose
    @SerializedName("detail_category")
    val detail_category: String,
    @Expose
    @SerializedName("is_branch")
    val is_branch : Boolean,
    @Expose
    @SerializedName("branch_name")
    val branch_name : String

)