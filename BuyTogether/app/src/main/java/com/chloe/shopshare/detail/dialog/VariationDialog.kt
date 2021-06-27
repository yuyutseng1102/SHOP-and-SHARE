package com.chloe.shopshare.detail.dialog

import android.content.Context
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
import com.chloe.shopshare.databinding.DialogVariationBinding
import com.chloe.shopshare.detail.VariationSelector
import com.chloe.shopshare.ext.getVmFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup


class VariationDialog(private val optionSelector: VariationSelector?, private val cart: Cart): BottomSheetDialogFragment()  {

    private val viewModel by viewModels<VariationViewModel> { getVmFactory(cart) }
    private lateinit var binding : DialogVariationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DetailOptionDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogVariationBinding.inflate(inflater, container, false)
        binding.viewDialog.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_slide_up))
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val viewId = 0

        fun ChipGroup.addChip(context: Context?, option: List<String>) {


            for (i in option.indices) {
                val drawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.WidgetAppChipChoice)
                Chip(context).apply {
                    id = viewId+i
                    text = option[i]
                    isCheckable = true
                    isCloseIconVisible = false
                    isFocusable = true
                    setBackgroundColor(R.drawable.bg_radio_button)
                    setChipDrawable(drawable)
                    addView(this)
                }
                Log.d("Chloe","elements is ${i} to ${option[i]}")
            }

        }


        viewModel.selectedChip.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getOption()
                Log.d("Chloe","what i choose is ${viewModel.productTitle.value}")
            }
        })





        var lastCheckedId = View.NO_ID
        binding.chipGroupVariation.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == View.NO_ID) {
                // User tried to uncheck, make sure to keep the chip checked
                group.check(lastCheckedId)
                return@setOnCheckedChangeListener
            }
            lastCheckedId = checkedId

            // New selection happened, do your logic here.
            viewModel.selectedChip.value = lastCheckedId
            Log.d("Chloe","checkid  is ${lastCheckedId}")

        }


        viewModel.option.observe(viewLifecycleOwner, Observer {
            it?.let {
        binding.chipGroupVariation.addChip(context,viewModel.option.value!!)
            }
        })

        viewModel.productTitle.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.isEditable()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                optionSelector?.onVariationSelector(it)
                viewModel.onDetailNavigated()
                this.dismiss()
            }

        })

        viewModel.navigateToOrder.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToOrderFragment(it))
                optionSelector?.onVariationSelector(it.products)
                viewModel.onOrderNavigated()
                this.dismiss()
            }
        })

        return binding.root
    }


}
