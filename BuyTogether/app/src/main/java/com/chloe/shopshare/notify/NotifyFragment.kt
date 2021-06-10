package com.chloe.shopshare.notify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.chloe.shopshare.databinding.FragmentHomePageBinding
import com.chloe.shopshare.databinding.FragmentNotifyBinding
import com.chloe.shopshare.ext.getVmFactory


class NotificationFragment : Fragment() {

    private val viewModel by viewModels<NotifyViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentNotifyBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = NotifyAdapter(viewModel)
        binding.recyclerNotify.adapter = adapter
//        viewModel.addMockData()

//        viewModel.notify.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.notifyDataSetChanged()
//            }
//        })

        return binding.root
    }

}