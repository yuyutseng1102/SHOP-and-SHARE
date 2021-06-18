package com.chloe.shopshare.requestdetail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSnapHelper
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentDetailBinding
import com.chloe.shopshare.databinding.FragmentRequestDetailBinding
import com.chloe.shopshare.detail.DetailFragmentArgs
import com.chloe.shopshare.detail.DetailViewModel
import com.chloe.shopshare.detail.item.DetailCircleAdapter
import com.chloe.shopshare.detail.item.DetailDeliveryAdapter
import com.chloe.shopshare.detail.item.DetailImageAdapter
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.HostFragment
import com.chloe.shopshare.host.HostViewModel

class RequestDetailFragment : Fragment() {

    private val args: RequestDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<RequestDetailViewModel> { getVmFactory(args.requestKey) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRequestDetailBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerImage.adapter = RequestDetailImageAdapter()
        binding.recyclerImageCircles.adapter = RequestDetailCircleAdapter()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerImage)
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

        val bundle = Bundle()
        viewModel.request.value?.let {
            bundle.putString("requestId", it.id)
        }

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