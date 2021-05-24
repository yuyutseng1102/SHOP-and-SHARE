package com.chloe.buytogether.host

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.IOException


class HostFragment : Fragment() {

    private val viewModel by viewModels<HostViewModel> { getVmFactory() }
    private lateinit var binding : FragmentHostBinding
    private val pickImageFile = 2
    private var selectedImageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHostBinding.inflate(inflater, container, false)
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



        binding.addGatherCondition.setOnClickListener {

            val dialog = GatherConditionDialog(object : ConditionSelector {

                override fun onConditionSelected(conditionType: Int?,
                                                 deadLine: Long?,
                                                 condition: Int?,
                                                 conditionShow: String?) {
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
                        override fun onOptionAdded(option: List<String>, isStandard: Boolean, optionShow: String) {
                            Log.d("Chloe", "option dialog is Success! optionlist is ${viewModel.option.value}")
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
            requestCode: Int, resultCode: Int, resultData: Intent?) {

        val contentResolver = requireActivity().contentResolver

        @Throws(IOException::class)
        fun getBitmapFromUri(uri: Uri): Bitmap? {
            val parcelFileDescriptor: ParcelFileDescriptor? =
                    contentResolver.openFileDescriptor(uri, "r")
            if (parcelFileDescriptor !=null){
                val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
                val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                parcelFileDescriptor.close()
                Log.d("Chloe","My image of bitmap is ${image} ")
                return image
            }else{
                return null
            }
        }

        fun bitMapToString(bitmap: Bitmap): String {
            val byteStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream)
            val b = byteStream.toByteArray()
            Log.d("Chloe","My image of string is ${Base64.encodeToString(b, Base64.DEFAULT)} ")
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        when (requestCode) {
            pickImageFile -> {
                if (resultCode == Activity.RESULT_OK && resultData != null)
                    resultData.data?.let { uri ->
//                        val bitmap = getBitmapFromUri(uri)
//                        selectedImageUri = resultData.data
//                        val bitMapToString = bitMapToString(bitmap)
                        binding.imageView3.setImageURI(uri)
//                        viewModel.updateDataPhoto(adapter.position!!, bitMapToString)
//                        Log.d("test", "${viewModel.commentSend.value}")
//                        adapter.imageView?.setImageURI(uri)
                    }
            }
        }
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