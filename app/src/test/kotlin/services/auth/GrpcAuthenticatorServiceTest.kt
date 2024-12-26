package services.auth

import com.google.protobuf.Empty
import domain.exceptions.Exceptions
import grpc.AuthProto.AuthRequest
import grpc.AuthServiceGrpc
import io.grpc.Status
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GrpcAuthenticatorServiceTest {

    private lateinit var mockStub: AuthServiceGrpc.AuthServiceBlockingStub
    private lateinit var authenticator: GrpcAuthenticatorService

    @Before
    fun setUp() {
        mockStub = mock(AuthServiceGrpc.AuthServiceBlockingStub::class.java)
        authenticator = GrpcAuthenticatorService(mockStub)
    }

    @Test
    fun `register success`() = runBlocking {
        val request = AuthRequest.newBuilder()
            .setLogin("test_login")
            .setPassword("password123")
            .build()

        `when`(mockStub.register(request)).thenReturn(Empty.getDefaultInstance())

        val result = authenticator.register("Test User", "test_login", "password123")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `register failure - user already exists`() = runBlocking {
        val request = AuthRequest.newBuilder()
            .setLogin("test_login")
            .setPassword("password123")
            .build()

        `when`(mockStub.register(request)).thenThrow(Status.ALREADY_EXISTS.asRuntimeException())

        val result = authenticator.register("Test User", "test_login", "password123")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exceptions.UserAlreadyExistsException)
    }

    @Test
    fun `login success`() = runBlocking {
        val request = AuthRequest.newBuilder()
            .setLogin("test_login")
            .setPassword("password123")
            .build()

        `when`(mockStub.login(request)).thenReturn(Empty.getDefaultInstance())

        val result = authenticator.login("test_login", "password123")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `login failure - invalid credentials`() = runBlocking {
        val request = AuthRequest.newBuilder()
            .setLogin("test_login")
            .setPassword("wrong_password")
            .build()

        `when`(mockStub.login(request)).thenThrow(Status.UNAUTHENTICATED.asRuntimeException())

        val result = authenticator.login("test_login", "wrong_password")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exceptions.InvalidCredentialsException)
    }

    @Test
    fun `grpc failure - server unavailable`() = runBlocking {
        `when`(mockStub.login(any())).thenThrow(Status.UNAVAILABLE.asRuntimeException())

        val result = authenticator.login("test_login", "password123")

        result.onFailure { assertTrue(it is Exceptions.UnexpectedError) }
        assertTrue(result.isFailure)
    }
}