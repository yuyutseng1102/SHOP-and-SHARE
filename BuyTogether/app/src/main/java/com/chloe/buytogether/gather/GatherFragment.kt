package com.chloe.buytogether.gather

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chloe.buytogether.MyApplication
import com.chloe.buytogether.R
import com.chloe.buytogether.databinding.FragmentGatherBinding
import com.chloe.buytogether.databinding.FragmentHomeBinding
import com.chloe.buytogether.ext.getVmFactory
import com.chloe.buytogether.gather.item.CategorySpannerAdapter
import com.chloe.buytogether.gather.item.CountrySpannerAdapter
import com.chloe.buytogether.gather.item.GatherConditionDialog
import com.chloe.buytogether.home.HomeAdapter
import com.chloe.buytogether.home.item.HomeCollectViewModel
import com.chloe.buytogether.home.item.HomePageViewModel
import com.chloe.buytogether.home.item.HomeSpinnerAdapter
import com.chloe.buytogether.network.LoadApiStatus
import com.google.android.material.tabs.TabLayout


class GatherFragment : Fragment() {

    private val viewModel by viewModels<GatherViewModel> { getVmFactory() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentGatherBinding.inflate(inflater,container,false)
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

        binding.buttonAdd.setOnClickListener {

            viewModel.readyToPost()
            viewModel.status.observe(viewLifecycleOwner, Observer {
                if (viewModel.status.value == LoadApiStatus.LOADING) {
                    Toast.makeText(context, "尚未輸入完整內容", Toast.LENGTH_SHORT).show()
                } else if (viewModel.status.value == LoadApiStatus.DONE) {
                    Toast.makeText(context, "成功送出", Toast.LENGTH_SHORT).show()
                    viewModel.postGatherCollection()
                }
            }
            )
            Log.d("Chloe", "The new collection is ${viewModel.collection.value}")
        }

        binding.addGatherCondition.setOnClickListener{
            val dialog = GatherConditionDialog(object : ConditionSelector {

                override fun onConditionSelected() {
                    Log.d("Chloe","dialog is Success!")
                }
            })
            dialog.show(childFragmentManager, "hiya")
        }

        return binding.root
    }

}

interface ConditionSelector {
    fun onConditionSelected()
}