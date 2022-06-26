package com.example.YUmarket.screen.chat

import com.example.YUmarket.databinding.ActivityChatBinding
import com.example.YUmarket.screen.base.BaseActivity

class ChatActivity : BaseActivity<ActivityChatBinding>() {
    override fun getViewBinding(): ActivityChatBinding =
        ActivityChatBinding.inflate(layoutInflater)

    override fun observeData() {

    }
}