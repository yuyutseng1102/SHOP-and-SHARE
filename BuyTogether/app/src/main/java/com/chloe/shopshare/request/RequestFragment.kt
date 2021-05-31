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

        binding.spinnerRequestCategory.adapter = CategorySpannerAdapter(
            MyApplication.instance.resources.getStringArray(R.array.category_list))

        binding.spinnerRequestCountry.adapter = CountrySpannerAdapter(
            MyApplication.instance.resources.getStringArray(R.array.country_list))

        viewModel.categoryType.observe(viewLifecycleOwner, Observer {
            viewModel.selectCategory()
        })

        viewModel.countryType.observe(viewLifecycleOwner, Observer {
            viewModel.selectCountry()
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
                val successDialog = Dialog(this.requireContext())
                val view = layoutInflater.inflate(R.layout.dialog_success, null)
                successDialog.setContentView(view)
                successDialog.show()

                findNavController().navigate(
                    NavigationDirections.navigateToHomeFragment()
                )
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
