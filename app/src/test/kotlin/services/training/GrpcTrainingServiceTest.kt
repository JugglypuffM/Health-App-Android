package services.training

import com.google.protobuf.Empty
import com.google.protobuf.Timestamp
import domain.exceptions.Exceptions
import domain.training.Training
import grpc.TrainingProto
import grpc.TrainingServiceGrpc
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import org.junit.Before
import kotlin.time.Duration.Companion.seconds
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GrpcTrainingServiceTest {

    private lateinit var stub: TrainingServiceGrpc.TrainingServiceBlockingStub
    private lateinit var service: GrpcTrainingService

    @Before
    fun setUp(){
        stub = mock(TrainingServiceGrpc.TrainingServiceBlockingStub::class.java)
        service = GrpcTrainingService(stub)
    }

    @Test
    fun `saveTraining should return success when server responds successfully`() = runBlocking {
        val training = Training.Yoga(LocalDate(12,12,12), 30.seconds)

        `when`(stub.saveTraining(training.toTrainingProto())).thenReturn(Empty.getDefaultInstance())

        val result = service.saveTraining(training)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `saveTraining should return InvalidArgumentException for INVALID_ARGUMENT error`() = runBlocking {
        val training = Training.Yoga(LocalDate(12,12,12), 30.seconds)

        `when`(stub.saveTraining(training.toTrainingProto())).thenThrow(
            io.grpc.StatusRuntimeException(io.grpc.Status.INVALID_ARGUMENT)
        )

        val result = service.saveTraining(training)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exceptions.InvalidArgumentException)
    }

    @Test
    fun `getTrainings should return list of trainings when server responds successfully`() = runBlocking {
        val date = LocalDate(12,12,12)

        val response = TrainingProto.TrainingsResponse.newBuilder()
            .addTrainings(
                TrainingProto.Training.newBuilder().setYoga(
                    TrainingProto.Yoga.newBuilder()
                        .setDate(Timestamp.newBuilder().setSeconds(date.toEpochDays().toLong()))
                        .setDuration(com.google.protobuf.Duration.newBuilder().setSeconds(1800))
                )
            ).build()

        val request = Timestamp.newBuilder().setSeconds(date.toEpochDays().toLong()).build()

        `when`(stub.getTrainings(request)).thenReturn(response)

        val result = service.getTrainings(date)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertTrue(result.getOrNull()?.get(0) is Training.Yoga)
    }

    @Test
    fun `getTrainings should return InvalidCredentialsException for UNAUTHENTICATED error`() = runBlocking {
        val date = LocalDate(12,12,12)

        val request = Timestamp.newBuilder().setSeconds(date.toEpochDays().toLong()).build()

        `when`(stub.getTrainings(request)).thenThrow(
            io.grpc.StatusRuntimeException(io.grpc.Status.UNAUTHENTICATED)
        )

        val result = service.getTrainings(date)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exceptions.InvalidCredentialsException)
    }
}
