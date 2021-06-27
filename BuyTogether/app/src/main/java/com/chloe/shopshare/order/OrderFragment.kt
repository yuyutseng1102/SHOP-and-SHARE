package com.chloe.shopshare.order

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.databinding.FragmentOrderBinding
import com.chloe.shopshare.ext.getVmFactory


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
        binding.recyclerProducts.adapter = adapter

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
            viewModel.shop.value?.let { addRadio(context, it.deliveryMethod) }
            setOnCheckedChangeListener { _, checkedId ->
                val radioButton: RadioButton = findViewById(checkedId)
                viewModel.selectDelivery(radioButton)
            }
        }

        viewModel.delivery.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.receiverDeliveryField.hint = viewModel.displayHint(it)
            }
        })

        binding.buttonSendOrder.setOnClickListener {
            viewModel.readyToPost()
            viewModel.isInvalid.observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    viewModel.sendOrder()
                }
            })
        }

        viewModel.successNumber.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.shop.value?.let { shop -> viewModel.increaseOrderSize(shop.id) }
            }
        })

        viewModel.successIncreaseOrder.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.editNotify()
            }
        })

        viewModel.successToNotify.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.navigateToSuccess()
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