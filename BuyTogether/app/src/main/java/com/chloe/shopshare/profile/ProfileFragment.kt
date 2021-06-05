package com.chloe.shopshare.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentProfileBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.login.LoginViewModel


class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentProfileBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.hostBlock.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToMyHostFragment())
        }

        binding.notifyBlock.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToNotifyFragment())
        }

        binding.participateBlock.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToMyOrderFragment())
        }

        binding.requestBlock.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToMyRequestFragment())
        }

        binding.logoutBlock.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(NavigationDirections.navigateToLoginFragment())
        }

        return binding.root
    }

}