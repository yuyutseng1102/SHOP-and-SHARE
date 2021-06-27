package com.chloe.shopshare.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentManageBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.manage.groupmessage.GroupMessageDialog


class ManageFragment : Fragment() {

    private val args: ManageFragmentArgs by navArgs()

    private val viewModel by viewModels<ManageViewModel> { getVmFactory(args.shopKey) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentManageBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val memberAdapter = MemberAdapter(viewModel)
        binding.recyclerMember.adapter = memberAdapter

        binding.layoutSwipeRefreshManage.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshManage.isRefreshing = it
            }
        })


        viewModel.member.observe(viewLifecycleOwner, Observer {
            it?.let {
                memberAdapter.notifyDataSetChanged()
            }
        })

        binding.deleteButton.setOnClickListener {
            viewModel.deleteMember()
        }

        binding.buttonGather.setOnClickListener {
            viewModel.readyCollect()
            memberAdapter.notifyDataSetChanged()
            val dialog = GroupMessageDialog(
                groupMessageSend = object :
                    GroupMessageSend {
                    override fun onMessageSend(message: String) {
                        viewModel.messageContent.value = message
                    }
                },
                status = viewModel.shop.value?.status ?: 0
            )
            dialog.show(childFragmentManager, "groupMessageSend")
        }

        binding.updateProgressButton.setOnClickListener {
            viewModel.updateStatus()
            val dialog = GroupMessageDialog(
                groupMessageSend = object :
                    GroupMessageSend {
                    override fun onMessageSend(message: String) {
                        viewModel.messageContent.value = message
                    }
                },
                status = viewModel.shop.value?.status ?: 0
            )
            dialog.show(childFragmentManager, "groupMessageSend")
        }

        viewModel.messageContent.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.editShopNotify()
            }
        })

        viewModel.apply {
            deleteSuccess.observe(viewLifecycleOwner, Observer {
                it?.let {
                    shop.value?.let { shop ->
                        order.value?.let { order ->
                            decreaseOrderSize(
                                shop.id,
                                order.size.minus(deleteList.value?.size ?: 0)
                            )
                        }
                    }
                    onSuccessDeleteOrder()
                }
            })
        }

        viewModel.successDecreaseOrder.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.deleteList.observe(viewLifecycleOwner, Observer { list ->
                    list?.let {
                        viewModel.editOrderNotify(list)
                        viewModel.onFailNotifySend()
                    }
                })
                viewModel.onSuccessDecreaseOrder()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
                viewModel.onDetailNavigate()
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

interface GroupMessageSend {
    fun onMessageSend(
        message: String
    )
}