package auth

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AuthenticationWithValidationTest {
    private lateinit var authenticatorStub: Authenticator
    private lateinit var authenticationWithValidation: AuthenticationWithValidation

    val successServerResult = Result.success("хороший ответ от сервера")

    @Before
    fun setUp() {
        authenticatorStub = mockk<Authenticator>()
        authenticationWithValidation = AuthenticationWithValidation(authenticatorStub)
    }

    @Test
    fun `valid parameters for registration`() {
        runBlocking {
            coEvery { authenticatorStub.register(any(), any(), any()) } returns successServerResult

            val name = "Гриша"
            val login = "MegaGrish1337"
            val password = "123456"

            val expected = successServerResult
            val actual = authenticationWithValidation.register(name, login, password)

            coVerify { authenticatorStub.register(name, login, password) }
            assert(actual.isSuccess)
            assertEquals(expected, actual)
        }
    }



    @Test
    fun `valid parameters for login`() {
        runBlocking {
            coEvery { authenticatorStub.login(any(), any()) } returns successServerResult

            val login = "MegaGrish1337"
            val password = "123456"

            val expected = successServerResult
            val actual = authenticationWithValidation.login(login, password)

            coVerify { authenticatorStub.login(login, password) }
            assert(actual.isSuccess)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `blank username`() {
        runBlocking {
            val name = "  "
            val login = "MegaGrish1337"
            val password = "123456"

            val actual = authenticationWithValidation.register(name, login, password)

            coVerify (exactly = 0){ authenticatorStub.register(name, login, password) }
            assert(actual.isFailure)
            assert(actual.exceptionOrNull() is Authenticator.InvalidCredentialsException)
        }
    }

    @Test
    fun `blank login`() {
        runBlocking {
            val name = "Гриша"
            val login = "  "
            val password = "123456"

            val actual = authenticationWithValidation.register(name, login, password)

            coVerify (exactly = 0){ authenticatorStub.register(name, login, password) }
            assert(actual.isFailure)
            assert(actual.exceptionOrNull() is Authenticator.InvalidCredentialsException)
        }
    }

    @Test
    fun `short password`() {
        runBlocking {
            val name = "Гриша"
            val login = "MegaGrish1337"
            val password = "12345"

            val actual = authenticationWithValidation.register(name, login, password)

            coVerify(exactly = 0) { authenticatorStub.register(name, login, password) }
            assert(actual.isFailure)
            assert(actual.exceptionOrNull() is Authenticator.InvalidCredentialsException)
        }
    }
}