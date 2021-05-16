package com.chloe.buytogether.gather.item

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_DATETIME
import android.text.InputType.TYPE_DATETIME_VARIATION_TIME
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.chloe.buytogether.MyApplication
import com.chloe.buytogether.R
import com.chloe.buytogether.databinding.DialogGatherConditionBinding
import com.chloe.buytogether.ext.getVmFactory
import com.chloe.buytogether.ext.toDisplayFormat
import com.chloe.buytogether.gather.ConditionSelector
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class GatherConditionDialog(private val conditionSelector: ConditionSelector?) : AppCompatDialogFragment() {

    private val viewModel by viewModels<GatherConditionViewModel> { getVmFactory() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DialogGatherConditionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.spinnerGatherCondition.adapter = ConditionSpannerAdapter(
            MyApplication.instance.resources.getStringArray(R.array.condition_list))

        binding.buttonReady.setOnClickListener {
            conditionSelector?.onConditionSelected()
            this.dismiss()
        }

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setTheme(R.style.DatePicker)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        binding.datePickerButton.setOnClickListener {
            datePicker.show(childFragmentManager, "tag")
        }








        datePicker.addOnPositiveButtonClickListener {
            val dateToDisplay = datePicker.headerText
            val datePicked = datePicker.selection
            if (datePicked != null) {
                binding.deadLineEdit.text = datePicked.toDisplayFormat()
            }

//            val formatter = DateTimeFormatter.ofPattern("mm dd, jjjj")
//            val dt = LocalDate.parse(datePicked, formatter)
            Log.d("Chloe","from $datePicked")
        }




        return binding.root
    }


}