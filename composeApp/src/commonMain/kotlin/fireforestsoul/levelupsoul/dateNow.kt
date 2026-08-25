package fireforestsoul.levelupsoul

import kotlinx.datetime.*
import kotlin.time.Clock

fun dateNow(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.minusDays(days: Int): LocalDate {
    return this.minus(days, DateTimeUnit.DAY)
}

fun LocalDate.plusDays(days: Int): LocalDate {
    return this.plus(days, DateTimeUnit.DAY)
}