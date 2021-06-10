package com.chloe.buytogether.detail.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.chloe.buytogether.databinding.FragmentDetailDescriptionBinding
import com.chloe.buytogether.ext.getVmFactory


class DetailDescriptionFragment(private val description: String) : Fragment() {


    private val viewModel by viewModels<DetailDescriptionViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailDescriptionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.description = description
        return binding.root
    }


}