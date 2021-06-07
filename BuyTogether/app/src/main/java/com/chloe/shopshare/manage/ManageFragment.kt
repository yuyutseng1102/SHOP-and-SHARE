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

    private val viewModel by viewModels<ManageViewModel> { getVmFactory(args.shopIdKey) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentManageBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val memberAdapter = MemberAdapter(viewModel)
        binding.recyclerMember.adapter = memberAdapter

        binding.layoutSwipeRefreshManage.setOnRefreshListener {
            viewModel.refresh()
            Log.d("Chloe", "home status = ${viewModel.status.value}")
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshManage.isRefreshing = it
            }
        })


            viewModel.member.observe(viewLifecycleOwner, Observer {
                it?.let {
                    Log.d("Life", "member is change")
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

                status = viewModel.shop.value?.status?:0
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

                status = viewModel.shop.value?.status?:0
            )
            dialog.show(childFragmentManager, "hiya")
        }

        viewModel.messageContent.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.editShopNotify()
            }
        })

        viewModel.deleteSuccess.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    viewModel.apply {
                        order.value?.let {
                            decreaseOrderSize(shop.value!!.id,it.size.minus(viewModel.deleteList.value?.size?:0))
                        }
                        onSuccessDeleteOrder()
                    }
                }

            }
        })

        viewModel.successDecreaseOrder.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    viewModel.deleteList.observe(viewLifecycleOwner, Observer {
                        it?.let {
                            viewModel.editOrderNotify(it)
                            viewModel.onFailNotifySend()
                        }
                    })
                    viewModel.onSuccessDecreaseOrder()
                }
            }
        }
            )


        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
                viewModel.onDetailNavigate()
            }
        })

        viewModel.navigateToChatRoom.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToChatRoomFragment(it.myId,it.friendId,it.chatRoomId))
                viewModel.onChatRoomNavigated()
            }
        })



        return binding.root
    }

}

interface GroupMessageSend {
    fun onMessageSend(
        message:String
    )
}