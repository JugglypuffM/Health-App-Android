package domain.training

import com.google.protobuf.Timestamp
import grpc.TrainingProto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
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
                                .setSeconds(date.atStartOfDayIn(TimeZone.currentSystemDefault()).epochSeconds)
                                .build()
                        )
                        .setDuration(
                            com.google.protobuf.Duration.newBuilder()
                                .setSeconds(duration.inWholeSeconds)
                                .build()
                        )
                        .build()
                ).build()
        }

        override fun toString(): String {
            return "Yoga($date, $duration)"
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
            jogging.distance.toDouble()
        )

        override fun toTrainingProto(): TrainingProto.Training {
            return TrainingProto.Training.newBuilder()
                .setJogging(
                    TrainingProto.Jogging.newBuilder()
                        .setDate(
                            Timestamp.newBuilder()
                                .setSeconds(date.atStartOfDayIn(TimeZone.currentSystemDefault()).epochSeconds)
                                .build()
                        )
                        .setDuration(
                            com.google.protobuf.Duration.newBuilder()
                                .setSeconds(duration.inWholeSeconds)
                                .build()
                        )
                        .setDistance(distance.toInt())
                        .build()
                ).build()
        }

        override fun toString(): String {
            return "Jogging($date, $duration, $distance)"
        }
    }

    /**
     * Информация о планке
     */
    class Plank(date: LocalDate, duration: Duration) : Training(
        date = date,
        duration = duration
    ) {
        constructor(plank: TrainingProto.Plank) : this(
            LocalDate.fromEpochDays(plank.date.seconds.toInt()),
            plank.duration.seconds.seconds
        )

        override fun toTrainingProto(): TrainingProto.Training {
            return TrainingProto.Training.newBuilder()
                .setPlank(
                    TrainingProto.Plank.newBuilder()
                        .setDate(
                            Timestamp.newBuilder()
                                .setSeconds(date.atStartOfDayIn(TimeZone.currentSystemDefault()).epochSeconds)
                                .build()
                        )
                        .setDuration(
                            com.google.protobuf.Duration.newBuilder()
                                .setSeconds(duration.inWholeSeconds)
                                .build()
                        )
                        .build()
                ).build()
        }

        override fun toString(): String {
            return "Plank($date, $duration)"
        }
    }

    abstract fun toTrainingProto(): TrainingProto.Training
}