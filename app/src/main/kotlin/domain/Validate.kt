package domain

object Validate {
    class InvalidNameException(message: String) : Exception(message)
    class InvalidLoginException(message: String) : Exception(message)
    class InvalidPasswordException(message: String) : Exception(message)
    class NotEqualPasswordException(message: String) : Exception(message)
}