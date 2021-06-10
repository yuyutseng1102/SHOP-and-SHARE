package com.chloe.shopshare

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.util.CurrentFragmentType
import com.firebase.ui.auth.AuthUI

class MainViewModel(private val repository: Repository) : ViewModel() {

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()



}