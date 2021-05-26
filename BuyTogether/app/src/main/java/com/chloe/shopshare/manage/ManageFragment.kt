package com.chloe.shopshare.manage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.manage.groupmessage.GroupMessageDialog
import com.chloe.shopshare.databinding.FragmentManageBinding
import com.chloe.shopshare.ext.getVmFactory


class ManageFragment : Fragment() {

    private val args: ManageFragmentArgs by navArgs()

    private val viewModel by viewModels<ManageViewModel> { getVmFactory(args.shopKey) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentManageBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val memberAdapter = MemberAdapter(viewModel)
        binding.recyclerMember.adapter = memberAdapter

//        viewModel.getOrderOfShop("2TgmGprHsundCcuvHw8J")

        viewModel.shop.value?.let {
            viewModel.getOrderOfShop(it.id)
            Log.d("Chloe","order is ${viewModel.order.value}")
        }



            viewModel.member.observe(viewLifecycleOwner, Observer {
                it?.let {
                    Log.d("Life", "member is change")
                    memberAdapter.notifyDataSetChanged()
                }
            })



    viewModel.order.observe(viewLifecycleOwner, Observer {
        it?.let {
            Log.d("Life", "order is change")
            memberAdapter.notifyDataSetChanged()
        }
    })


        binding.deleteButton.setOnClickListener {
            Log.d("Chloe","status now is ${viewModel.shop.value?.status}")
            viewModel.deleteMember()
        }

        binding.buttonGather.setOnClickListener{
            viewModel.readyCollect()
            memberAdapter.notifyDataSetChanged()
            val dialog = GroupMessageDialog(
                groupMessageSend=object :
                    GroupMessageSend {
                    override fun onMessageSend(message:String) {
                        viewModel.messageContent.value = message
                        Log.d("Chloe","message dialog is Success!viewModel.messageContent.value = ${viewModel.messageContent.value}")
                    }},

                status = viewModel.shopStatus.value?:0
            )
            dialog.show(childFragmentManager, "hiya")
        }

        binding.updateProgressButton.setOnClickListener {
            viewModel.updateStatus()
            val dialog = GroupMessageDialog(
                groupMessageSend=object :
                    GroupMessageSend {
                    override fun onMessageSend(message:String) {
                        viewModel.messageContent.value = message
                        Log.d("Chloe","message dialog is Success!viewModel.messageContent.value = ${viewModel.messageContent.value}")
                    }},

                status = viewModel.shopStatus.value?:0
            )
            dialog.show(childFragmentManager, "hiya")
        }






        return binding.root
    }

}

interface GroupMessageSend {
    fun onMessageSend(
        message:String
    )
}