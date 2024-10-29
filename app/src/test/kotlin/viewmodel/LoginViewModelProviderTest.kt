package viewmodel

import auth.Authenticator
import domain.Either
import domain.User
import domain.Validate
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import utils.LocalStorage

class LoginViewModelProviderTest {
    private lateinit var authenticatorStub: Authenticator
    private lateinit var storageStub: LocalStorage
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        storageStub = mockk<LocalStorage>()
        authenticatorStub = mockk<Authenticator>()
        loginViewModel = LoginViewModel(storageStub, authenticatorStub)
    }


    @Test
    fun `valid parameters for registration`() {
        runBlocking {
            coEvery { authenticatorStub.register(any(), any(), any()) } answers {
                Either.Right(User(firstArg(), secondArg(), thirdArg()))
            }

            val name = "Гриша"
            val login = "MegaGrish1337"
            val password = "123456"
            val confirmPassword = "123456"

            val expectedUser = User(name, login, password)
            val expected = Either.Right<Throwable, User>(expectedUser)
            val actual = loginViewModel.validate(name, login, password, confirmPassword)

            assert(actual is Either.Right)
            assertEquals(expected, actual)
        }
    }



    @Test
    fun `valid parameters for login`() {
        runBlocking {
            coEvery { authenticatorStub.login(any(), any()) } answers {
                Either.Right(User(null, firstArg(), secondArg()))
            }

            val login = "MegaGrish1337"
            val password = "123456"

            val expectedUser = User(null, login, password)
            val expected = Either.Right<Throwable, User>(expectedUser)
            val actual = loginViewModel.validate(login, password)

            assert(actual is Either.Right)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `blank username`() {
        runBlocking {
            coEvery { authenticatorStub.register(any(), any(), any()) } answers {
                Either.Right(User(firstArg(), secondArg(), thirdArg()))
            }

            val name = "   "
            val login = "MegaGrish1337"
            val password = "123456"
            val confirmPassword = "123456"

            val expected = Either.Left<Throwable, User>(Validate.InvalidNameException("User name is empty"))
            val actual = loginViewModel.validate(name, login, password, confirmPassword)

            assert(actual is Either.Left)
            assertEquals(expected.error::class, (actual as Either.Left).error::class)
            assertEquals(expected.error.message, actual.error.message)
        }
    }

    @Test
    fun `blank login`() {
        runBlocking {
            coEvery { authenticatorStub.register(any(), any(), any()) } answers {
                Either.Right(User(firstArg(), secondArg(), thirdArg()))
            }

            val name = "Гриша"
            val login = "   "
            val password = "123456"
            val confirmPassword = "123456"

            val expected = Either.Left<Throwable, User>(Validate.InvalidLoginException("Login is empty"))
            val actual = loginViewModel.validate(name, login, password, confirmPassword)

            assert(actual is Either.Left)
            assertEquals(expected.error::class, (actual as Either.Left).error::class)
            assertEquals(expected.error.message, actual.error.message)
        }
    }

    @Test
    fun `short password`() {
        runBlocking {
            coEvery { authenticatorStub.register(any(), any(), any()) } answers {
                Either.Right(User(firstArg(), secondArg(), thirdArg()))
            }

            val name = "Гриша"
            val login = "MegaGrish1337"
            val password = "12345"
            val confirmPassword = "12345"

            val expected = Either.Left<Throwable, User>(Validate.InvalidPasswordException("The password must be at least 6 characters long"))
            val actual = loginViewModel.validate(name, login, password, confirmPassword)

            assert(actual is Either.Left)
            assertEquals(expected.error::class, (actual as Either.Left).error::class)
            assertEquals(expected.error.message, actual.error.message)
        }
    }

    @Test
    fun `password not equal confirmPassword`() {
        runBlocking {
            coEvery { authenticatorStub.register(any(), any(), any()) } answers {
                Either.Right(User(firstArg(), secondArg(), thirdArg()))
            }

            val name = "Гриша"
            val login = "MegaGrish1337"
            val password = "strongpassword"
            val confirmPassword = "srongpassword"

            val expected = Either.Left<Throwable, User>(Validate.NotEqualPasswordException("Passwords do not match"))
            val actual = loginViewModel.validate(name, login, password, confirmPassword)

            assert(actual is Either.Left)
            assertEquals(expected.error::class, (actual as Either.Left).error::class)
            assertEquals(expected.error.message, actual.error.message)
        }
    }

    @Test
    fun `save user before register`() {
        runBlocking {
            coEvery { authenticatorStub.register(any(), any(), any()) } answers {
                Either.Right(User(firstArg(), secondArg(), thirdArg()))
            }

            coEvery { storageStub.saveUser(any()) } just Runs

            val name = "Гриша"
            val login = "MegaGrish1337"
            val password = "123456"
            val confirmPassword = "123456"

            val expectedUser = User(name, login, password)
            val expected = Either.Right<Throwable, User>(expectedUser)
            val actual = loginViewModel.register(name, login, password)

            coVerify(exactly = 1){ authenticatorStub.register(name, login, password) }
            coVerify(exactly = 1){ storageStub.saveUser(expectedUser) }

            assert(actual is Either.Right)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `correct login`() {
        runBlocking {
            coEvery { authenticatorStub.login(any(), any()) } answers {
                Either.Right(User(null, firstArg(), secondArg()))
            }

            val login = "MegaGrish1337"
            val password = "123456"

            val expectedUser = User(null, login, password)
            val expected = Either.Right<Throwable, User>(expectedUser)
            val actual = loginViewModel.login(login, password)

            coVerify(exactly = 1){ authenticatorStub.login(login, password) }

            assert(actual is Either.Right)
            assertEquals(expected, actual)
        }
    }
}