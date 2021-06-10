package com.chloe.shopshare.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SearchViewModel(private val repository: Repository): ViewModel() {

    private val _shop = MutableLiveData<List<Shop>>()
    val shop: LiveData<List<Shop>>
        get() = _shop

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status


    private val _category = MutableLiveData<Int>()
    val category: LiveData<Int>
        get() = _category

    private val _country = MutableLiveData<Int>()
    val country: LiveData<Int>
        get() = _country

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateToDetail = MutableLiveData<Filter>()

    val navigateToDetail: LiveData<Filter>
        get() = _navigateToDetail




    fun navigateToDetail(category: Int?, country: Int?){
        _navigateToDetail.value = Filter(category?:0, country?:0)
    }

    fun onDetailNavigated(){
        _navigateToDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun selectCategory(category: Int?){
        _category.value = category!!
    }

    fun selectCountry(country: Int?){
        _country.value = country!!
    }

}

data class Filter(
    val category : Int? = null,
    val country: Int? = null
)