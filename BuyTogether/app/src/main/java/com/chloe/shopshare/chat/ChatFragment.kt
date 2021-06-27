package com.chloe.shopshare.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.data.ChatRoom
import com.chloe.shopshare.databinding.FragmentChatBinding
import com.chloe.shopshare.ext.getVmFactory


class ChatFragment : Fragment() {

    private val viewModel by viewModels<ChatViewModel> { getVmFactory() }

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        val adapter = ChatAdapter(ChatAdapter.OnClickListener {
            viewModel.navigateToChatRoom(it.chatRoom?: ChatRoom())
        }, viewModel)

        binding.recyclerChat.adapter = adapter

        viewModel.chatRooms.observe(viewLifecycleOwner, Observer {
            when(it.isNullOrEmpty()){
                true -> viewModel.isChatListEmpty()
                false -> {
                    binding.viewModel = viewModel
                    viewModel.getProfile(it)
                    viewModel.isChatListNotEmpty()
                }
            }
        })

        viewModel.navigateToChatRoom.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToChatRoomFragment(it))
                viewModel.onChatRoomNavigated()
            }
        })

        return binding.root
    }
}