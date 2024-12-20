package services.auth

import domain.exceptions.Exceptions
import grpc.AuthProto.AuthResponse
import grpc.AuthProto.LoginRequest
import grpc.AuthProto.RegisterRequest
import grpc.AuthServiceGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.`when`

class GrpcAuthenticatorServiceTest {

    private lateinit var mockStub: AuthServiceGrpc.AuthServiceBlockingStub
    private lateinit var authenticator: GrpcAuthenticatorService

    @Before
    fun setUp() {
        mockStub = mock(AuthServiceGrpc.AuthServiceBlockingStub::class.java)
        authenticator = spy(GrpcAuthenticatorService(mockStub))
    }

    @Test
    fun `register success`() = runBlocking {
        val request = RegisterRequest.newBuilder()
            .setLogin("test_login")
            .setPassword("password123")
            .build()

        val response = AuthResponse.newBuilder()
            .setResultCode(0)
            .setMessage("Registration successful")
            .build()

        `when`(mockStub.register(request)).thenReturn(response)

        val result = authenticator.register("Test User", "test_login", "password123")

        assertTrue(result.isSuccess)
        assertEquals("Registration successful", result.getOrNull())
    }

    @Test
    fun `register failure - user already exists`() = runBlocking {
        val request = RegisterRequest.newBuilder()
            .setLogin("test_login")
            .setPassword("password123")
            .build()

        val response = AuthResponse.newBuilder()
            .setResultCode(1)
            .setMessage("User already exists.")
            .build()

        `when`(mockStub.register(request)).thenReturn(response)

        val result = authenticator.register("Test User", "test_login", "password123")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exceptions.UserAlreadyExistsException)
        assertEquals("User already exists.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `login success`() = runBlocking {
        val request = LoginRequest.newBuilder()
            .setLogin("test_login")
            .setPassword("password123")
            .build()

        val response = AuthResponse.newBuilder()
            .setResultCode(0)
            .setMessage("Login successful")
            .build()

        `when`(mockStub.login(request)).thenReturn(response)

        val result = authenticator.login("test_login", "password123")

        assertTrue(result.isSuccess)
        assertEquals("Login successful", result.getOrNull())
    }

    @Test
    fun `login failure - invalid credentials`() = runBlocking {
        val request = LoginRequest.newBuilder()
            .setLogin("test_login")
            .setPassword("wrong_password")
            .build()

        val response = AuthResponse.newBuilder()
            .setResultCode(2)
            .setMessage("Invalid login or password.")
            .build()

        `when`(mockStub.login(request)).thenReturn(response)

        val result = authenticator.login("test_login", "wrong_password")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exceptions.InvalidCredentialsException)
        assertEquals("Invalid login or password.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `grpc failure - server unavailable`() = runBlocking {
        `when`(mockStub.login(any())).thenThrow(StatusRuntimeException(Status.UNAVAILABLE))

        val result = authenticator.login("test_login", "password123")

        result.onFailure { assertTrue(Status.fromThrowable(it).code == Status.Code.UNAVAILABLE) }
        assertTrue(result.isFailure)
    }
}