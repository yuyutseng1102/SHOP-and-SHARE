package com.chloe.buytogether.detail

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
import com.chloe.buytogether.NavigationDirections
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.databinding.FragmentCollectionManageBinding
import com.chloe.buytogether.databinding.FragmentDetailBinding
import com.chloe.buytogether.detail.dialog.DetailOptionDialog
import com.chloe.buytogether.detail.dialog.ProductListDialog
import com.chloe.buytogether.ext.getVmFactory
import com.google.android.material.tabs.TabLayout


class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel by viewModels<DetailViewModel> { getVmFactory(args.collectionKey) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel



        binding.viewpagerDetail.let{
            binding.tabsDetail.setupWithViewPager(it)
            it.adapter = DetailPagerAdapter(childFragmentManager,viewModel.collection.value!!.description)
            it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsDetail))
        }

        viewModel.navigateToOption.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (viewModel.collection.value !=null) {
                    val dialog =
                        DetailOptionDialog(
                            viewModel.collection.value!!,
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
                if (viewModel.collection.value !=null){
                    val dialog =
                        ProductListDialog(
                            viewModel.collection.value!!,
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