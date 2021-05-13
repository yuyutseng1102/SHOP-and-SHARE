package com.chloe.buytogether.gather

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.chloe.buytogether.R
import com.chloe.buytogether.databinding.FragmentGatherBinding
import com.chloe.buytogether.databinding.FragmentHomeBinding
import com.chloe.buytogether.home.HomeAdapter
import com.chloe.buytogether.home.item.HomePageViewModel
import com.google.android.material.tabs.TabLayout


class GatherFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentGatherBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        val viewModel : GatherViewModel = ViewModelProvider(this).get(GatherViewModel::class.java)
        binding.viewModel = viewModel
        binding.buttonAdd.setOnClickListener {
            Log.d("Chloe","The new collection is ${viewModel.collection.value}")
        }
        return binding.root
    }

}