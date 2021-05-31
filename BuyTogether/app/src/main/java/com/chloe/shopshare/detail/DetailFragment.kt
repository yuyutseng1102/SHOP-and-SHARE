package com.chloe.shopshare.detail

import android.os.Bundle
import android.util.Log
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
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.databinding.FragmentDetailBinding
import com.chloe.shopshare.detail.dialog.DetailOptionDialog
import com.chloe.shopshare.detail.dialog.ProductListDialog
import com.chloe.shopshare.detail.item.DetailCircleAdapter
import com.chloe.shopshare.detail.item.DetailDeliveryAdapter
import com.chloe.shopshare.detail.item.DetailImageAdapter
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.network.LoadApiStatus
import com.google.android.material.tabs.TabLayout


class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel by viewModels<DetailViewModel> { getVmFactory(args.shopIdKey) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerDeliveryList.adapter = DetailDeliveryAdapter()
        binding.recyclerDetailImage.adapter = DetailImageAdapter()
        binding.recyclerDetailCircles.adapter = DetailCircleAdapter()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerDetailImage)
        }

        binding.recyclerDetailImage.setOnScrollChangeListener { _, _, _, _, _ ->
            viewModel.onGalleryScrollChange(
                binding.recyclerDetailImage.layoutManager,
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
                binding.viewpagerDetail.adapter = DetailPagerAdapter(childFragmentManager,it.description)
                viewModel.getUserProfile(it.userId)
            }
        })





        viewModel.navigateToOption.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (viewModel.shop.value !=null) {
                    val dialog =
                        DetailOptionDialog(
                            viewModel.shop.value!!,
                            it,
                            object : OptionSelector {
                                override fun onOptionSelector(product: List<Product>) {
                                    viewModel.updateProductList(product)
                                    Log.d(
                                        "Chloe",
                                        "option dialog is Success! optionlist is ${viewModel.product.value}"
                                    )
                                }
                            }

                        )
                    viewModel.onOptionNavigated()
                    dialog.show(childFragmentManager, "hiya")
                }
            }
        })

        viewModel.navigateToProductList.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Chloe","navigateToProductList = $it")
                if (viewModel.shop.value !=null){
                    val dialog =
                        ProductListDialog(
                            viewModel.shop.value!!,
                            it,
                            object : ProductListCheck {
                                override fun onProductListCheck(product: List<Product>) {
                                    viewModel.updateProductList(product)
                                    Log.d(
                                        "Chloe",
                                        "option dialog is Success! optionlist is ${viewModel.product.value}"
                                    )
                                }
                            }
                        )
                    viewModel.onProductListNavigated()
                    dialog.show(childFragmentManager, "hiya")
                }
            }
        })

        binding.deliveryExpandButton.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> viewModel.isChecked.value = true
                else -> viewModel.isChecked.value = false
            }
            Log.d("Chloe","isChecked=${isChecked}deliveryExpandButton is ${viewModel.isChecked.value}"
            )
        }



        binding.navHome.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToHomeFragment())
        }

        binding.navFollow.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToHomeFragment())
        }

//        viewModel.productItem.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                viewModel.addToProductList()
//            }
//        })




        return binding.root
    }

}

interface OptionSelector {
    fun onOptionSelector(
        product: List<Product>
    )
}

interface ProductListCheck {
    fun onProductListCheck(
        product: List<Product>
    )
}