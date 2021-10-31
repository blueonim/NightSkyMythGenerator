package generators

import calculations.PathCalculator
import calculations.PointStrategy
import model.Constellation
import model.ConstellationOutput

class Systematic: GeneratorStrategy {

    private val pathCalculator = PathCalculator()
    private val usageCount = mutableMapOf<Constellation, Int>()
    private val usedVectors = mutableMapOf<Constellation, MutableSet<Constellation>>()
    private val outputMyths = mutableSetOf<Set<Constellation>>()

    override fun generateMyths(
        constellationOutput: ConstellationOutput,
        pointStrategy: PointStrategy
    ): Set<Set<Constellation>> {

        // Add the allow list
        constellationOutput.allowList.forEach { addMyth(it, pointStrategy) }

        // Add blocked list to used vectors
        constellationOutput.blockedList.forEach { blocked ->
            blocked.forEach { first ->
                blocked.subtract(setOf(first)).forEach { second ->
                    usedVectors.getOrPut(first) { mutableSetOf() }.add(second)
                }
            }
        }

        val constellations = constellationOutput.allConstellations
        buildThreeConstellationMyths(constellations, pointStrategy)
        buildTwoConstellationMyths(constellations, pointStrategy)

        return outputMyths
    }

    //TODO factor in desired occurrence - not as simple as just multiplying
    private fun buildThreeConstellationMyths(constellations: Set<Constellation>, pointStrategy: PointStrategy) {
        constellations.forEach first@ { first ->
            val secondPotentialSorted = first
                .findAllNearby(constellations, pathCalculator, 4)
                .filter { !usedVectors.getOrPut(first) { mutableSetOf() }.contains(it) }
                .sortedBy { usageCount[it] ?: 0 }
            if (secondPotentialSorted.isEmpty()) return@first

            val secondLowestUsage = usageCount[secondPotentialSorted.first()] ?: 0
            val second = secondPotentialSorted
                .takeWhile { usageCount[it] ?: 0 <= secondLowestUsage }
                .random()

            // Find a third that's close to one of the others
            val nearFirst = first
                .findAllNearby(constellations.subtract(setOf(second)), pathCalculator, 2)
                .filter { !usedVectors.getOrPut(first) { mutableSetOf() }.contains(it) }
            val nearSecond = second
                .findAllNearby(constellations.subtract(setOf(first)), pathCalculator, 2)
                .filter { !usedVectors.getOrPut(first) { mutableSetOf() }.contains(it) }

            val thirdPotentialSorted = nearFirst.plus(nearSecond)
                .sortedBy { usageCount[it] ?: 0 }
            if (thirdPotentialSorted.isEmpty()) return@first

            val thirdLowestUsage = usageCount[thirdPotentialSorted.first()] ?: 0
            val third = thirdPotentialSorted
                .takeWhile { usageCount[it] ?: 0 <= thirdLowestUsage }
                .random()

            addMyth(setOf(first, second, third), pointStrategy)
        }
    }

    private fun buildTwoConstellationMyths(constellations: Set<Constellation>, pointStrategy: PointStrategy) {
        constellations.forEach first@ { first ->
            val secondPotentialSorted = first
                .findAllNearby(constellations, pathCalculator, 4)
                .filter { !usedVectors.getOrPut(first) { mutableSetOf() }.contains(it) }
                .sortedBy { usageCount[it] ?: 0 }
            if (secondPotentialSorted.isEmpty()) return@first

            val secondLowestUsage = usageCount[secondPotentialSorted.first()] ?: 0
            val second = secondPotentialSorted
                .takeWhile { usageCount[it] ?: 0 <= secondLowestUsage }
                .random()

            addMyth(setOf(first, second), pointStrategy)
        }
    }

    private fun addMyth(myth: Set<Constellation>, pointStrategy: PointStrategy) {
        if (myth.size != 2 && myth.size != 3) return
        if (outputMyths.contains(myth)) return

        myth.forEach { first ->
            usageCount[first] = 1 + (usageCount[first] ?: 0)
            myth.subtract(setOf(first)).forEach { second ->
                usedVectors.getOrPut(first) { mutableSetOf() }.add(second)
            }
        }

        pointStrategy.calculatePoints(myth, pathCalculator)
        outputMyths.add(myth)
    }

    private fun printUsageStats() {
        usageCount.forEach { (constellation, count) -> println(constellation.name + " " + count) }
        println()

        usedVectors.forEach { (constellation, used) ->
            used.forEach { second ->
                println(constellation.name + " " + second.name)
            }
        }
        println()
    }
}

fun Constellation.findAllNearby(constellations: Set<Constellation>,
                                pathCalculator: PathCalculator,
                                distance: Int
): Set<Constellation> = constellations.subtract(setOf(this))
    .filter { pathCalculator.shortestPath(this, it) <= distance }.toSet()
