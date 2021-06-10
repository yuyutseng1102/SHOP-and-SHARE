package com.chloe.shopshare.chatroom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentChatRoomBinding
import com.chloe.shopshare.databinding.FragmentHostBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.HostFragmentArgs
import com.chloe.shopshare.myrequest.item.MyRequestListViewModel


class ChatRoomFragment : Fragment() {
    private val args: ChatRoomFragmentArgs by navArgs()
    private val viewModel by viewModels<ChatRoomViewModel> { getVmFactory(args.myId,args.friendId,args.chatRoomId) }
    private lateinit var binding: FragmentChatRoomBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = ChatRoomMessageAdapter()
        binding.recyclerMessage.adapter = adapter

        viewModel.getChatRoomDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.friendId.value?.let {
                    viewModel.getFriendProfile(it)
                }
                viewModel.onGetChatRoomDone()
            }
        })

        viewModel.getProfileDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.chatRoom.value?.let {
                    viewModel.getLiveMessage(it.id)
                    binding.viewModel = viewModel
                }
            }
        })

        viewModel.sendMessageDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    viewModel.editMessage.value = ""
                }
            }
        })


        return binding.root
    }


}