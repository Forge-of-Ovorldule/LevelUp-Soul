package fireforestsoul.levelupsoul

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun dateNow(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
