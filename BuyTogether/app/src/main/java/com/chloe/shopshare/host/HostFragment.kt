package com.chloe.shopshare.host

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.chloe.shopshare.data.Condition
import com.chloe.shopshare.data.Variation
import com.chloe.shopshare.databinding.FragmentHostBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.item.HostConditionDialog
import com.chloe.shopshare.host.item.HostVariationDialog


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
        val imageAdapter = HostImageAdapter(viewModel)
        binding.recyclerImage.adapter = imageAdapter

        /** Set up selector.**/

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

        /** Init select position if it is lead from request .**/

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

        /** get the selector value.**/

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

        viewModel.option.observe(viewLifecycleOwner, Observer {
            viewModel.checkOption()
        })

        viewModel.conditionContent.observe(viewLifecycleOwner, Observer {
            viewModel.checkCondition()
        })

        viewModel.imagesPicked.observe(viewLifecycleOwner, Observer {
            imageAdapter.notifyDataSetChanged()
        })

        /** Click on the post button to trigger the checking for whether there is any field empty.
         * If all fields are edited done, upload the image to server. **/

        binding.buttonAdd.setOnClickListener {
            viewModel.isShopInvalid()
            viewModel.isInvalid.observe(viewLifecycleOwner, Observer {
                when (it) {
                    null -> viewModel.imagesPicked.value?.let { image ->
                        viewModel.uploadImages(
                            image
                        )
                    }
                    else -> Log.d("HostTag", "There is something invalid")
                }
            })
        }

        /** After uploading images successfully, start to post shop to Server.**/

        viewModel.uploadImageDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.editShop()
                viewModel.onImageUploadDone()
            }
        })

        /** After successful post, before leaving the fragment, need to distinguish whether
         * the host is lead by other's request.
         * If not -> Show Success.
         * If is -> Notify the related people(to requester -> to member -> update host info).
         **/

        viewModel.postShopDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (viewModel.request.value) {
                    null -> showDialog()
                    else -> viewModel.editRequesterNotify()
                }
                viewModel.onShopPostDone()
            }
        })

        viewModel.notifyRequesterDone.observe(viewLifecycleOwner, Observer {
            it.let {
                viewModel.editMemberNotify()
                viewModel.onRequesterNotifyDone()
            }
        })

        viewModel.notifyMemberDone.observe(viewLifecycleOwner, Observer {
            it.let {
                viewModel.updateHost()
                viewModel.onMemberNotifyDone()
            }
        })

        /** After notifying successfully, show Success.**/

        viewModel.updateRequestHostDone.observe(viewLifecycleOwner, Observer {
            it?.let {
                showDialog()
                viewModel.onRequestHostUpdateDone()
            }
        })

        binding.addGatherCondition.setOnClickListener {
            val dialog = HostConditionDialog(
                object : ConditionEditor {
                override fun onConditionEdited(condition: Condition) {
                    viewModel.conditionType.value = condition.type
                    viewModel.deadLine.value = condition.deadLine
                    viewModel.condition.value = condition.value
                    viewModel.conditionContent.value = condition.content
                }
            })
            dialog.show(childFragmentManager, "Condition Edit")
        }

        binding.addGatherOption.setOnClickListener {
            val dialog = HostVariationDialog(
                optionAdd = object : VariationEditor {
                    override fun onVariationEdited(variation: Variation) {
                        viewModel.option.value = variation.value
                        viewModel.isStandard.value = variation.isStandard
                        viewModel.optionContent.value = variation.content
                    }
                },
                oldOption = viewModel.option.value,
                oldIsStandard = viewModel.isStandard.value ?: false

            )
            dialog.show(childFragmentManager, "Option Edit")
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
        viewModel.shopId.value?.let { id ->
            findNavController().navigate(NavigationDirections.navigateToDetailFragment(id))
        }
    }

}

interface ConditionEditor {
    fun onConditionEdited(condition: Condition)
}

interface VariationEditor {
    fun onVariationEdited(variation: Variation)
}
