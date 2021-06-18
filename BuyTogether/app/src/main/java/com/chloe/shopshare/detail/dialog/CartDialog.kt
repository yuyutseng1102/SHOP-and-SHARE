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
import com.chloe.shopshare.data.Cart
import com.chloe.shopshare.databinding.DialogCartBinding
import com.chloe.shopshare.detail.CartCheck
import com.chloe.shopshare.ext.getVmFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CartDialog(private val productListCheck: CartCheck?, private val cart: Cart) : BottomSheetDialogFragment()  {

    private val viewModel by viewModels<CartViewModel> { getVmFactory(cart) }
    private lateinit var binding: DialogCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DetailOptionDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCartBinding.inflate(inflater, container, false)
        binding.viewDialog.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_slide_up))
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = CartAdapter(viewModel)

        viewModel.products.observe(viewLifecycleOwner, Observer {
            Log.d("Chloe","product is change to ${viewModel.products.value}")
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
                viewModel.isEnable()
            }
        })


        viewModel.navigateToOrder.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToOrderFragment(it))
                this.dismiss()
            }

        })


        binding.recyclerProducts.adapter = adapter

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()

        /**When Dialog dismiss, define it is left to Order Fragment or back to Detail Fragment **/


        when (viewModel.navigateToOrder.value) {
            /** Back to Detail Fragment -> Update the product quantity of navArgs to Detail Fragment**/
            null -> {
                    viewModel.navigateToDetails(viewModel.products.value?: listOf())
                    productListCheck?.onCartCheck(viewModel.navigateToDetails.value?: listOf())
                    viewModel.onDetailsNavigated()
                    this.dismiss()
            }
            /** nav to Detail Fragment -> Just delete the value of navigateToOrder **/
            else -> viewModel.onOrderNavigated()
        }

    }


}