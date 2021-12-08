package generators

import calculations.PathCalculator
import calculations.PointStrategy
import model.Constellation
import model.ConstellationOutput
import model.Ring
import kotlin.math.abs

class SingleUseEqual: GeneratorStrategy {

    private val pathCalculator = PathCalculator()
    private val usedConstellations = mutableSetOf<Constellation>()
    private val outputMyths = mutableSetOf<Set<Constellation>>()
    private val ranks = mutableMapOf<Set<Constellation>, Double>()

    override fun generateMyths(
        constellationOutput: ConstellationOutput,
        pointStrategy: PointStrategy
    ): Set<Set<Constellation>> {

        val constellations = constellationOutput.allConstellations
        val starters = constellationOutput.starters

        // Pair starters with outer ring
        starters.forEach { starter ->
            val rankSorted = constellations
                .filter { it.ring == Ring.Third }
                .filter { !usedConstellations.contains(it) }
                .sortedBy { rankMyth(setOf(starter, it), starters) }

            val second = rankSorted[(rankSorted.size / 2) + 1]
            addMyth(setOf(starter, second), starters)
        }

        // Pair the rest of the outers
        constellations
            .filter { it.ring == Ring.Third }
            .forEach outer@ { outer ->
                if (usedConstellations.contains(outer)) return@outer

                val rankAverage = rankAverage()
                val second = constellations.subtract(usedConstellations).subtract(setOf(outer))
                    .filter { !outer.findAllNearby(constellations, pathCalculator, 1).contains(it) }
                    .filter { isOneOrLessOuter(outer, it) }
                    .minBy { abs(rankAverage - rankMyth(setOf(outer, it), starters)) }

                second?.let { addMyth(setOf(outer, second), starters) }
            }

        // Pair the remaining constellations
        constellations.forEach first@ { first ->
            if (usedConstellations.contains(first)) return@first

            val rankAverage = rankAverage()
            val second = constellations.subtract(usedConstellations).subtract(setOf(first))
                .filter { !first.findAllNearby(constellations, pathCalculator, 1).contains(it) }
                .filter { isOneOrLessSmall(first, it) }
                .minBy { abs(rankAverage - rankMyth(setOf(first, it), starters)) }

            second?.let { addMyth(setOf(first, second), starters) }
        }

        return outputMyths
    }

    private fun addMyth(myth: Set<Constellation>, starters: Set<Constellation>) {
        if (myth.size != 2) return
        if (outputMyths.contains(myth)) return

        ranks[myth] = rankMyth(myth, starters)
        usedConstellations.addAll(myth)
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

    private fun rankAverage(): Double {
        return ranks.values.sum() / ranks.size
    }

    private fun starCount(constellation: Constellation): Int {
        return constellation.yellow + constellation.orange + constellation.blue
    }

    private fun isOneOrLessSmall(vararg constellations: Constellation): Boolean =
        constellations.count { it.ring.isSmall } <= 1

    private fun isOneOrLessOuter(vararg constellations: Constellation): Boolean =
        constellations.count { it.ring.isOuter } <= 1
}
