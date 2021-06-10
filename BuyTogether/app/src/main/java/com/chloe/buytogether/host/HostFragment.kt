package com.chloe.buytogether.host

import android.app.Dialog
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
import com.chloe.buytogether.MyApplication
import com.chloe.buytogether.NavigationDirections
import com.chloe.buytogether.R
import com.chloe.buytogether.databinding.FragmentHostBinding
import com.chloe.buytogether.ext.getVmFactory
import com.chloe.buytogether.host.item.CategorySpannerAdapter
import com.chloe.buytogether.host.item.CountrySpannerAdapter
import com.chloe.buytogether.host.item.GatherConditionDialog
import com.chloe.buytogether.host.item.GatherOptionDialog
import com.chloe.buytogether.network.LoadApiStatus


class HostFragment : Fragment() {

    private val viewModel by viewModels<HostViewModel> { getVmFactory() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentHostBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.spinnerGatherCategory.adapter = CategorySpannerAdapter(
                MyApplication.instance.resources.getStringArray(R.array.category_list))

        binding.spinnerGatherCountry.adapter = CountrySpannerAdapter(
                MyApplication.instance.resources.getStringArray(R.array.country_list))


        viewModel.categoryType.observe(viewLifecycleOwner, Observer {
            viewModel.selectCategory()
        })

        viewModel.countryType.observe(viewLifecycleOwner, Observer {
            viewModel.selectCountry()
        })

        viewModel.conditionShow.observe(viewLifecycleOwner, Observer {
            viewModel.checkCondition()
        })
        val successDialog = Dialog(this.requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_success, null)
        binding.buttonAdd.setOnClickListener {

            viewModel.readyToPost()
            viewModel.status.observe(viewLifecycleOwner, Observer {
                if (viewModel.status.value == LoadApiStatus.LOADING) {
                    Toast.makeText(context, "尚未輸入完整內容", Toast.LENGTH_SHORT).show()
                } else if (viewModel.status.value == LoadApiStatus.DONE) {
                    Toast.makeText(context, "成功送出", Toast.LENGTH_SHORT).show()
                    viewModel.postGatherCollection()
                    successDialog.setContentView(view)
                    successDialog.show()

                    findNavController().navigate(
                            NavigationDirections.navigateToHomeFragment()
                    )
                }
            }
            )
            Log.d("Chloe", "The new collection is ${viewModel.collection.value}")
        }



        binding.addGatherCondition.setOnClickListener{

            val dialog = GatherConditionDialog(object : ConditionSelector {

                override fun onConditionSelected(conditionType:Int?,
                                                 deadLine:Long?,
                                                 condition:Int?,
                                                 conditionShow:String?) {
                    Log.d("Chloe","dialog is Success!")
                    viewModel.conditionType.value = conditionType
                    viewModel.deadLine.value = deadLine
                    viewModel.condition.value = condition
                    viewModel.conditionShow.value = conditionShow
                }
            })
            dialog.show(childFragmentManager, "hiya")
        }

        binding.addGatherOption.setOnClickListener{
            Log.d("Chloe","oldOption is ${viewModel.option.value}")
            val dialog = GatherOptionDialog(
                    optionAdd = object : OptionAdd {
                        override fun onOptionAdded(option: List<String>,isStandard:Boolean,optionShow:String) {
                            Log.d("Chloe","option dialog is Success! optionlist is ${viewModel.option.value}")
                            viewModel.option.value = option
                            viewModel.isStandard.value = isStandard
                            viewModel.optionShow.value = optionShow
                        }},
                    oldOption = viewModel.option.value,
                    oldIsStandard = viewModel.isStandard.value?:false

            )
            dialog.show(childFragmentManager, "hiya")
        }


        binding.checkBoxSevenEleven.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> viewModel.selectDelivery(DeliveryMethod.SEVEN_ELEVEN.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.SEVEN_ELEVEN.delivery)
            }
        }
        binding.checkBoxFamilyMart.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> viewModel.selectDelivery(DeliveryMethod.FAMILY_MART.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.FAMILY_MART.delivery)
            }
        }
        binding.checkBoxHiLife.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> viewModel.selectDelivery(DeliveryMethod.HI_LIFE.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.HI_LIFE.delivery)
            }
        }
        binding.checkBoxOk.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> viewModel.selectDelivery(DeliveryMethod.OK.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.OK.delivery)
            }
        }
        binding.checkBoxHomeDelivery.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> viewModel.selectDelivery(DeliveryMethod.HOME_DELIVERY.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.HOME_DELIVERY.delivery)
            }
        }
        binding.checkBoxByHand.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> viewModel.selectDelivery(DeliveryMethod.BY_HAND.delivery)
                else -> viewModel.removeDelivery(DeliveryMethod.BY_HAND.delivery)
            }
        }


        return binding.root
    }

}

interface ConditionSelector {
    fun onConditionSelected(
        conditionType:Int?,
        deadLine:Long?,
        condition:Int?,
        conditionShow:String?
    )
}

interface OptionAdd {
    fun onOptionAdded(
            option: List<String>,
            isStandard: Boolean,
            optionShow:String

    )
}