package com.hung.core.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository {
    protected suspend fun <T> launchSafely(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        callBlock: suspend () -> T
    ) = withContext(dispatcher) {
        callBlock()
    }
}