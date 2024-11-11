package domain.time

import kotlin.time.Duration

/**
 * Функция для получения представления длительности в формате String (чч:мм::cc)
 */
fun Duration.getRepresentation(): String{
    val hours = this.inWholeHours
    val minutes = this.inWholeMinutes % 60
    val seconds = this.inWholeSeconds % 60
    return "%d:%02d:%02d".format(hours, minutes, seconds)
}