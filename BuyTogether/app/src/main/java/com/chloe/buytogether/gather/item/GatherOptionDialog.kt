package com.chloe.buytogether.gather.item

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chloe.buytogether.databinding.DialogGatherOptionBinding
import com.chloe.buytogether.ext.getVmFactory
import com.chloe.buytogether.gather.OptionAdd
import com.chloe.buytogether.network.LoadApiStatus


class GatherOptionDialog(private val optionAdd: OptionAdd?,private val oldOption:List<String>?,private val oldIsStandard:Boolean) : AppCompatDialogFragment() {

    private val viewModel by viewModels<GatherOptionViewModel> { getVmFactory(oldOption,oldIsStandard) }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DialogGatherOptionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = GatherOptionAdapter()
        binding.recyclerOption.adapter = adapter

        binding.addOption.setOnClickListener {
            viewModel.addOption()
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