package com.chloe.buytogether.participate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.databinding.FragmentParticipateBinding
import com.chloe.buytogether.ext.getVmFactory


class ParticipateFragment : Fragment() {

    private val args: ParticipateFragmentArgs by navArgs()

    private val viewModel by viewModels<ParticipateViewModel> { getVmFactory(args.collectionKey,args.productKey.asList()) }

    private lateinit var binding : FragmentParticipateBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParticipateBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        Log.d("Chloe","args.productKey.asList()=${args.productKey.asList()}")

        val adapter = ParticipateAdapter(viewModel)
        binding.recyclerProductList.adapter = adapter

        viewModel.product.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()

            }
        })

        return binding.root
    }

}