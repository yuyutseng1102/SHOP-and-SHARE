package com.chloe.buytogether.collection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.chloe.buytogether.R
import com.chloe.buytogether.databinding.FragmentCollectionBinding
import com.chloe.buytogether.databinding.FragmentCollectionManageBinding
import com.chloe.buytogether.ext.getVmFactory


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

        return binding.root
    }



}