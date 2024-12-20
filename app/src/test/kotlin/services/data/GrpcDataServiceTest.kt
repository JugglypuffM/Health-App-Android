package services.data

import domain.BasicUserData
import domain.exceptions.Exceptions
import grpc.DataProto.BasicDataRequest
import grpc.DataProto.BasicDataResponse
import grpc.DataServiceGrpc.DataServiceBlockingStub
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

class GrpcDataServiceTest {
    private lateinit var mockStub: DataServiceBlockingStub
    private lateinit var dataRequester: GrpcDataService

    @Before
    fun setUp() {
        mockStub = mock(DataServiceBlockingStub::class.java)
        dataRequester = spy(GrpcDataService(mockStub))
    }

    @Test
    fun `successful data request`() = runBlocking {
        val request = BasicDataRequest.newBuilder()
            .setLogin("test_login")
            .setPassword("password123")
            .build()

        val response = BasicDataResponse.newBuilder()
            .setSuccess(true)
            .setName("Test")
            .build()

        `when`(mockStub.getBasicUserData(request)).thenReturn(response)

        val result = dataRequester.getBasicUserData("test_login", "password123")

        assertTrue(result.isSuccess)
        assertEquals(BasicUserData("Test"), result.getOrNull())
    }

    @Test
    fun `failed data request`() = runBlocking {
        val request = BasicDataRequest.newBuilder()
            .setLogin("wrong_login")
            .setPassword("password123")
            .build()

        val response = BasicDataResponse.newBuilder()
            .setSuccess(false)
            .setName("")
            .build()

        `when`(mockStub.getBasicUserData(request)).thenReturn(response)

        val result = dataRequester.getBasicUserData("wrong_login", "password123")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exceptions.InvalidCredentialsException)
        assertEquals("Failed to login user with provided credentials", result.exceptionOrNull()?.message)
    }

    @Test
    fun `server connection failure`() = runBlocking {
        `when`(mockStub.getBasicUserData(any())).thenThrow(StatusRuntimeException(Status.UNAVAILABLE))

        val result = dataRequester.getBasicUserData("test_login", "password123")

        result.onFailure { assertTrue(Status.fromThrowable(it).code == Status.Code.UNAVAILABLE) }
        assertTrue(result.isFailure)
    }
}