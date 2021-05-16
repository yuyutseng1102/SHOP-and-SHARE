package com.chloe.buytogether.data.source.local

import android.content.Context
import com.chloe.buytogether.data.Collections
import com.chloe.buytogether.data.source.DataSource
import com.chloe.buytogether.data.Result


class LocalDataSource(val context: Context): DataSource {
    override suspend fun postCollection(collection: Collections): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getCollection(id: String): Result<List<Collections>> {
        TODO("Not yet implemented")
    }
}