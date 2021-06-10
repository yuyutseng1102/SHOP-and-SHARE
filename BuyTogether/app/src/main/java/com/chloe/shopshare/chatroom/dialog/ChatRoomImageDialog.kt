package com.chloe.shopshare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.chloe.shopshare.databinding.DialogChatRoomImageBinding
import com.chloe.shopshare.databinding.DialogHostOptionBinding

class ChatRoomImageDialog(private val image: String) : AppCompatDialogFragment()  {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogChatRoomImageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.imageItem = image
        binding.buttonClose.setOnClickListener {
            this.dismiss()
        }
        return binding.root
    }

}