package com.chloe.shopshare.collection

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentCollectionBinding
import androidx.lifecycle.Observer
import com.chloe.shopshare.ext.getVmFactory


class CollectionFragment : Fragment() {

    private val viewModel by viewModels<CollectionViewModel> {getVmFactory()}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentCollectionBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.addMockData()

        val adapter = CollectionAdapter(viewModel)
        binding.recyclerCollection.adapter = adapter

        viewModel.navigateToManage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("navigate", "navigateToManage_01 = ${viewModel.navigateToManage}")
                findNavController().navigate(NavigationDirections.navigateToCollectionManageFragment(it))
                viewModel.onManageNavigated()
                Log.d("navigate", "navigateToDetail_02 = ${viewModel.navigateToManage}")
            }
        })


        return binding.root
    }



}