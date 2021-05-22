package com.chloe.buytogether.participate

import androidx.lifecycle.ViewModel
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Product
import com.chloe.buytogether.data.source.Repository
import kotlinx.coroutines.launch

class ParticipateViewModel(
    private val repository: Repository,
    private val collection: Collections,
    private val product: List<Product>
) :ViewModel() {
}
