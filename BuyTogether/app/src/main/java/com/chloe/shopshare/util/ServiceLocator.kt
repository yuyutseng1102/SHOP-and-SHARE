package com.chloe.shopshare.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.chloe.shopshare.data.source.DataSource
import com.chloe.shopshare.data.source.DefaultRepository
import com.chloe.shopshare.data.source.Repository
import com.chloe.shopshare.data.source.local.LocalDataSource
import com.chloe.shopshare.data.source.remote.RemoteDataSource

/**
 *
 * A Service Locator for the [Repository].
 */
object ServiceLocator {

    @Volatile
    var repository: Repository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): Repository {
        synchronized(this) {
            return repository
                    ?: repository
                    ?: createPublisherRepository(context)
        }
    }

    private fun createPublisherRepository(context: Context): Repository {
        return DefaultRepository(
                RemoteDataSource,
                createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): DataSource {
        return LocalDataSource(context)
    }
}