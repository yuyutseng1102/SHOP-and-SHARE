package com.chloe.shopshare.order

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentOrderBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.DeliveryMethod


class OrderFragment : Fragment() {

    private val args: OrderFragmentArgs by navArgs()
    private val viewModel by viewModels<OrderViewModel> { getVmFactory(args.cartKey) }
    private lateinit var binding: FragmentOrderBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = OrderProductAdapter(viewModel)
        binding.recyclerProductList.adapter = adapter

        viewModel.products.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()

            }
        })

        fun RadioGroup.addRadio(context: Context?, delivery: List<Int>) {
            for (i in delivery.indices) {
                RadioButton(context).apply {
                    id = View.generateViewId()
                    text = viewModel.displayDelivery(delivery[i])
                    addView(this)
                }
            }
        }

        binding.radioGroupDelivery.apply {
            orientation = RadioGroup.VERTICAL
            addRadio(context, viewModel.shop.value!!.deliveryMethod ?: listOf())
            setOnCheckedChangeListener { _, checkedId ->
                val radioButton: RadioButton = findViewById(checkedId)
                viewModel.selectDelivery(radioButton)
            }
        }

        viewModel.delivery.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Chloe", "delivery selected is ${viewModel.delivery.value}")
                binding.receiverDeliveryField.hint =
                    when (it){
                        DeliveryMethod.SEVEN_ELEVEN.delivery -> DeliveryMethod.SEVEN_ELEVEN.hint
                        DeliveryMethod.FAMILY_MART.delivery -> DeliveryMethod.FAMILY_MART.hint
                        DeliveryMethod.HI_LIFE.delivery -> DeliveryMethod.HI_LIFE.hint
                        DeliveryMethod.OK.delivery -> DeliveryMethod.OK.hint
                        DeliveryMethod.HOME_DELIVERY.delivery -> DeliveryMethod.HOME_DELIVERY.hint
                        DeliveryMethod.BY_HAND.delivery -> DeliveryMethod.BY_HAND.hint
                        else -> DeliveryMethod.SEVEN_ELEVEN.hint
                    }
            }
            if (it == null) {binding.receiverDeliveryField.hint = "取貨地址"}
        })

        viewModel.price.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Chloe", "delivery selected is ${viewModel.price}")
            }
        })



        //案送出按鈕->確認有效填寫->送出訂單
        binding.buttonSendOrder.setOnClickListener {
            viewModel.readyToPost()
            viewModel.isInvalid.observe(viewLifecycleOwner, Observer {
                it.let {
                    if (it == null) {
                        viewModel.sendOrder()
                    }
                    Log.d("Chloe", "order is not ready")
                }
            })
        }

        //成功下單->得到訂單號碼->修改訂單數量
        viewModel.successNumber.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.shop.value?.let {
                    viewModel.increaseOrderSize(it.id)
                }
            }
        })

        //成功修改訂單數量->發送通知
        viewModel.successIncreaseOrder.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    viewModel.editNotify()
                }
            }
        })

        //成功發送通知->跳轉業面
        viewModel.successToNotify.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    viewModel.navigateToSuccess()
                }
            }
        })

        viewModel.navigateToSuccess.observe(viewLifecycleOwner, Observer {
            it?.let {
                val successDialog = Dialog(this.requireContext())
                val view = layoutInflater.inflate(R.layout.dialog_success, null)
                successDialog.setContentView(view)
                successDialog.show()
                findNavController().navigate(NavigationDirections.navigateToHomeFragment())
                viewModel.onSuccessNavigated()
            }
        })




        return binding.root
    }

}