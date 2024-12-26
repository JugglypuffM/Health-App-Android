package services.data

import com.google.protobuf.Empty

import domain.exceptions.Exceptions
import domain.UserInfo
import grpc.DataProto.UserData
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
        val request = Empty.getDefaultInstance()

        val expectedData = UserData.newBuilder()
            .setName("Test")
            .setAge(20)
            .setWeight(200)
            .setTotalDistance(1)
            .build()

        `when`(mockStub.getUserData(request)).thenReturn(expectedData)

        val result = dataRequester.getUserData()

        assertTrue(result.isSuccess)
        assertEquals(UserInfo("Test", 20, 200, 1), result.getOrNull())
    }

    @Test
    fun `failed data request auth`() = runBlocking {
        val request = Empty.getDefaultInstance()

        `when`(mockStub.getUserData(request)).thenThrow(Status.UNAUTHENTICATED.asRuntimeException())

        val result = dataRequester.getUserData()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exceptions.InvalidCredentialsException)
    }

    @Test
    fun `server connection failure`() = runBlocking {
        `when`(mockStub.getUserData(any())).thenThrow(StatusRuntimeException(Status.UNAVAILABLE))

        val result = dataRequester.getUserData()

        result.onFailure { assertTrue(it is Exceptions.UnexpectedError) }
        assertTrue(result.isFailure)
    }

    @Test
    fun `successful update request`() = runBlocking {
        val userInfo = UserInfo("name", 1, distance = 3)

        val response = Empty.getDefaultInstance()

        `when`(mockStub.updateUserData(userInfo.toUserData())).thenReturn(response)

        val result = dataRequester.updateUserData(userInfo)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `failed update request`() = runBlocking {
        val userInfo = UserInfo("name", 1, distance = 3)

        `when`(mockStub.updateUserData(userInfo.toUserData())).thenThrow(
            StatusRuntimeException(Status.UNAUTHENTICATED)
        )

        val result = dataRequester.updateUserData(userInfo)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exceptions.InvalidCredentialsException)
    }
}