package com.chloe.shopshare.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloe.shopshare.data.Filter
import com.chloe.shopshare.data.Shop
import com.chloe.shopshare.network.LoadApiStatus

class SearchViewModel : ViewModel() {

    private val _shop = MutableLiveData<List<Shop>>()
    val shop: LiveData<List<Shop>>
        get() = _shop

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _category = MutableLiveData<Int>()
    val category: LiveData<Int>
        get() = _category

    private val _country = MutableLiveData<Int>()
    val country: LiveData<Int>
        get() = _country

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _navigateToDetail = MutableLiveData<Filter?>()
    val navigateToDetail: LiveData<Filter?>
        get() = _navigateToDetail

    fun navigateToDetail(category: Int?, country: Int?) {
        _navigateToDetail.value = Filter(category ?: 0, country ?: 0)
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }

    fun selectCategory(category: Int?) {
        category?.let { _category.value = it }
    }

    fun selectCountry(country: Int?) {
        country?.let { _country.value = it }
    }
}

