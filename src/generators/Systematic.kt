package generators

import calculations.PathCalculator
import calculations.PointStrategy
import model.Constellation
import model.ConstellationOutput

private const val THREES_MAX_FROM_FIRST = 2
private const val THREES_MAX_FROM_EITHER = 1
private const val TWOS_MAX_FROM_FIRST = 3
private const val THREE_MYTH_CAP = 15
private const val THREES_MAX_DISTANCE_FROM_STARTER = 6
private const val TWOS_MAX_DISTANCE_FROM_STARTER = 4

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
        constellationOutput.allowList.forEach { addMyth(it, constellationOutput.starters, pointStrategy) }

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

        // Create initial set of three size myths
        //TODO disable 3s for now
        //buildThreeConstellationMyths(constellations, starters, pointStrategy)

        // Make two size myths until we run out of possibilities
        var mythCount: Int
        do {
            mythCount = outputMyths.size
            buildTwoConstellationMyths(constellations, starters, pointStrategy)
        } while (outputMyths.size > mythCount)

        return outputMyths
    }

    private fun buildThreeConstellationMyths(constellations: Set<Constellation>,
                                             starters: Set<Constellation>,
                                             pointStrategy: PointStrategy)
    {
        var count = 0
        constellations.forEach first@ { first ->
            if (count >= THREE_MYTH_CAP) return
            if (usageCount[first] ?: 0 >= first.limit) return@first

            // Find one that's close to the first
            val secondPotentialSorted = first.findAllNearby(constellations, pathCalculator, THREES_MAX_FROM_FIRST)
                .filter { !usedVectors.getOrPut(first) { mutableSetOf() }.contains(it) }
                .filter { usageCount[it] ?: 0 < it.limit }
                .filter { isOneOrLessSmall(first, it) }
                .filter { isOneOrLessOuter(first, it) }
                .filter { totalDistanceFromStarter(starters, first, it) <= THREES_MAX_DISTANCE_FROM_STARTER }
                .sortedBy { usageCount[it] ?: 0 }
            if (secondPotentialSorted.isEmpty()) return@first

            val secondLowestUsage = usageCount[secondPotentialSorted.first()] ?: 0
            val second = secondPotentialSorted
                .takeWhile { usageCount[it] ?: 0 <= secondLowestUsage }
                .random()

            // Find a third that's close to one of the others
            val nearEither = first
                .findAllNearby(constellations.subtract(setOf(second)), pathCalculator, THREES_MAX_FROM_EITHER)
                .plus(second
                    .findAllNearby(constellations.subtract(setOf(first)), pathCalculator, THREES_MAX_FROM_EITHER))

            val thirdPotentialSorted = nearEither
                .filter { !usedVectors.getOrPut(first) { mutableSetOf() }.contains(it) }
                .filter { !usedVectors.getOrPut(second) { mutableSetOf() }.contains(it) }
                .filter { usageCount[it] ?: 0 < it.limit }
                .filter { isOneOrLessSmall(first, second, it) }
                .filter { isOneOrLessOuter(first, second, it) }
                .filter { totalDistanceFromStarter(starters, first, second, it) <= THREES_MAX_DISTANCE_FROM_STARTER }
                .sortedBy { usageCount[it] ?: 0 }
            if (thirdPotentialSorted.isEmpty()) return@first

            val thirdLowestUsage = usageCount[thirdPotentialSorted.first()] ?: 0
            val third = thirdPotentialSorted
                .takeWhile { usageCount[it] ?: 0 <= thirdLowestUsage }
                .random()

            count++
            addMyth(setOf(first, second, third), starters, pointStrategy)
        }
    }

    private fun buildTwoConstellationMyths(constellations: Set<Constellation>,
                                           starters: Set<Constellation>,
                                           pointStrategy: PointStrategy)
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

            addMyth(setOf(first, second), starters, pointStrategy)
        }
    }

    private fun addMyth(myth: Set<Constellation>, starters: Set<Constellation>, pointStrategy: PointStrategy) {
        if (myth.size != 2 && myth.size != 3) return
        if (outputMyths.contains(myth)) return

        myth.forEach { first ->
            usageCount[first] = 1 + (usageCount[first] ?: 0)
            myth.subtract(setOf(first)).forEach { second ->
                usedVectors.getOrPut(first) { mutableSetOf() }.add(second)
            }
        }

        pointStrategy.calculatePoints(myth, starters, pathCalculator)
        outputMyths.add(myth)
    }

    private fun isOneOrLessSmall(vararg constellations: Constellation): Boolean =
        constellations.count { it.ring.isSmall } <= 1

    private fun isOneOrLessOuter(vararg constellations: Constellation): Boolean =
        constellations.count { it.ring.isOuter } <= 1

    private fun totalDistanceFromStarter(starters: Set<Constellation>,
                                         vararg constellations: Constellation
    ): Int = constellations.map { it.distanceFromStarter(starters, pathCalculator) }.sum()

    private fun printUsageStats() {
        usageCount.forEach { (constellation, count) -> println(constellation.name + " " + count) }
        println()

        val combinationCount = mutableMapOf<Set<Constellation>, Int>()
        usedVectors.forEach { (constellation, used) ->
            used.forEach { second ->
                println(constellation.name + " " + second.name)

                val combination = setOf(constellation, second)
                combinationCount[combination] = 1 + (combinationCount[combination] ?: 0)
            }
        }
        println()

        combinationCount.forEach { (combination, count) ->
            combination.forEach { constellation -> print(constellation.name + " ") }
            println(count)
        }
        println()
    }
}

fun Constellation.findAllNearby(constellations: Set<Constellation>,
                                pathCalculator: PathCalculator,
                                distance: Int
): Set<Constellation> = constellations.subtract(setOf(this))
    .filter { pathCalculator.shortestPath(this, it) <= distance }.toSet()

fun Constellation.distanceFromStarter(starters: Set<Constellation>,
                                      pathCalculator: PathCalculator
): Int {
    if (starters.contains(this)) return 0
    return starters.map { pathCalculator.shortestPath(this, it) }.min() ?: 0
}
