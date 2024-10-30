package utils

import domain.User
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ValidatorTest {
    private var validatorStub: Validator = Validator()


    @Test
    fun `valid parameters for registration`() {
        runBlocking {
            val name = "Гриша"
            val login = "MegaGrish1337"
            val password = "123456"
            val confirmPassword = "123456"

            val expectedUser = User(name, login, password)
            val expected = Result.success(expectedUser)
            val actual = validatorStub.check(name, login, password, confirmPassword)
            
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `valid parameters for login`() {
        runBlocking {
            val login = "MegaGrish1337"
            val password = "123456"

            val expectedUser = User(null, login, password)
            val expected = Result.success(expectedUser)
            val actual = validatorStub.check(login, password)
            
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `blank username`() {
        runBlocking {
            val name = "   "
            val login = "MegaGrish1337"
            val password = "123456"
            val confirmPassword = "123456"

            val actual = validatorStub.check(name, login, password, confirmPassword)

            assert(actual.isFailure)
            assert(actual.exceptionOrNull() is Validator.InvalidNameException)
        }

    }
    @Test
    fun `blank login`() {
        runBlocking {
            val name = "Гриша"
            val login = "   "
            val password = "123456"
            val confirmPassword = "123456"

            val actual = validatorStub.check(name, login, password, confirmPassword)

            assert(actual.isFailure)
            assert(actual.exceptionOrNull() is Validator.InvalidLoginException)
        }
    }

    @Test
    fun `short password`() {
        runBlocking {
            val name = "Гриша"
            val login = "MegaGrish1337"
            val password = "12345"
            val confirmPassword = "12345"

            val actual = validatorStub.check(name, login, password, confirmPassword)

            assert(actual.isFailure)
            assert(actual.exceptionOrNull() is Validator.InvalidPasswordException)
        }
    }

    @Test
    fun `password not equal confirmPassword`() {
        runBlocking {
            val name = "Гриша"
            val login = "MegaGrish1337"
            val password = "strongpassword"
            val confirmPassword = "srongpassword"

            val actual = validatorStub.check(name, login, password, confirmPassword)

            assert(actual.isFailure)
            assert(actual.exceptionOrNull() is Validator.NotEqualPasswordException)
        }
    }
}