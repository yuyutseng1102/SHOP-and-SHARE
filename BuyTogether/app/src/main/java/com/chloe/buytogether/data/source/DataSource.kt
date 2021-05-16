package com.chloe.buytogether.data.source

import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Result

/**
 *
 * Main entry point for accessing Publisher sources.
 */
interface DataSource {

    suspend fun postCollection(collection: Collections): Result<Boolean>
    suspend fun getCollection(id:String): Result<List<Collections>>
}