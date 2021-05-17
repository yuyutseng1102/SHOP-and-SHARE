package com.chloe.buytogether.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chloe.buytogether.NavigationDirections
import com.chloe.buytogether.R
import com.chloe.buytogether.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentProfileBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.gatherButton.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToCollectionFragment())
        }

        return binding.root
    }

}