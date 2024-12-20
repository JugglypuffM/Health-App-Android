package services.training

import com.google.protobuf.Empty
import com.google.protobuf.Timestamp
import domain.exceptions.Exceptions
import domain.training.Training
import grpc.TrainingProto.SaveRequest
import grpc.TrainingProto.TrainingsRequest
import grpc.TrainingProto.TrainingsResponse
import grpc.TrainingServiceGrpc
import grpc.TrainingServiceGrpc.TrainingServiceBlockingStub
import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.StatusRuntimeException
import kotlinx.datetime.LocalDate
import services.async.AsyncCallExecutor

class GrpcTrainingService(private val stub: TrainingServiceBlockingStub) : TrainingService,
    AsyncCallExecutor {

    /**
     * @param address Адреса сервера
     * @param port Порт сервера
     */
    constructor(address: String, port: Int) : this(
        TrainingServiceGrpc.newBlockingStub(
            ManagedChannelBuilder.forAddress(address, port).usePlaintext().build()
        )
    )

    override suspend fun saveTraining(
        login: String,
        password: String,
        training: Training
    ): Result<Unit> {
        return executeCallAsyncWithError<Empty, Unit>(
            {
                val request = SaveRequest.newBuilder()
                    .setLogin(login)
                    .setPassword(password)
                    .setTraining(training.toTrainingProto())
                    .build()

                stub.saveTraining(request)
            },
            {
                Result.success(Unit)
            },
            {
                when (it) {
                    is StatusRuntimeException -> {
                        val status = Status.fromThrowable(it)

                        when (status.code) {
                            Status.Code.INVALID_ARGUMENT -> Result.failure(
                                Exceptions.InvalidArgumentException(
                                    "Unexpected training type"
                                )
                            )

                            Status.Code.UNAUTHENTICATED -> Result.failure(
                                Exceptions.InvalidCredentialsException(
                                    "Invalid credentials passed"
                                )
                            )

                            else -> Result.failure(
                                Exceptions.UnexpectedError("Invalid server response ${status.code}")
                            )
                        }
                    }

                    else -> Result.failure(it)
                }
            }
        )
    }

    override suspend fun getTrainings(
        login: String,
        password: String,
        date: LocalDate
    ): Result<List<Training>> {
        return executeCallAsyncWithError<TrainingsResponse, List<Training>>(
            {
                val request = TrainingsRequest.newBuilder()
                    .setLogin(login)
                    .setPassword(password)
                    .setDate(Timestamp.newBuilder().setSeconds(date.toEpochDays().toLong()))
                    .build()

                stub.getTrainings(request)
            },
            { response ->
                Result.success(response.trainingsList.map {
                    if (it.hasYoga()) Training.Yoga(it.yoga)
                    else if (it.hasJogging()) Training.Jogging(it.jogging)
                    else return@executeCallAsyncWithError Result.failure(
                        Exceptions.UnexpectedError(
                            "Unexpected training type, probably server was updated"
                        )
                    )
                })
            },
            {
                when (it) {
                    is StatusRuntimeException -> {
                        val status = Status.fromThrowable(it)

                        when (status.code) {
                            Status.Code.UNAUTHENTICATED ->
                                Result.failure(
                                    Exceptions.InvalidCredentialsException(
                                        "Invalid credentials passed"
                                    )
                                )

                            else -> Result.failure(
                                Exceptions.UnexpectedError("Unexpected server response ${status.code}")
                            )
                        }
                    }

                    else -> Result.failure(it)
                }
            }
        )
    }

}