package com.chloe.buytogether.collection.manage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
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

        val adapter = MemberAdapter()
        binding.recyclerMember.adapter = adapter

        viewModel.addMockData()

        return binding.root
    }

}