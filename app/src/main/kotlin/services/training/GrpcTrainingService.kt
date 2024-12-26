package services.training

import com.google.protobuf.Empty
import com.google.protobuf.Timestamp
import domain.exceptions.Exceptions
import domain.training.Training
import grpc.TrainingProto.TrainingsResponse
import grpc.TrainingServiceGrpc
import grpc.TrainingServiceGrpc.TrainingServiceBlockingStub
import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.MetadataUtils
import kotlinx.datetime.LocalDate
import services.grpc.AsyncGrpcService

class GrpcTrainingService(private val stub: TrainingServiceBlockingStub) : TrainingService,
    AsyncGrpcService {

    /**
     * @param address Адреса сервера
     * @param port Порт сервера
     */
    constructor(login: String, password: String, address: String, port: Int) : this(
        TrainingServiceGrpc.newBlockingStub(
            ManagedChannelBuilder.forAddress(address, port).usePlaintext().build()
        ).withInterceptors(
            MetadataUtils.newAttachHeadersInterceptor(
                AsyncGrpcService.createMetadata(login, password)
            )
        )
    )

    override suspend fun saveTraining(training: Training): Result<Unit> {
        return executeCallAsyncWithError<Empty, Unit>(
            {
                stub.saveTraining(training.toTrainingProto())
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

    override suspend fun getTrainings(date: LocalDate): Result<List<Training>> {
        return executeCallAsyncWithError<TrainingsResponse, List<Training>>(
            {
                val timestamp = Timestamp.newBuilder().setSeconds(date.toEpochDays().toLong()).build()

                stub.getTrainings(timestamp)
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