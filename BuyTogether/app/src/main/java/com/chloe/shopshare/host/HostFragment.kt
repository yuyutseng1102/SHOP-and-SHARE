package com.chloe.shopshare.host

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentHostBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.item.CategorySpannerAdapter
import com.chloe.shopshare.host.item.CountrySpannerAdapter
import com.chloe.shopshare.host.item.GatherConditionDialog
import com.chloe.shopshare.host.item.GatherOptionDialog
import com.chloe.shopshare.network.LoadApiStatus


class HostFragment : Fragment() {

    private val viewModel by viewModels<HostViewModel> { getVmFactory() }
    private lateinit var binding: FragmentHostBinding
    private val pickImageFile = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.spinnerGatherCategory.adapter = CategorySpannerAdapter(
            MyApplication.instance.resources.getStringArray(R.array.category_list)
        )

        binding.spinnerGatherCountry.adapter = CountrySpannerAdapter(
            MyApplication.instance.resources.getStringArray(R.array.country_list)
        )

        viewModel.categoryType.observe(viewLifecycleOwner, Observer {
            viewModel.selectCategory()
        })

        viewModel.countryType.observe(viewLifecycleOwner, Observer {
            viewModel.selectCountry()
        })

        viewModel.conditionShow.observe(viewLifecycleOwner, Observer {
            viewModel.checkCondition()
        })


        val imageAdapter = HostImageAdapter(viewModel)
        binding.recyclerImage.adapter = imageAdapter
        viewModel.image.observe(viewLifecycleOwner, Observer {
            imageAdapter.notifyDataSetChanged()
        })





        binding.buttonAdd.setOnClickListener {
            //確認有沒有沒填好的
            viewModel.readyToPost()

            // 都有填寫->上傳照片
            viewModel.isInvalid.observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    viewModel.image.value?.let {
                        viewModel.uploadImages(it)
                    }
                }else{
                    Toast.makeText(context, "尚未輸入完整內容", Toast.LENGTH_SHORT).show()
                }
            })
        }


        //上傳圖片完畢送出
        viewModel.uploadDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) { viewModel.postGatherCollection()}
            } })

        //上傳團購完畢跳轉成功畫面
        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    val successDialog = Dialog(this.requireContext())
                    val view = layoutInflater.inflate(R.layout.dialog_success, null)
                    successDialog.setContentView(view)
                    successDialog.show()
                    findNavController().navigate(NavigationDirections.navigateToHomeFragment())
                }
            }
        })



        binding.addGatherCondition.setOnClickListener {
            val dialog = GatherConditionDialog(object : ConditionSelector {
                override fun onConditionSelected(
                    conditionType: Int?,
                    deadLine: Long?,
                    condition: Int?,
                    conditionShow: String?
                ) {
                    Log.d("Chloe", "dialog is Success!")
                    viewModel.conditionType.value = conditionType
                    viewModel.deadLine.value = deadLine
                    viewModel.condition.value = condition
                    viewModel.conditionShow.value = conditionShow
                }
            })
            dialog.show(childFragmentManager, "hiya")
        }

        binding.addGatherOption.setOnClickListener {
            Log.d("Chloe", "oldOption is ${viewModel.option.value}")
            val dialog = GatherOptionDialog(
                optionAdd = object : OptionAdd {
                    override fun onOptionAdded(
                        option: List<String>,
                        isStandard: Boolean,
                        optionShow: String
                    ) {
                        Log.d(
                            "Chloe",
                            "option dialog is Success! optionlist is ${viewModel.option.value}"
                        )
                        viewModel.option.value = option
                        viewModel.isStandard.value = isStandard
                        viewModel.optionShow.value = optionShow
                    }
                },
                oldOption = viewModel.option.value,
                oldIsStandard = viewModel.isStandard.value ?: false

            )
            dialog.show(childFragmentManager, "hiya")
        }


        binding.checkBoxSevenEleven.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> viewModel.selectDelivery(DeliveryMethod.SEVEN_ELEVEN.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.SEVEN_ELEVEN.delivery)
            }
        }
        binding.checkBoxFamilyMart.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> viewModel.selectDelivery(DeliveryMethod.FAMILY_MART.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.FAMILY_MART.delivery)
            }
        }
        binding.checkBoxHiLife.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> viewModel.selectDelivery(DeliveryMethod.HI_LIFE.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.HI_LIFE.delivery)
            }
        }
        binding.checkBoxOk.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> viewModel.selectDelivery(DeliveryMethod.OK.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.OK.delivery)
            }
        }
        binding.checkBoxHomeDelivery.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> viewModel.selectDelivery(DeliveryMethod.HOME_DELIVERY.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.HOME_DELIVERY.delivery)
            }
        }
        binding.checkBoxByHand.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> viewModel.selectDelivery(DeliveryMethod.BY_HAND.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.BY_HAND.delivery)
            }
        }

        binding.pickImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, pickImageFile)
        }


        return binding.root
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?
    ) {

        when (requestCode) {
            pickImageFile -> {
                if (resultCode == Activity.RESULT_OK && resultData != null)
                    resultData.data?.let { uri ->
                        viewModel.pickImages(uri)
                    }

            }
        }
    }

}

interface ConditionSelector {
    fun onConditionSelected(
        conditionType: Int?,
        deadLine: Long?,
        condition: Int?,
        conditionShow: String?
    )
}

interface OptionAdd {
    fun onOptionAdded(
        option: List<String>,
        isStandard: Boolean,
        optionShow: String

    )
}


//        viewModel.imageUri.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                Log.d("Chloe","imgUri is change to ${viewModel.imageUri.value}")
//                viewModel.pickImages()
//            }
//        })

//    override fun onActivityResult(
//            requestCode: Int, resultCode: Int, resultData: Intent?) {
//
//        when (requestCode) {
//            pickImageFile -> {
//                if (resultCode == Activity.RESULT_OK && resultData != null)
//                    resultData.data?.let { uri -> viewModel.uploadImages(uri)}
//            }
//        }
//    }