package com.example.YUmarket.util.provider


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

class DefaultResourcesProvider(
    private val context: Context
) : ResourcesProvider {

    override fun getString(resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg formArgs: Any): String =
        context.getString(resId, *formArgs)

    override fun getColor(resId: Int): Int = ContextCompat.getColor(context, resId)

    override fun getColorStateList(resId: Int): ColorStateList =
        AppCompatResources.getColorStateList(context, resId)

    override fun getDrawable(resId: Int): Drawable? = AppCompatResources.getDrawable(context, resId)

}