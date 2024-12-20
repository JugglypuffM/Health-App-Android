package domain.training

import com.google.protobuf.Timestamp
import grpc.TrainingProto
import kotlinx.datetime.LocalDate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Информация о тренировке
 * Специфична для каждого типа тренировки
 */
sealed class Training(
    val date: LocalDate,
    val duration: Duration
) {
    /**
     * Информация о йоге
     */
    class Yoga(date: LocalDate, duration: Duration) : Training(
        date = date,
        duration = duration
    ) {
        constructor(yoga: TrainingProto.Yoga) : this(
            LocalDate.fromEpochDays(yoga.date.seconds.toInt()),
            yoga.duration.seconds.seconds
        )

        override fun toTrainingProto(): TrainingProto.Training {
            return TrainingProto.Training.newBuilder()
                .setYoga(
                    TrainingProto.Yoga.newBuilder()
                        .setDate(
                            Timestamp.newBuilder()
                                .setSeconds(date.toEpochDays().toLong())
                        )
                        .setDuration(
                            com.google.protobuf.Duration.newBuilder()
                                .setSeconds(duration.inWholeSeconds)
                        )
                        .build()
                ).build()
        }
    }

    /**
     * Информация о беге
     */
    class Jogging(date: LocalDate, duration: Duration, val distance: Double) : Training(
        date = date,
        duration = duration
    ) {
        constructor(jogging: TrainingProto.Jogging) : this(
            LocalDate.fromEpochDays(jogging.date.seconds.toInt()),
            jogging.duration.seconds.seconds,
            jogging.distance
        )

        override fun toTrainingProto(): TrainingProto.Training {
            return TrainingProto.Training.newBuilder()
                .setJogging(
                    TrainingProto.Jogging.newBuilder()
                        .setDate(
                            Timestamp.newBuilder()
                                .setSeconds(date.toEpochDays().toLong())
                        )
                        .setDuration(
                            com.google.protobuf.Duration.newBuilder()
                                .setSeconds(duration.inWholeSeconds)
                        )
                        .setDistance(distance)
                        .build()
                ).build()
        }
    }

    abstract fun toTrainingProto(): TrainingProto.Training
}