package services.grpc

import io.grpc.StatusRuntimeException
import io.grpc.Metadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AsyncGrpcService {
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

    companion object{
        fun createMetadata(login: String, password: String): Metadata {
            val metadata = Metadata()
            val usernameKey = Metadata.Key.of("login-bin", Metadata.BINARY_BYTE_MARSHALLER)
            val passwordKey = Metadata.Key.of("password-bin", Metadata.BINARY_BYTE_MARSHALLER)
            metadata.put(usernameKey, login.toByteArray())
            metadata.put(passwordKey, password.toByteArray())
            return metadata
        }
    }
}