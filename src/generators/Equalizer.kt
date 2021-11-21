package generators

import calculations.PathCalculator
import calculations.PointStrategy
import model.Constellation
import model.ConstellationOutput

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
        val firstToCenter = pathCalculator.shortestPathToMultiple(first, starters)
        val secondToCenter = pathCalculator.shortestPathToMultiple(second, starters)
        val totalToCenter = firstToCenter + secondToCenter
        val totalStars = starCount(first) + starCount(second)

        val rank = distanceApart + (totalToCenter / 2.0) + (totalStars / 6.0)

        return RankedMyth(first, second, rank)
    }

    private fun chooseMyths(rankedMyths: Set<RankedMyth>): Set<Set<Constellation>> {
        //TODO testing - sort and print
        /*rankedMyths.toList().sortedBy { it.rank }.forEach {
            println(it.first.name + " " + it.second.name + " " + it.rank)
        }*/

        //choose middle 50ish
        val index = (rankedMyths.size / 2) - 25
        val middleRanks = rankedMyths.toList().sortedBy { it.rank }.subList(index, index + 50)

        //TODO work on optimizing for equal usage of constellations/stars, etc
        //do "inside-out" selection
        //add caps for constellation usage?

        return middleRanks.map { setOf(it.first, it.second) }.toSet()
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
