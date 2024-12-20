package services.async

import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AsyncCallExecutor {
    suspend fun <T, R> executeCallAsync(call: () -> T, f: (T) -> Result<R>): Result<R> =
        executeCallAsyncWithError(call, f) { Result.failure(it) }

    suspend fun <T, R> executeCallAsyncWithError(
        call: () -> T,
        process: (T) -> Result<R>,
        handler: (Throwable) -> Result<R>
    ): Result<R> =
        withContext(Dispatchers.IO) {
            try {
                val response = call()
                process(response)
            } catch (e: StatusRuntimeException) {
                handler(e)
            }
        }
}