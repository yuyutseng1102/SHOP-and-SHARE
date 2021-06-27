package com.chloe.shopshare.host.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.data.Condition
import com.chloe.shopshare.databinding.DialogHostConditionBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.ConditionEditor
import com.chloe.shopshare.network.LoadApiStatus


class HostConditionDialog(private val editor: ConditionEditor?) : AppCompatDialogFragment() {

    private val viewModel by viewModels<HostConditionViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DialogHostConditionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.spinnerGatherCondition.adapter = ConditionSpannerAdapter(
            MyApplication.instance.resources.getStringArray(R.array.condition_list)
        )

        binding.datePickerButton.setOnClickListener {
            viewModel.datePicker.show(childFragmentManager, "datePicker")
        }

        viewModel.datePicker.addOnPositiveButtonClickListener {
            viewModel.pickDate()
        }

        viewModel.selectedConditionPosition.observe(viewLifecycleOwner, Observer {
            viewModel.hintToShow()
        })

        binding.buttonReady.setOnClickListener {
            viewModel.checkCondition()
            viewModel.status.observe(viewLifecycleOwner, Observer {
                if (it == LoadApiStatus.DONE) {
                    viewModel.showCondition()
                    editor?.onConditionEdited(
                        Condition(
                            viewModel.selectedConditionPosition.value,
                            viewModel.deadLine.value,
                            viewModel.condition.value,
                            viewModel.conditionShow.value
                        )
                    )
                    this.dismiss()
                }
            })
        }
        return binding.root
    }
}