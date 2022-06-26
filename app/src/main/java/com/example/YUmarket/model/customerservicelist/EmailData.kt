package com.example.YUmarket.model.customerservicelist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmailData(
    val emailAddress : String,
    val title: String,
    val content: String
):Parcelable
