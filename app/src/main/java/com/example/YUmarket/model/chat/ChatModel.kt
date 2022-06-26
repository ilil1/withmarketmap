package com.example.YUmarket.model.chat

import com.example.YUmarket.model.CellType
import com.example.YUmarket.model.Model
import com.example.YUmarket.screen.myinfo.customerservice.list.CSCategory

data class ChatModel(
    override val id:Long,
    override val type: CellType = CellType.CHATTING_CELL,
    val InfoId: Long,
    val ImageUrl: String,
    val StoreName: String,
    val RecentlyText:String,
    val Data:String
): Model(id,type)
