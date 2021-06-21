package com.chloe.shopshare.detail

import android.os.Bundle
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
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.databinding.FragmentDetailBinding
import com.chloe.shopshare.detail.dialog.CartDialog
import com.chloe.shopshare.detail.dialog.VariationDialog
import com.chloe.shopshare.detail.item.DetailCircleAdapter
import com.chloe.shopshare.detail.item.DetailDeliveryAdapter
import com.chloe.shopshare.detail.item.DetailImageAdapter
import com.chloe.shopshare.ext.getVmFactory
import com.google.android.material.tabs.TabLayout


class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel by viewModels<DetailViewModel> { getVmFactory(args.shopKey) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        binding.recyclerImage.adapter = DetailImageAdapter()
        binding.recyclerImageCircles.adapter = DetailCircleAdapter()
        binding.recyclerDelivery.adapter = DetailDeliveryAdapter()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerImage)
        }

        binding.recyclerImage.setOnScrollChangeListener { _, _, _, _, _ ->
            viewModel.onGalleryScrollChange(
                binding.recyclerImage.layoutManager,
                linearSnapHelper
            )
        }

        binding.viewpagerDetail.let {
            binding.tabsDetail.setupWithViewPager(it)
            it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsDetail))
        }

        viewModel.shop.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.viewModel = viewModel
                binding.navChat.isEnabled = it.userId != viewModel.myId
                binding.viewpagerDetail.adapter =
                    DetailPagerAdapter(childFragmentManager, it.description)
                viewModel.getUserProfile(it.userId)
            }
        })


        viewModel.navigateToVariation.observe(viewLifecycleOwner, Observer {
            it?.let {
                val dialog =
                    VariationDialog(
                        object : VariationSelector {
                            override fun onVariationSelector(products: List<Product>) {
                                viewModel.updateProductList(products)
                            }
                        },
                        it
                    )
                viewModel.onVariationNavigated()
                dialog.show(childFragmentManager, "Show Variation Dialog")
            }
        })

        viewModel.navigateToCart.observe(viewLifecycleOwner, Observer {
            it?.let {
                val dialog =
                    CartDialog(
                        object : CartCheck {
                            override fun onCartCheck(products: List<Product>) {
                                viewModel.updateProductList(products)
                            }
                        },
                        it
                    )
                viewModel.onCartNavigated()
                dialog.show(childFragmentManager, "Show Cart Dialog")

            }
        })

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToHomeFragment())
                viewModel.onHomeNavigated()
            }
        })

        viewModel.chatRoom.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.navigateToChatRoom(it)
            }
        })

        viewModel.navigateToChatRoom.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToChatRoomFragment(it))
                viewModel.onChatRoomNavigated()
            }
        })

        binding.deliveryExpandButton.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isExpanded(isChecked)
        }

        return binding.root
    }

}

interface VariationSelector {
    fun onVariationSelector(
        products: List<Product>
    )
}

interface CartCheck {
    fun onCartCheck(
        products: List<Product>
    )
}