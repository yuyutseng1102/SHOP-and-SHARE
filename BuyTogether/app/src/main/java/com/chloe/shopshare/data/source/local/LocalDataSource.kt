package com.chloe.shopshare.data.source.local

import android.content.Context
import com.chloe.shopshare.data.Collections
import com.chloe.shopshare.data.source.DataSource
import com.chloe.shopshare.data.Result


class LocalDataSource(val context: Context): DataSource {
    override suspend fun postCollection(collection: Collections): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getCollection(id: String): Result<List<Collections>> {
        TODO("Not yet implemented")
    }
}