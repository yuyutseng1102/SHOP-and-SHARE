package com.chloe.shopshare.manage.groupmessage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chloe.shopshare.manage.GroupMessageSend
import com.chloe.shopshare.databinding.DialogGroupMessageBinding
import com.chloe.shopshare.ext.getVmFactory


class GroupMessageDialog(private val groupMessageSend : GroupMessageSend?, val status :Int) : AppCompatDialogFragment() {

    private val viewModel by viewModels<GroupMessageViewModel> { getVmFactory() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DialogGroupMessageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.messageContent.value = ""
        Log.d("Chloe","the status pass to dialog is $status")
        viewModel.shopStatus.value = status



        binding.buttonReady.setOnClickListener{

            viewModel.messageContent.observe(viewLifecycleOwner, Observer {
                Log.d("Chloe", "the message is ${viewModel.messageContent.value}")
                viewModel.checkContent()
            })

            if(viewModel.isClickable.value == true){
                groupMessageSend?.onMessageSend(
                    viewModel.messageContent.value?:""
                )
                Toast.makeText(context,"成功送出",Toast.LENGTH_SHORT).show()
                this.dismiss()
            }else{
                Toast.makeText(context,"內容不完整",Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonReject.setOnClickListener{
                groupMessageSend?.onMessageSend(
                    ""
                )
                this.dismiss()
        }




        return binding.root
    }

}