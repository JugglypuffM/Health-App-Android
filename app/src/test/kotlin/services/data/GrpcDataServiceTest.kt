package services.data

import domain.exceptions.Exceptions
import domain.UserInfo
import grpc.DataProto.UserData
import grpc.DataProto.UserDataRequest
import grpc.DataProto.UserDataResponse
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
        val request = UserDataRequest.newBuilder()
            .setLogin("test_login")
            .setPassword("password123")
            .build()

        val expectedData = UserData.newBuilder()
            .setName("Test")
            .setAge(20)
            .setWeight(200)
            .setTotalDistance(1)
            .build()

        val response = UserDataResponse.newBuilder()
            .setSuccess(true)
            .setData(expectedData)
            .build()

        `when`(mockStub.getUserData(request)).thenReturn(response)

        val result = dataRequester.getUserData("test_login", "password123")

        assertTrue(result.isSuccess)
        assertEquals(UserInfo("Test", 20, 200, 1), result.getOrNull())
    }

    @Test
    fun `failed data request`() = runBlocking {
        val request = UserDataRequest.newBuilder()
            .setLogin("wrong_login")
            .setPassword("password123")
            .build()

        val expectedData = UserData.newBuilder()
            .setName("")
            .setAge(0)
            .setWeight(0)
            .setTotalDistance(0)
            .build()

        val response = UserDataResponse.newBuilder()
            .setSuccess(false)
            .setData(expectedData)
            .build()

        `when`(mockStub.getUserData(request)).thenReturn(response)

        val result = dataRequester.getUserData("wrong_login", "password123")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exceptions.InvalidCredentialsException)
        assertEquals("Failed to login user with provided credentials", result.exceptionOrNull()?.message)
    }

    @Test
    fun `server connection failure`() = runBlocking {
        `when`(mockStub.getUserData(any())).thenThrow(StatusRuntimeException(Status.UNAVAILABLE))

        val result = dataRequester.getUserData("test_login", "password123")

        result.onFailure { assertTrue(Status.fromThrowable(it).code == Status.Code.UNAVAILABLE) }
        assertTrue(result.isFailure)
    }
}