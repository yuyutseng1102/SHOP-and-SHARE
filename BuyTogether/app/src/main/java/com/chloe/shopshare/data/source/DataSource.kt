package com.chloe.shopshare.data.source

import com.chloe.shopshare.data.Collections
import com.chloe.shopshare.data.Result

/**
 *
 * Main entry point for accessing Publisher sources.
 */
interface DataSource {

    suspend fun postCollection(collection: Collections): Result<Boolean>
    suspend fun getCollection(id:String): Result<List<Collections>>
}