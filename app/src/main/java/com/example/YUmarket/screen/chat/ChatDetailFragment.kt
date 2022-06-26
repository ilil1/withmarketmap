package com.example.YUmarket.screen.chat

import androidx.navigation.Navigation
import com.example.YUmarket.R
import com.example.YUmarket.databinding.FragmentChatBinding
import com.example.YUmarket.databinding.FragmentChatDetailBinding
import com.example.YUmarket.model.chat.ChatData
import com.example.YUmarket.screen.base.BaseFragment

class ChatDetailFragment : BaseFragment<FragmentChatDetailBinding>() {
    override fun getViewBinding(): FragmentChatDetailBinding =
        FragmentChatDetailBinding.inflate(layoutInflater)

    override fun observeData () {}

    override fun initViews() {
        super.initViews()


        val chatData = arguments?.getParcelable<ChatData>("data")
        binding.storeName.text = chatData?.storeName.toString()

        binding.back.setOnClickListener {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_chatDetailFragment_to_chatFragment)
            }
        }

    }
}