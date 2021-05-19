package com.chloe.buytogether.collection.manage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.chloe.buytogether.databinding.FragmentCollectionManageBinding
import com.chloe.buytogether.ext.getVmFactory
import okhttp3.internal.notifyAll


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


            viewModel.member.observe(viewLifecycleOwner, Observer {
                it?.let {

                    Log.d("Life", "member is change")

                    memberAdapter.notifyDataSetChanged()

                }

            })



    viewModel.order.observe(viewLifecycleOwner, Observer {
        it?.let {

            Log.d("Life", "order is change")
//            memberAdapter.submitList(it)
            memberAdapter.notifyDataSetChanged()

        }


    })




        binding.deleteButton.setOnClickListener {
            viewModel.deleteMember()
        }


        return binding.root
    }

}