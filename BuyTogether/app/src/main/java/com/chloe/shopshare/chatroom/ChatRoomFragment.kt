package com.chloe.shopshare.chatroom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentChatRoomBinding
import com.chloe.shopshare.databinding.FragmentHostBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.HostFragmentArgs
import com.chloe.shopshare.myrequest.item.MyRequestListViewModel
import kotlinx.android.synthetic.main.item_message_left.*


class ChatRoomFragment : Fragment() {

    private val args: ChatRoomFragmentArgs by navArgs()

    private val viewModel by viewModels<ChatRoomViewModel> { getVmFactory(args.chatRoomKey) }

    private lateinit var binding: FragmentChatRoomBinding

    private val pickImageFile = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatRoomBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        val adapter = ChatRoomMessageAdapter(viewModel)

        binding.recyclerMessage.adapter = adapter

        viewModel.chatRoom.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getFriendId(it)
                viewModel.getLiveMessage(it.id)
                binding.viewModel = viewModel
            }
        })

        viewModel.sendMessageDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                    viewModel.editMessage.value = ""
                    viewModel.onSendMessageDone()
            }
        })

        viewModel.uploadImageDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.image.value?.let { image ->
                    viewModel.sendImages(image)
                }
                viewModel.onUploadImageDone()
            }
        })

        viewModel.navigateToDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToChatRoomImageDialog(it))
                viewModel.onDialogNavigated()
            }
        })

        binding.buttonAlbum.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, pickImageFile)
        }

        return binding.root
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {

        when (requestCode) {
            pickImageFile -> {
                if (resultCode == Activity.RESULT_OK && resultData != null)
                    resultData.data?.let { uri -> viewModel.pickImages(uri)}
            }
        }
    }

}