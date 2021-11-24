package generators

import calculations.PathCalculator
import calculations.PointStrategy
import model.Constellation
import model.ConstellationOutput

private const val MAX_MYTHS = 100
private const val MAX_USAGE = 3

class Equalizer:GeneratorStrategy {

    private val pathCalculator = PathCalculator()

    override fun generateMyths(
        constellationOutput: ConstellationOutput,
        pointStrategy: PointStrategy
    ): Set<Set<Constellation>> {
        return chooseMyths(createAll(constellationOutput))
    }

    private fun createAll(constellationOutput: ConstellationOutput): Set<RankedMyth> {
        val constellations = constellationOutput.allConstellations.toList()
        val myths = mutableSetOf<RankedMyth>()
        constellations.forEach { first ->
            constellations.subList(constellations.indexOf(first) + 1, constellations.size).forEach { second ->
                //TODO add rules for invalid myths (both small, both outer) - might not need to with ranking
                myths.add(rankMyth(first, second, constellationOutput.starters))
            }
        }
        return myths
    }

    private fun rankMyth(first: Constellation, second: Constellation, starters: Set<Constellation>): RankedMyth {
        val distanceApart = pathCalculator.shortestPath(first, second)
        val firstToCenter = pathCalculator.shortestPathToMultiple(first, starters) + 1
        val secondToCenter = pathCalculator.shortestPathToMultiple(second, starters) + 1
        val totalToCenter = firstToCenter + secondToCenter
        val totalStars = starCount(first) + starCount(second)

        val rank = (distanceApart / 1.2) + (totalToCenter / 1.8) + (totalStars / 7.0)

        return RankedMyth(first, second, rank)
    }

    private fun chooseMyths(rankedMyths: Set<RankedMyth>): Set<Set<Constellation>> {
        var lowIndex = rankedMyths.size / 2
        var highIndex = lowIndex + 1
        val ordered = rankedMyths.toList().sortedBy { it.rank }

        val usage = mutableMapOf<Constellation, Int>()

        val chosen = mutableSetOf<Set<Constellation>>()
        while (chosen.size < MAX_MYTHS && lowIndex >=0 && highIndex < ordered.size) {
            setOf(ordered.elementAtOrNull(lowIndex), ordered.elementAtOrNull(highIndex))
                .filterNotNull()
                .filter { usage[it.first] ?: 0 < MAX_USAGE }
                .filter { usage[it.second] ?: 0 < MAX_USAGE }
                .forEach {
                    usage[it.first] = 1 + (usage[it.first] ?: 0)
                    usage[it.second] = 1 + (usage[it.second] ?: 0)
                    chosen.add(setOf(it.first, it.second))
            }
            lowIndex --
            highIndex ++
        }

        return chosen
    }

    private fun starCount(constellation: Constellation): Int {
        return constellation.yellow + constellation.orange + constellation.blue
    }

    private data class RankedMyth (
        val first: Constellation,
        val second: Constellation,
        val rank: Double
    )
}
