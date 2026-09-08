package fireforestsoul.levelupsoul

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

fun Month.toThreeString(): String = when (this) {
    Month.APRIL -> TranslatedStrings.Table.APR
    Month.AUGUST -> TranslatedStrings.Table.AUG
    Month.DECEMBER -> TranslatedStrings.Table.DEC
    Month.FEBRUARY -> TranslatedStrings.Table.FEB
    Month.JANUARY -> TranslatedStrings.Table.JAN
    Month.JULY -> TranslatedStrings.Table.JUL
    Month.JUNE -> TranslatedStrings.Table.JUN
    Month.MARCH -> TranslatedStrings.Table.MAR
    Month.MAY -> TranslatedStrings.Table.MAY
    Month.NOVEMBER -> TranslatedStrings.Table.NOV
    Month.OCTOBER -> TranslatedStrings.Table.OCT
    Month.SEPTEMBER -> TranslatedStrings.Table.SEP
}

fun DayOfWeek.toThreeString(): String = when (this) {
    DayOfWeek.MONDAY -> TranslatedStrings.Table.MON
    DayOfWeek.FRIDAY -> TranslatedStrings.Table.FRI
    DayOfWeek.SATURDAY -> TranslatedStrings.Table.SAT
    DayOfWeek.SUNDAY -> TranslatedStrings.Table.SUN
    DayOfWeek.THURSDAY -> TranslatedStrings.Table.THU
    DayOfWeek.TUESDAY -> TranslatedStrings.Table.TUE
    DayOfWeek.WEDNESDAY -> TranslatedStrings.Table.WED
}

fun Priority.toTranslatedString(): String = when (this) {
    Priority.NO_PRIORITY -> TranslatedStrings.Table.NO_PRIORITY
    Priority.LOW_PRIORITY -> TranslatedStrings.Table.LOW_PRIORITY
    Priority.MEDIUM_PRIORITY -> TranslatedStrings.Table.MEDIUM_PRIORITY
    Priority.HIGH_PRIORITY -> TranslatedStrings.Table.HIGH_PRIORITY
}