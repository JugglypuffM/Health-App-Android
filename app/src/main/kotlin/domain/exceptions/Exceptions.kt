package domain.exceptions

object Exceptions {
    class ServerConnectionException(message: String) : Exception(message)
    class InvalidCredentialsException(message: String) : Exception(message)
    class InvalidArgumentException(message: String) : Exception(message)
    class UserAlreadyExistsException(message: String) : Exception(message)
    class UnexpectedError(message: String) : Exception(message)
}