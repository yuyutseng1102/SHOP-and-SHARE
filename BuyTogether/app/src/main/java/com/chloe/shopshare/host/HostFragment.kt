package com.chloe.shopshare.host

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentHostBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.item.GatherConditionDialog
import com.chloe.shopshare.host.item.GatherOptionDialog


class HostFragment : Fragment() {

    private val args: HostFragmentArgs by navArgs()
    private val viewModel by viewModels<HostViewModel> { getVmFactory(args.requestInfoKey) }
    private lateinit var binding: FragmentHostBinding
    private val pickImageFile = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val categoryAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_host_category_spinner,
            MyApplication.instance.resources.getStringArray(R.array.category_list)
        )
        (binding.menuHostCategory.editText as? AutoCompleteTextView)?.setAdapter(categoryAdapter)

        val countryAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_host_country_spinner,
            MyApplication.instance.resources.getStringArray(R.array.country_list)
        )
        (binding.menuHostCountry.editText as? AutoCompleteTextView)?.setAdapter(countryAdapter)

        viewModel.initCategoryPosition.observe(viewLifecycleOwner, Observer {
            it.let {
                (binding.menuHostCategory.editText as? AutoCompleteTextView)?.setText(
                    MyApplication.instance.resources.getStringArray(
                        R.array.category_list
                    )[it], false
                )
            }
        })

        viewModel.initCountryPosition.observe(viewLifecycleOwner, Observer {
            it.let {
                (binding.menuHostCountry.editText as? AutoCompleteTextView)?.setText(
                    MyApplication.instance.resources.getStringArray(
                        R.array.country_list
                    )[it], false
                )
            }
        })

        viewModel.selectedCategoryTitle.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    viewModel.convertCategoryTitleToInt(it)
                }
            }
        })

        viewModel.selectedCountryTitle.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    viewModel.convertCountryTitleToInt(it)
                }
            }
        })

        viewModel.option.observe(viewLifecycleOwner, Observer {
            viewModel.checkOption()
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
            viewModel.readyToPost()
            viewModel.isInvalid.observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    viewModel.image.value?.let { image ->
                        viewModel.uploadImages(image)
                    }
                }
            })
        }

        viewModel.uploadDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.postGatherCollection()
                viewModel.onImageUploadDone()
            }
        })

        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (viewModel.request.value) {
                    null -> {
                        showDialog()
                        viewModel.shopId.value?.let { id ->
                            findNavController().navigate(
                                NavigationDirections.navigateToDetailFragment(id)
                            )
                        }
                    }
                    else -> viewModel.editNotify()
                }
                viewModel.onLeft()
            }
        })

        viewModel.notifyRequestMemberDone.observe(viewLifecycleOwner, Observer {
            it.let {
                viewModel.updateHost()
                viewModel.onRequestMemberNotifyDone()
            }
        })

        viewModel.updateRequestHostDone.observe(viewLifecycleOwner, Observer {
            it.let {
                showDialog()
                viewModel.shopId.value?.let { id ->
                    findNavController().navigate(NavigationDirections.navigateToDetailFragment(id))
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
                    viewModel.conditionType.value = conditionType
                    viewModel.deadLine.value = deadLine
                    viewModel.condition.value = condition
                    viewModel.conditionShow.value = conditionShow
                }
            })
            dialog.show(childFragmentManager, "Condition Edit")
        }

        binding.addGatherOption.setOnClickListener {
            val dialog = GatherOptionDialog(
                optionAdd = object : OptionAdd {
                    override fun onOptionAdded(
                        option: List<String>,
                        isStandard: Boolean,
                        optionShow: String
                    ) {
                        viewModel.option.value = option
                        viewModel.isStandard.value = isStandard
                        viewModel.optionShow.value = optionShow
                    }
                },
                oldOption = viewModel.option.value,
                oldIsStandard = viewModel.isStandard.value ?: false

            )
            dialog.show(childFragmentManager, "Option Edit")
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        when (requestCode) {
            pickImageFile -> {
                if (resultCode == Activity.RESULT_OK && resultData != null) {
                    resultData.data?.let { uri -> viewModel.pickImages(uri) }
                }
            }
        }
    }

    private fun showDialog() {
        val successDialog = Dialog(this.requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_success, null)
        successDialog.setContentView(view)
        successDialog.show()
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
