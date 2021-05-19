package com.chloe.buytogether.collection.manage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.chloe.buytogether.databinding.FragmentCollectionManageBinding
import com.chloe.buytogether.databinding.ItemCollectionManageMemberBinding
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

        binding.buttonGather.setOnClickListener{
            viewModel.readyCollect()
            memberAdapter.notifyDataSetChanged()
        }
        viewModel.member.observe(viewLifecycleOwner, Observer{
            it?.let {  memberAdapter.notifyDataSetChanged()}

        })

        viewModel.paymentStatus.observe(viewLifecycleOwner, Observer{
            it?.let {  memberAdapter.notifyDataSetChanged()}

        })


        binding.deleteButton.setOnClickListener {
            viewModel.deleteMember()
            memberAdapter.notifyDataSetChanged()

        }

        return binding.root
    }

}