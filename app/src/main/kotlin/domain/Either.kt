package domain

sealed class Either<L, R> {
    data class Left<L, R>(val error: L) : Either<L, R>()
    data class Right<L, R>(val value: R) : Either<L, R>()

    suspend fun <B>flatMap(f: suspend (R) -> Either<L, B>): Either<L, B> = when (this) {
        is Either.Left -> Either.Left(this.error)
        is Either.Right -> f(this.value)
    }

    suspend fun <B>map(f: suspend (R) -> B): Either<L, B> = when (this) {
        is Either.Left -> Either.Left(this.error)
        is Either.Right -> Either.Right(f(this.value))
    }

    override fun toString(): String = when (this) {
        is Either.Left -> "Left($error)"
        is Either.Right -> "Right($value)"
    }
}