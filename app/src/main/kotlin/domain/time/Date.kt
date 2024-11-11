package domain.time

import kotlinx.datetime.LocalDate

/**
 * Функция для получения представления даты в формате String (дд:ммм)
 */
fun LocalDate.getRepresentation(): String {
    val day = this.dayOfMonth
    val month = this.month
    val monthName = month.name.take(3)
    return "%02d:%s".format(day, monthName)
}
