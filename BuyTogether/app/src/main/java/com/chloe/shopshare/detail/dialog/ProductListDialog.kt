package com.chloe.shopshare.detail.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.Product
import com.chloe.shopshare.databinding.DialogProductListBinding
import com.chloe.shopshare.detail.ProductListCheck
import com.chloe.shopshare.ext.getVmFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ProductListDialog(private val shop: Shop, private val productList: List<Product>, private val productListCheck: ProductListCheck?) : BottomSheetDialogFragment()  {

    private val viewModel by viewModels<ProductListViewModel> { getVmFactory(shop,productList) }
    private lateinit var binding : DialogProductListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DetailOptionDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogProductListBinding.inflate(inflater, container, false)
        binding.viewDialog.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_slide_up))
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = ProductListAdapter(viewModel)

        viewModel.product.observe(viewLifecycleOwner, Observer {
            Log.d("Chloe","product is change to ${viewModel.product.value}")
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
                viewModel.isEnable()
            }
        })


        viewModel.navigateToParticipate.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (viewModel.shop.value!=null)
                    Log.d("Chloe","navigate to participate is product = ${it.toTypedArray()}")
                findNavController().navigate(NavigationDirections.navigateToParticipateFragment(
                    viewModel.shop.value!!,
                    it.toTypedArray()
                ))
            }

        })

        binding.recyclerProductList.adapter = adapter

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Chloe","navigateToDetails is ${viewModel.navigateToDetails.value},navigateToParticipate is ${viewModel.navigateToParticipate.value}")
        Log.d("Chloe","ProductList is Destroy")
        if (viewModel.navigateToParticipate.value == null) {
            Log.d("Chloe","Pop back to Detail")
            if (viewModel.product.value != null) {
                Log.d("Chloe","viewModel.product.value is ${viewModel.product.value}")
                viewModel.navigateToDetails(viewModel.product.value!!)
                productListCheck?.onProductListCheck(viewModel.navigateToDetails.value!!)
                viewModel.onDetailsNavigated()
                this.dismiss()
            }
        }else{
            Log.d("Chloe","Pop to Participate")
            viewModel.onParticipateNavigated()
        }





    }


}