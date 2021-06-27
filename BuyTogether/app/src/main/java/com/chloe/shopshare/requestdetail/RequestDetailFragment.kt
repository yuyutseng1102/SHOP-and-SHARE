package com.chloe.shopshare.requestdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSnapHelper
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentRequestDetailBinding
import com.chloe.shopshare.detail.item.DetailCircleAdapter
import com.chloe.shopshare.ext.getVmFactory

class RequestDetailFragment : Fragment() {

    private val args: RequestDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<RequestDetailViewModel> { getVmFactory(args.requestKey) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        val binding = FragmentRequestDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.recyclerImage.adapter = RequestDetailImageAdapter()
        binding.recyclerImageCircles.adapter = RequestDetailCircleAdapter()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerImage)
        }

        viewModel.snapPosition.observe(viewLifecycleOwner, Observer {
            Log.d("CircleTag", "snapPosition is change to ${viewModel.snapPosition.value}")
            viewModel.request.value?.let { request ->
                (binding.recyclerImageCircles.adapter as RequestDetailCircleAdapter).selectedPosition.value =
                    (it % request.image.size)
            }
        })

        // set the initial position to the center of infinite gallery
        viewModel.request.value?.let { request ->
            binding.recyclerImage.scrollToPosition(request.image.size * 100)
        }

        binding.recyclerImage.setOnScrollChangeListener { _, _, _, _, _ ->
            viewModel.onGalleryScrollChange(
                binding.recyclerImage.layoutManager,
                linearSnapHelper
            )
        }

        viewModel.navigateToHostDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
                viewModel.onHostDetailNavigated()
            }
        })

        viewModel.request.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getUserProfile(it.userId)
            }
        })

        binding.buttonHost.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToHostFragment(viewModel.request.value))
        }
        
        binding.navHome.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToHomeFragment())
        }

        binding.navChat.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToHomeFragment())
        }

        return binding.root
    }

}