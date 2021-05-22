package com.chloe.buytogether.participate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
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

        return binding.root
    }

}