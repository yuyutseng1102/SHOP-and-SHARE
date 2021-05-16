package com.chloe.buytogether.data.source

import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Result

/**
 *
 * Interface to the Publisher layers.
 */

interface Repository {
    suspend fun postCollection(collection: Collections): Result<Boolean>
    suspend fun getCollection(id:String): Result<List<Collections>>
}