package domain

sealed class Either<L, R> {
    data class Left<L, R>(val error: L) : Either<L, R>()
    data class Right<L, R>(val value: R) : Either<L, R>()

    suspend fun <B>flatMap(f: suspend (R) -> Either<L, B>): Either<L, B> = when (this) {
        is Left -> Left(this.error)
        is Right -> f(this.value)
    }

    suspend fun <B>map(f: suspend (R) -> B): Either<L, B> = when (this) {
        is Left -> Left(this.error)
        is Right -> Right(f(this.value))
    }

    override fun toString(): String = when (this) {
        is Left -> "Left($error)"
        is Right -> "Right($value)"
    }

    companion object {
        fun <T>fromNullable(value: T?, defaultError: Throwable = NullPointerException()): Either<Throwable, T> = when (value) {
            null -> Left(defaultError)
            else -> Right(value)
        }
    }
}