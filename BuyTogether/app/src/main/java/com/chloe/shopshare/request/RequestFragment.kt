package com.chloe.shopshare.request

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.MyApplication
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentRequestBinding
import com.chloe.shopshare.ext.getVmFactory


class RequestFragment : Fragment() {

    private val viewModel by viewModels<RequestViewModel> { getVmFactory() }
    private lateinit var binding: FragmentRequestBinding
    private val pickImageFile = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRequestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val categoryAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_host_category_spinner,
            MyApplication.instance.resources.getStringArray(R.array.category_list)
        )

        (binding.menuRequestCategory.editText as? AutoCompleteTextView)?.setAdapter(categoryAdapter)

        val countryAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_host_country_spinner,
            MyApplication.instance.resources.getStringArray(R.array.country_list)
        )

        (binding.menuRequestCountry.editText as? AutoCompleteTextView)?.setAdapter(countryAdapter)

        viewModel.categoryTitle.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    viewModel.convertTitleToValue(true, it)
                }
            }
        })

        viewModel.countryTitle.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    viewModel.convertTitleToValue(false, it)
                }
            }
        })

        val imageAdapter = RequestImageAdapter(viewModel)

        binding.recyclerImage.adapter = imageAdapter

        viewModel.imagesPicked.observe(viewLifecycleOwner, Observer {
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
                when (it) {
                    null -> {
                        viewModel.imagesPicked.value?.let { image ->
                            viewModel.uploadImages(image)
                        }
                    }
                    else -> Toast.makeText(context, "尚未輸入完整內容", Toast.LENGTH_SHORT).show()
                }
            })
        }

        viewModel.uploadDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.editRequest()
                viewModel.onUploadDone()
            }
        })

        viewModel.postDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                showDialog()
                viewModel.onPostDone()
                findNavController().navigate(
                    NavigationDirections.navigateToHomeFragment()
                )
            }
        })

        return binding.root
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?
    ) {

        when (requestCode) {
            pickImageFile -> {
                if (resultCode == Activity.RESULT_OK && resultData != null)
                    resultData.data?.let { uri -> viewModel.pickImages(uri) }
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
