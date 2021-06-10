package com.chloe.shopshare.host.item

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.DialogHostConditionBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.ConditionSelector
import com.chloe.shopshare.network.LoadApiStatus


class GatherConditionDialog(private val conditionSelector: ConditionSelector?) : AppCompatDialogFragment() {

    private val viewModel by viewModels<GatherConditionViewModel> { getVmFactory() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DialogHostConditionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.spinnerGatherCondition.adapter = ConditionSpannerAdapter(
            MyApplication.instance.resources.getStringArray(R.array.condition_list))



        binding.datePickerButton.setOnClickListener {
            viewModel.datePicker.show(childFragmentManager, "tag")
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
                Log.d("Chloe","status = ${viewModel.status.value}")
                if (it == LoadApiStatus.DONE){
                    viewModel.showCondition()
                    viewModel.setCondition()
                    conditionSelector?.onConditionSelected(
                            viewModel.selectedConditionPosition.value,
                            viewModel.deadLine.value,
                            viewModel.condition.value,
                            viewModel.conditionShow.value
                    )
                    Log.d("Chloe","ready to put conditionType = ${viewModel.selectedConditionPosition.value}")
                    Log.d("Chloe","ready to put deadLine = ${viewModel.deadLine.value}")
                    Log.d("Chloe","ready to put condition = ${viewModel.condition.value}")

                    this.dismiss()
                }
            })
        }




        return binding.root
    }


}