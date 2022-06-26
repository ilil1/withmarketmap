package com.example.YUmarket.model.customerservicelist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageData(
    val csTitle : String,
    val csContentTitle : String,
    val csContentBody :String,
    val csAuthor: String,
) : Parcelable
