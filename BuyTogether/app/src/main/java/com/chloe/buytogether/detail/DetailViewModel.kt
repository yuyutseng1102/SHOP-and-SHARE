package com.chloe.buytogether.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.data.source.Repository
import com.chloe.buytogether.gather.OptionAdd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class DetailViewModel(
    private val repository: Repository,
    private val arguments: Collections
):ViewModel() {

    // Detail has product data from arguments
    private val _collection = MutableLiveData<Collections>().apply {
        value = arguments
    }

    val collection: LiveData<Collections>
        get() = _collection



    private val _navigateToOption = MutableLiveData<Collections?>()

    val navigateToOption: LiveData<Collections?>
        get() = _navigateToOption

    val product = MutableLiveData<Product>()



    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    fun navigateToOption(collection: Collections) {
        _navigateToOption.value = collection

    }

    fun onOptionNavigated() {
        _navigateToOption.value = null
    }






}