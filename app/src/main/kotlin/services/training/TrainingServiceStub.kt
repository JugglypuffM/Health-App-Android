package services.training

import domain.exceptions.Exceptions
import domain.training.Training
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class TrainingServiceStub : TrainingService {
    override suspend fun saveTraining(training: Training): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getTrainings(date: LocalDate): Result<List<Training>> {
        return Result.success(
            listOf(
                Training.Yoga(date, 3.minutes),
                Training.Jogging(date, 3.minutes, 9.0)
            )
        )
    }
}