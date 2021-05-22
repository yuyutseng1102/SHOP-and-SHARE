package com.chloe.buytogether.collection.manage

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
import com.chloe.buytogether.NavigationDirections
import com.chloe.buytogether.collection.groupmessage.GroupMessageDialog
import com.chloe.buytogether.databinding.FragmentCollectionManageBinding
import com.chloe.buytogether.ext.getVmFactory


class CollectionManageFragment : Fragment() {

    private val args: CollectionManageFragmentArgs by navArgs()

    private val viewModel by viewModels<CollectionManageViewModel> { getVmFactory(args.collectionKey) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentCollectionManageBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val memberAdapter = MemberAdapter(viewModel)
        binding.recyclerMember.adapter = memberAdapter




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
            Log.d("Chloe","status now is ${viewModel.collection.value?.status}")
            viewModel.deleteMember()
        }

        binding.buttonGather.setOnClickListener{
            viewModel.readyCollect()
            memberAdapter.notifyDataSetChanged()
            val dialog = GroupMessageDialog(
                groupMessageSend=object : GroupMessageSend {
                    override fun onMessageSend(message:String) {
                        viewModel.messageContent.value = message
                        Log.d("Chloe","message dialog is Success!viewModel.messageContent.value = ${viewModel.messageContent.value}")
                    }},

                status = viewModel.collectionStatus.value?:0
            )
            dialog.show(childFragmentManager, "hiya")
        }

        binding.updateProgressButton.setOnClickListener {
            viewModel.updateStatus()
            val dialog = GroupMessageDialog(
                groupMessageSend=object : GroupMessageSend {
                    override fun onMessageSend(message:String) {
                        viewModel.messageContent.value = message
                        Log.d("Chloe","message dialog is Success!viewModel.messageContent.value = ${viewModel.messageContent.value}")
                    }},

                status = viewModel.collectionStatus.value?:0
            )
            dialog.show(childFragmentManager, "hiya")
        }

        binding.collectionConditionTitle.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToDetailFragment(viewModel.collection.value!!))
        }






        return binding.root
    }

}

interface GroupMessageSend {
    fun onMessageSend(
        message:String
    )
}