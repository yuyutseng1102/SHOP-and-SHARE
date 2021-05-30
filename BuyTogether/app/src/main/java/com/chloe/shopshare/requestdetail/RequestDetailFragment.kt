package com.chloe.shopshare.requestdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSnapHelper
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentDetailBinding
import com.chloe.shopshare.databinding.FragmentRequestDetailBinding
import com.chloe.shopshare.detail.DetailFragmentArgs
import com.chloe.shopshare.detail.DetailViewModel
import com.chloe.shopshare.detail.item.DetailCircleAdapter
import com.chloe.shopshare.detail.item.DetailDeliveryAdapter
import com.chloe.shopshare.detail.item.DetailImageAdapter
import com.chloe.shopshare.ext.getVmFactory

class RequestDetailFragment : Fragment() {

    private val args: RequestDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<RequestDetailViewModel> { getVmFactory(args.requestIdKey) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRequestDetailBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerDetailImage.adapter = RequestDetailImageAdapter()
        binding.recyclerDetailCircles.adapter = RequestDetailCircleAdapter()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerDetailImage)
        }

        binding.recyclerDetailImage.setOnScrollChangeListener { _, _, _, _, _ ->
            viewModel.onGalleryScrollChange(
                binding.recyclerDetailImage.layoutManager,
                linearSnapHelper
            )
        }

        binding.buttonHost.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToHostFragment())
        }


        binding.navHome.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToHomeFragment())
        }

        binding.navFollow.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToHomeFragment())
        }


        return binding.root
    }

}