package fireforestsoul.levelupsoul

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
class LocalData {
    var habits: MutableList<Habit> = mutableListOf(
        Habit()
    )
    var soulColorType: TypeOfColorHabit = TypeOfColorHabit.ADAPTIVE

    @Contextual
    var soulColor: Color = Color(200, 200, 200)
    var soulName: String = ts_Mr_Soul_Forest
    var soulLevel: Int = 0
    var soulLastLevelChangeDate: LocalDate =
        kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var language: Languages = Languages.EN
    var withExponent: Boolean = false
    var backAppStatus: ScreenManager = ScreenManager.TABLE
    var listPointsOfHabitStatistic: MutableMap<HabitStatisticsStatus, Float> = mutableMapOf(
        HabitStatisticsStatus.GOAL to 0f,
        HabitStatisticsStatus.PROGRESS to 0f,
        HabitStatisticsStatus.LEVEL to 0f,
        HabitStatisticsStatus.PROGRESS_GRAPH to 0f,
        HabitStatisticsStatus.BAR_CHART to 0f,
        HabitStatisticsStatus.CALENDAR to 0f,
        HabitStatisticsStatus.DISTRIBUTION_BY_DAY_OF_THE_WEEK to 0f,
        HabitStatisticsStatus.STREAKS to 0f
    )
    var sortHabitStatisticsSectionsByFrequencyOfUse: Boolean = false
    var smartSort: Boolean = false
}
