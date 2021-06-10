package com.chloe.shopshare.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.data.Order
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner



//        val orderList = MutableLiveData<List<Order>>()
//        val shopList = MutableLiveData<List<Shop>>()
//        fun getShop(){
//
//            val shop = FirebaseFirestore.getInstance().collection("shop")
////            val document = shop.document("jsKOIUxVx9f6nvPArJn3")
//            shop.get().addOnSuccessListener { documents ->
//                var shopClass = documents.toObjects<Shop>()
//                Log.d("Chloe", "shop = $shopClass")
//                for (document in documents) {
//                    Log.d("Chloe", "${document.id} => ${document.data}")
//
//                    val messageRef = shop.document(document.id)
//                        .collection("order").get().addOnSuccessListener { subDocuments ->
//                            val orderClass = subDocuments.toObjects<Order>()
//                            for (i in shopClass) {
//                                if (i.id == document.id) {
//                                    i.order = orderClass
//                                    Log.d("Chloe", "orderClass = $orderClass")
//                                    Log.d("Chloe", "shopClass = $shopClass")
//                                    Log.d("Chloe", "i.id = ${i.order}")
//                                    shopClass = shopClass
//                                    shopList.value = shopClass
//
//                                }
//
//                            }
//
//                            for (subDocument in subDocuments) {
//                                Log.d("Chloe", "${subDocument.id} => ${subDocument.data}")
//                            }
//
//                            }
//                }
//
//
//            }
//                .addOnFailureListener { exception ->
//                    Log.w("Chloe", "Error getting documents: ", exception)
//                }
//        }
//
//        getShop()
//        shopList.observe(viewLifecycleOwner, Observer {
//            Log.d("Chloe", "shopList = ${shopList.value}")
//        })

        binding.viewpagerHome.let{
            binding.tabsHome.setupWithViewPager(it)
            it.adapter = HomeAdapter(childFragmentManager)
            it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabsHome))
        }

        binding.floatingCollect.setOnClickListener {
            findNavController().navigate(NavigationDirections.navigateToHostFragment())
        }



        return binding.root
    }

}