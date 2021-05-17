package com.chloe.buytogether.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.chloe.buytogether.data.source.DataSource
import com.chloe.buytogether.data.source.DefaultRepository
import com.chloe.buytogether.data.source.Repository
import com.chloe.buytogether.data.source.local.LocalDataSource
import com.chloe.buytogether.data.source.remote.RemoteDataSource

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