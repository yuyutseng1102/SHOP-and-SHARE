package com.chloe.shopshare.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.R
import com.chloe.shopshare.data.MyOrderDetailKey
import com.chloe.shopshare.databinding.FragmentProfileBinding
import com.chloe.shopshare.ext.getVmFactory
import com.google.android.material.tabs.TabLayout
import java.util.concurrent.atomic.AtomicBoolean


class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel> { getVmFactory() }
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentProfileBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        val myHostAdapter = ProfileShopAdapter(ProfileShopAdapter.OnClickListener { viewModel.navigateToManage(it) })
        val myOrderAdapter = ProfileOrderAdapter(ProfileOrderAdapter.OnClickListener { viewModel.navigateToOrderDetail(
            MyOrderDetailKey(shopId = it.shop.id, orderId = it.order.id)
        ) })
        val reminderAdapter = ProfileReminderAdapter(viewModel)
        binding.recyclerMyHostImage.adapter = myHostAdapter
        binding.recyclerMyOrderImage.adapter = myOrderAdapter
        binding.recyclerExpiredList.adapter = reminderAdapter



        /** MY HOST **/
        binding.tabLayoutMyHost.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            var alreadyReselected = AtomicBoolean(false)


            override fun onTabSelected(tab: TabLayout.Tab?) {

                Log.d("Profile","tab select on ${tab?.position}")

                when(tab?.position){
                    MyHostShortType.OPENING_SHOP.position-> viewModel.getMyOpeningHost()
                    MyHostShortType.ONGOING_SHOP.position-> viewModel.getMyOngoingHost()
                    else -> viewModel.getMyOpeningHost()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if  (tab?.position == 0 && !alreadyReselected.getAndSet(true)) onTabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("ProfileTag","tab is onTabUnselected = $tab")

            }
        }
        )

        binding.cardMyHost.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToMyHostFragment())
        }

        viewModel.navigateToManage.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToManageFragment(it))
                viewModel.onManageNavigate()
            }
        })

        /** MY ORDER **/

        binding.tabLayoutMyOrder.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            var alreadyReselected = AtomicBoolean(false)

            override fun onTabSelected(tab: TabLayout.Tab?) {

                Log.d("Profile","tab select on ${tab?.position}")

                when(tab?.position){
                    MyOrderShortType.OPENING_ORDER.position-> viewModel.getMyOpeningOrder()
                    MyOrderShortType.ONGOING_ORDER.position-> viewModel.getMyOngoingOrder()
                    else -> viewModel.getMyOpeningOrder()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if  (tab?.position == 0 && !alreadyReselected.getAndSet(true)) onTabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("ProfileTag","tab is onTabUnselected = $tab")

            }
        }
        )

        binding.cardMyOrder.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToMyOrderFragment())
        }

        viewModel.navigateToOrderDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Profile","navigateToOrderDetail = ${it}")
                findNavController().navigate(NavigationDirections.navigateToOrderDetailFragment(it))
                viewModel.onOrderDetailNavigated()
            }
        })

        binding.buttonMyRequest.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToMyRequestFragment())
        }

        binding.buttonMyNotify.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToNotifyFragment())
        }

        binding.buttonMyChat.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToChatFragment())
        }

        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(NavigationDirections.navigateToLoginFragment())
        }



        binding.buttonMyHost.setOnClickListener {
            binding.cardMyHost.visibility = View.VISIBLE
            binding.cardMyOrder.visibility = View.GONE
        }

        binding.buttonMyOrder.setOnClickListener {
            binding.cardMyOrder.visibility = View.VISIBLE
            binding.cardMyHost.visibility = View.GONE
        }

        viewModel.notify.observe(viewLifecycleOwner, Observer{
            it.let {
                binding.viewModel = viewModel
                binding.notifyBadge.isVisible = it.isNotEmpty()
            }
        }
        )



        return binding.root
    }
}