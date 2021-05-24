package com.chloe.shopshare.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentProfileBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.hostButton.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCollectionFragment())
        }

        binding.notifyButton.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToNotifyFragment())
        }

        return binding.root
    }

}