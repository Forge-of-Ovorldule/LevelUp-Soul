package fireforestsoul.levelupsoul

fun MutableList<Habit>.getPriority(priority: Priority): List<Pair<Habit, Int>> {
    return mapIndexedNotNull { index, habit ->
        if (habit.priority == priority) {
            habit to index
        } else {
            null
        }
    }
}

fun List<Pair<Habit, Int>>.sortSystem(): List<Pair<Habit, Int>> {
    val streak: (Pair<Habit, Int>) -> Int = { pair ->
        habitStreaks(pair.first).firstOrNull() ?: 0
    }

    val allHabits = LocalSaveManager.data.habits
    val maxLevel = allHabits.maxOfOrNull { it.level }
    val minLevel = allHabits.minOfOrNull { it.level }

    val comparator: Comparator<Pair<Habit, Int>> =
        if (
            LocalSaveManager.data.smartSort &&
            maxLevel != null &&
            minLevel != null &&
            maxLevel != minLevel
        ) {
            compareByDescending<Pair<Habit, Int>> { pair ->
                val normalizedLevel =
                    (pair.first.level - minLevel).toFloat() /
                            (maxLevel - minLevel).toFloat()

                normalizedLevel + progress(pair.first)
            }
                .thenByDescending { pair -> streak(pair) }
                .thenBy { pair -> pair.second }
        } else if (LocalSaveManager.data.smartSort) {
            compareByDescending<Pair<Habit, Int>> { pair -> progress(pair.first) }
                .thenByDescending { pair -> streak(pair) }
                .thenBy { pair -> pair.second }
        } else {
            compareByDescending<Pair<Habit, Int>> { pair -> progress(pair.first) }
                .thenByDescending { pair -> pair.first.level }
                .thenByDescending { pair -> streak(pair) }
                .thenBy { pair -> pair.second }
        }

    return sortedWith(comparator)
}