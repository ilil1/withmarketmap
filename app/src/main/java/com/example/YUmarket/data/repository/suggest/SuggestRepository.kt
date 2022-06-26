package com.example.YUmarket.data.repository.suggest

import com.example.YUmarket.model.homelist.SuggestItemModel
import com.example.YUmarket.model.homelist.category.SuggestCategory

interface SuggestRepository
{


    fun seasonMarket() : List<SuggestItemModel>
//
//    fun suggestBirth() : List<SuggestItemModel>
//
//
    fun suggestAnniversary() : List<SuggestItemModel>

    fun fixMarket() : List<SuggestItemModel>


    fun suggestHobby() : List<SuggestItemModel>


}