package com.chloe.shopshare.host.item

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.DialogHostOptionBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.OptionAdd
import com.chloe.shopshare.network.LoadApiStatus
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup


class GatherOptionDialog(private val optionAdd: OptionAdd?, private var oldOption:List<String>?, private val oldIsStandard:Boolean) : AppCompatDialogFragment() {

    private val viewModel by viewModels<GatherOptionViewModel> { getVmFactory(oldOption,oldIsStandard) }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DialogHostOptionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

//        val adapter = GatherOptionAdapter()
//        binding.recyclerOption.adapter = adapter


        fun ChipGroup.addChip(context: Context?, option: String) {
//            for (i in option.indices) {
                val drawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.WidgetAppChip)
                Chip(context).apply {
                    id = View.generateViewId()
                    text = option
                    isCloseIconVisible = true
                    isFocusable = true
                    setBackgroundColor(R.drawable.bg_radio_button)
                    setChipDrawable(drawable)
                    addView(this)
                    setOnCloseIconClickListener{
                        viewModel.removeOption(option)
                        removeView(it)
                    }
                }
//                Log.d("Chloe","elements is ${i} to ${option[i]}")
//            }

        }

//        Chip(context).setOnCloseIconClickListener {
//            viewModel.removeOption(option)
//            removeView(it)
//        }

        if(oldIsStandard){
            oldOption?.let {
                for (i in it)
                    binding.chipGroupOption.addChip(context,i)
            }
        }

        viewModel.isStandard.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    if (!oldIsStandard && !oldOption.isNullOrEmpty()){
                        viewModel.removeOption(oldOption!![0])
                        oldOption = null
                    }
                }
            }
        }
        )





        binding.addOption.setOnClickListener {
            viewModel.addOption()
            viewModel.optionItem.value?.let {
                    if (it.isNotEmpty())
                    binding.chipGroupOption.addChip(context,it)
                }
            viewModel.clearEditOption()
            binding.customOptionEdit.setText("")
        }





        binding.buttonReady.setOnClickListener {
            viewModel.optionDone()
            viewModel.status.observe(viewLifecycleOwner, Observer {
                Log.d("Chloe", "status = ${viewModel.status.value}")
                if (it == LoadApiStatus.DONE) {
                    viewModel.showOption()
                    Log.d("Chloe", "optionToDisplay = ${viewModel.optionToDisplay.value}")
                    optionAdd?.onOptionAdded(
                            viewModel.option.value!!,
                            viewModel.isStandard.value!!,
                            viewModel.optionToDisplay.value?:""
                    )
                    this.dismiss()
                } else {
                    Toast.makeText(context, "尚未輸入完畢", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return binding.root
    }
}