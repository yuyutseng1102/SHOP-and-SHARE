package com.chloe.shopshare.request

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentRequestBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.item.CategorySpannerAdapter
import com.chloe.shopshare.host.item.CountrySpannerAdapter


class RequestFragment : Fragment() {

    private val viewModel by viewModels<RequestViewModel> { getVmFactory() }
    private lateinit var binding : FragmentRequestBinding
    private val pickImageFile = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.item_host_category_spinner, MyApplication.instance.resources.getStringArray(R.array.category_list))
        (binding.menuRequestCategory.editText as? AutoCompleteTextView)?.setAdapter(categoryAdapter)

        val countryAdapter = ArrayAdapter(requireContext(), R.layout.item_host_country_spinner, MyApplication.instance.resources.getStringArray(R.array.country_list))
        (binding.menuRequestCountry.editText as? AutoCompleteTextView)?.setAdapter(countryAdapter)


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






        val imageAdapter = RequestImageAdapter(viewModel)
        binding.recyclerImage.adapter = imageAdapter
        viewModel.image.observe(viewLifecycleOwner, Observer {
            Log.d("Chloe","notify imageAdapter the image change to ${viewModel.image.value}")
            imageAdapter.notifyDataSetChanged()
        })


        binding.pickImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, pickImageFile)
        }


        binding.buttonPostRequest.setOnClickListener {
            viewModel.checkRequest()
            viewModel.isInvalid.observe(viewLifecycleOwner, Observer {
                if(it == null){
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
                if (it) { viewModel.editRequest()}
            } })



        viewModel.successPost.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    val successDialog = Dialog(this.requireContext())
                    val view = layoutInflater.inflate(R.layout.dialog_success, null)
                    successDialog.setContentView(view)
                    successDialog.show()

                    findNavController().navigate(
                        NavigationDirections.navigateToHomeFragment()
                    )
                }
            }
        })

        return binding.root
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {

        when (requestCode) {
            pickImageFile -> {
                if (resultCode == Activity.RESULT_OK && resultData != null)
                    resultData.data?.let { uri -> viewModel.pickImages(uri)}
            }
        }
    }
}
