package com.chloe.buytogether.participate

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
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
import com.chloe.buytogether.NavigationDirections
import com.chloe.buytogether.R
import com.chloe.buytogether.bindDisplayDelivery
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.databinding.FragmentParticipateBinding
import com.chloe.buytogether.ext.getVmFactory
import com.chloe.buytogether.network.LoadApiStatus
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class ParticipateFragment : Fragment() {

    private val args: ParticipateFragmentArgs by navArgs()

    private val viewModel by viewModels<ParticipateViewModel> { getVmFactory(args.collectionKey,args.productKey.asList()) }

    private lateinit var binding : FragmentParticipateBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParticipateBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        Log.d("Chloe","args.productKey.asList()=${args.productKey.asList()}")

        val adapter = ParticipateAdapter(viewModel)
        binding.recyclerProductList.adapter = adapter



        viewModel.product.observe(viewLifecycleOwner, Observer {
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

        binding.radioGroupDelivery.apply{
            orientation = RadioGroup.VERTICAL
           addRadio(context,viewModel.collection.value!!.deliveryMethod)
            setOnCheckedChangeListener { _ , checkedId ->
                val radioButton: RadioButton = findViewById(checkedId)
                viewModel.selectDelivery(radioButton)
            }
        }

        viewModel.delivery.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Chloe","delivery selected is ${viewModel.delivery.value}")
            }
        })

        viewModel.price.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Chloe","delivery selected is ${viewModel.price}")
            }
        })

        val successDialog = Dialog(this.requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_success, null)

        binding.buttonParticipate.setOnClickListener {
            viewModel.readyToPost()
            viewModel.status.observe(viewLifecycleOwner, Observer {
                it?.let {
                    if (it == LoadApiStatus.DONE) {
                        viewModel.sendOrder()
                        Log.d("Chloe","order is ready, this order = ${viewModel.order.value}")
                        Log.d("Chloe","order is ready, all order = ${viewModel.collection.value?.order}")

                        successDialog.setContentView(view)
                        successDialog.show()
                        findNavController().navigate(
                                NavigationDirections.navigateToHomeFragment()
                        )

                    }
                    Log.d("Chloe","order is not ready")
                }


            })
        }






        return binding.root
    }

}