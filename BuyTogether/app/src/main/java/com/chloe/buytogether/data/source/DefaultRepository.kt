package com.chloe.buytogether.data.source

import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.Result


class DefaultRepository (private val remoteDataSource: DataSource,
                         private val localDataSource: DataSource
) : Repository {
    override suspend fun postCollection(collection: Collections): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getCollection(id: String): Result<List<Collections>> {
        TODO("Not yet implemented")
    }


}