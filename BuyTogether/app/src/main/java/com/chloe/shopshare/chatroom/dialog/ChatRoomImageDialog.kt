package com.chloe.shopshare.chatroom.dialog

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.DialogChatRoomImageBinding

class ChatRoomImageDialog : AppCompatDialogFragment()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MessageDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args : ChatRoomImageDialogArgs by navArgs()
        val binding = DialogChatRoomImageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.imageItem = args.imageKey
        binding.buttonClose.setOnClickListener {
            this.dismiss()
        }
        return binding.root
    }

}