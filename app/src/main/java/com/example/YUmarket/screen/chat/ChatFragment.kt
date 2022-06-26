package com.example.YUmarket.screen.chat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.YUmarket.R
import com.example.YUmarket.databinding.FragmentChatBinding
import com.example.YUmarket.model.chat.ChatData
import com.example.YUmarket.model.chat.ChatModel
import com.example.YUmarket.screen.base.BaseFragment
import com.example.YUmarket.util.provider.ResourcesProvider
import com.example.YUmarket.widget.adapter.ModelRecyclerAdapter
import com.example.YUmarket.widget.adapter.listener.chat.ChatModelListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private val viewModel by viewModel<ChatViewModel>()
    private val resourcesProvider by inject<ResourcesProvider>()


    override fun getViewBinding(): FragmentChatBinding =
        FragmentChatBinding.inflate(layoutInflater)

    override fun observeData() = with(viewModel) {
        chatListData.observe(viewLifecycleOwner) {
            chatadapter.submitList(it)
        }
    }


    private val chatadapter by lazy {
        ModelRecyclerAdapter<ChatModel, ChatViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            object : ChatModelListener {
                override fun onClickItem(model: ChatModel) {
                    val data = ChatData(model.StoreName)
                    val bundle = Bundle()
                    bundle.putParcelable("data",data)
                    view?.let { it1 ->
                        Navigation.findNavController(it1)
                            .navigate(R.id.action_chatFragment_to_chatDetailFragment,bundle)
                    }
                }
            }
        )
    }


    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/user")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun initState() = with(viewModel) {
        fetchData()
        super.initState()
    }

    override fun initViews() {
        super.initViews()



        binding.chatRecy.adapter = chatadapter
        binding.chatRecy.layoutManager =  GridLayoutManager(
            requireContext(),
            1,
            GridLayoutManager.VERTICAL,
            false
        )


        binding.back.setOnClickListener {
            activity?.finish()
        }
    }

}

