package com.chloe.buytogether.collection

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.chloe.buytogether.MyApplication
import com.chloe.buytogether.R
import com.chloe.buytogether.databinding.FragmentCollectionManageBinding
import com.chloe.buytogether.databinding.FragmentHomeCollectBinding
import com.chloe.buytogether.ext.getVmFactory
import com.chloe.buytogether.home.item.HomeCollectAdapter
import com.chloe.buytogether.home.item.HomeCollectViewModel
import com.chloe.buytogether.home.item.HomeSpinnerAdapter


class CollectionManageFragment : Fragment() {

    private val viewModel by viewModels<CollectionManageViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentCollectionManageBinding.inflate(inflater,container,false)
//        val adapter = HomeCollectAdapter(HomeCollectAdapter.OnClickListener {
//            Log.d("Chloe", "click!")
//        })
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
//        binding.recyclerCollection.adapter = adapter

//        viewModel.addMockData(collectType)

        return binding.root
    }

}