package async

import auth.Authenticator.ServerConnectionException
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AsyncCallExecutor {
    suspend fun <T, R>executeCallAsync(f: (T) -> Result<R>, call: () -> T): Result<R> =
        withContext(Dispatchers.IO) {
            try {
                val response = call()
                f(response)
            } catch (e: StatusRuntimeException) {
                Result.failure(
                    ServerConnectionException(
                        "Failed to connect to the server: server is unavailable"
                    )
                )
            }
        }
}