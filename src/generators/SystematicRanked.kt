package generators

import calculations.PathCalculator
import calculations.PointStrategy
import model.Constellation
import model.ConstellationOutput

private const val TWOS_MAX_FROM_FIRST = 3
private const val TWOS_MAX_DISTANCE_FROM_STARTER = 4

class SystematicRanked: GeneratorStrategy {

    private val pathCalculator = PathCalculator()
    private val usageCount = mutableMapOf<Constellation, Int>()
    private val usedVectors = mutableMapOf<Constellation, MutableSet<Constellation>>()
    private val outputMyths = mutableSetOf<Set<Constellation>>()

    override fun generateMyths(
        constellationOutput: ConstellationOutput,
        pointStrategy: PointStrategy
    ): Set<Set<Constellation>> {

        // Add the allow list
        constellationOutput.allowList.forEach { addMyth(it) }

        // Add blocked list to used vectors
        constellationOutput.blockedList.forEach { blocked ->
            blocked.forEach { first ->
                blocked.subtract(setOf(first)).forEach { second ->
                    usedVectors.getOrPut(first) { mutableSetOf() }.add(second)
                }
            }
        }

        val constellations = constellationOutput.allConstellations
        val starters = constellationOutput.starters

        // Make two size myths until we run out of possibilities
        var mythCount: Int
        do {
            mythCount = outputMyths.size
            buildTwoConstellationMyths(constellations, starters)
        } while (outputMyths.size > mythCount)

        // Sort by rank and assign points based on rank
        outputMyths
            .sortedBy { rankMyth(it, starters) }
            .forEachIndexed { index, myth ->
                val point = when {
                    index < (outputMyths.size * .33) -> { 2 }
                    index < (outputMyths.size * .66) -> { 3 }
                    else -> { 4 }
                }
                pointStrategy.setSinglePoint(myth, point)
            }

        return outputMyths
    }

    private fun buildTwoConstellationMyths(constellations: Set<Constellation>,
                                           starters: Set<Constellation>)
    {
        constellations.forEach first@ { first ->
            if (usageCount[first] ?: 0 >= first.limit) return@first

            val secondPotentialSorted = first
                .findAllNearby(constellations, pathCalculator, TWOS_MAX_FROM_FIRST)
                .filter { !first.findAllNearby(constellations, pathCalculator, 1).contains(it) }
                .filter { !usedVectors.getOrPut(first) { mutableSetOf() }.contains(it) }
                .filter { usageCount[it] ?: 0 < it.limit }
                .filter { isOneOrLessSmall(first, it) }
                .filter { isOneOrLessOuter(first, it) }
                .filter { totalDistanceFromStarter(starters, first, it) <= TWOS_MAX_DISTANCE_FROM_STARTER }
                .sortedBy { usageCount[it] ?: 0 }
            if (secondPotentialSorted.isEmpty()) return@first

            val secondLowestUsage = usageCount[secondPotentialSorted.first()] ?: 0
            val second = secondPotentialSorted
                .takeWhile { usageCount[it] ?: 0 <= secondLowestUsage }
                .random()

            addMyth(setOf(first, second))
        }
    }

    private fun addMyth(myth: Set<Constellation>) {
        if (myth.size != 2) return
        if (outputMyths.contains(myth)) return

        myth.forEach { first ->
            usageCount[first] = 1 + (usageCount[first] ?: 0)
            myth.subtract(setOf(first)).forEach { second ->
                usedVectors.getOrPut(first) { mutableSetOf() }.add(second)
            }
        }
        outputMyths.add(myth)
    }

    private fun rankMyth(myth: Set<Constellation>, starters: Set<Constellation>): Double {
        val orderedMyth = myth.toList()
        val first = orderedMyth[0]
        val second = orderedMyth[1]

        val distanceApart = pathCalculator.shortestPath(first, second)
        val firstToCenter = pathCalculator.shortestPathToMultiple(first, starters) + 1
        val secondToCenter = pathCalculator.shortestPathToMultiple(second, starters) + 1
        val totalToCenter = firstToCenter + secondToCenter
        val totalStars = starCount(first) + starCount(second)

        return (distanceApart / 1.2) + (totalToCenter / 1.8) + (totalStars / 7.0)
    }

    private fun starCount(constellation: Constellation): Int {
        return constellation.yellow + constellation.orange + constellation.blue
    }

    private fun isOneOrLessSmall(vararg constellations: Constellation): Boolean =
        constellations.count { it.ring.isSmall } <= 1

    private fun isOneOrLessOuter(vararg constellations: Constellation): Boolean =
        constellations.count { it.ring.isOuter } <= 1

    private fun totalDistanceFromStarter(starters: Set<Constellation>,
                                         vararg constellations: Constellation
    ): Int = constellations.map { it.distanceFromStarter(starters, pathCalculator) }.sum()
}
