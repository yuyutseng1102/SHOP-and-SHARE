package com.chloe.shopshare.data.source

import com.chloe.shopshare.data.Collections
import com.chloe.shopshare.data.Result

/**
 *
 * Interface to the Publisher layers.
 */

interface Repository {
    suspend fun postCollection(collection: Collections): Result<Boolean>
    suspend fun getCollection(id:String): Result<List<Collections>>
}