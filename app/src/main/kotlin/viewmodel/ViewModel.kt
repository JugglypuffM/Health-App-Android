package viewmodel

import auth.Authenticator
import auth.GrpcAuthenticatorStub
import domain.Either
import domain.User
import domain.Validate
import kotlinx.coroutines.delay
import utils.LocalStorage

object ViewModel {
    val authenticator = GrpcAuthenticatorStub()
    val storage = LocalStorage()

    //TODO Реализовать
    suspend fun login(name: String, password: String): Either<Throwable, User> {
        delay(1000);
        if(name == "john_doe" && password == "password123")
            return Either.Right(User("John Doe", "john_doe", "password123"))
        else
            return Either.Left(Authenticator.InvalidCredentialsException(""))
    }

    suspend fun register(name: String, login:String, password: String): Either<Throwable, User> {
        delay(1000);
        if(name == "john_doe" && password == "password123")
            return Either.Right(User("John Doe", "john_doe", "password123"))
        else
            return Either.Left(Authenticator.InvalidCredentialsException(""))
    }

    suspend fun loadUser(): Either<Throwable, User> {
        delay(1000);
        return Either.Right(User("John Doe", "john_doe", "password123"))
    }

    fun validate(login: String, password: String): Either<Throwable, User> {
        return Either.Left(Validate.InvalidNameException(""))
    }

    fun validate(name: String, login: String, password: String, confirmPassword: String): Either<Throwable, User> {
        return Either.Right(User(name, login, password))
    }

    suspend fun dropUser(){

    }
}