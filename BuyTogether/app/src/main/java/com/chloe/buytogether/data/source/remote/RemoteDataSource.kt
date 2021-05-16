package com.chloe.buytogether.data.source.remote

import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.source.DataSource
import com.chloe.buytogether.data.Result


object RemoteDataSource: DataSource {
    override suspend fun postCollection(collection: Collections): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getCollection(id: String): Result<List<Collections>>{
        TODO("Not yet implemented")
    }
}