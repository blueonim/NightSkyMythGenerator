package generators

import calculations.PathCalculator
import calculations.PointStrategy
import model.Constellation
import model.ConstellationOutput
import print.printMythData

private const val MAX_VECTOR = 4
private const val MAX_DISTANCE = 6
private const val MAX_PATH_COMPLEXITY = 18
private const val MAX_STARS = 16
private const val MAX_COLOR = 8

class MythPruner: GeneratorStrategy {

    private val pathCalculator = PathCalculator()

    override fun generateMyths(
        constellationOutput: ConstellationOutput,
        pointStrategy: PointStrategy
    ): Set<Set<Constellation>> {
        val myths = createMyths(constellationOutput, pointStrategy)
        //printMythData(myths)

        // Do the prune a few times
        val threePruneValues = listOf(120, 100, 80, 70, 60, 50, 40)
        var threes = myths.toSet()
        threePruneValues.forEach { value -> threes = pruneThrees(threes, constellationOutput, value) }
        printMythData(threes, pointStrategy)

        return threes

        //TODO maybe pruning is the wrong approach - we just want "unique" myths - and more 3s than 2s
        //TODO maybe 2s and 3s can be dealt with separately
        //val prunedMyths = pruner(myths, constellationOutput)
        //printMythData(prunedMyths)
    }

    //TODO something is messed up here - the order of the input is effecting what myths are created
    //TODO maybe there's something wrong with using sets to test equality?
    private fun createMyths(
        constellationOutput: ConstellationOutput,
        pointStrategy: PointStrategy
    ): Set<Set<Constellation>> {

        val constellations = constellationOutput.allConstellations
        val myths = mutableSetOf<Set<Constellation>>()
        constellations.forEach { first ->
            constellations.subtract(setOf(first)).forEach second@ { second ->
                val two = setOf(first, second)

                //TODO this shouldn't matter - but it does
                if (myths.contains(two)) return@second

                val vectorOne = pathCalculator.shortestPath(first, second)
                var yellows = first.yellow + second.yellow
                var oranges = first.orange + second.orange
                var blues = first.blue + second.blue
                val twoStarCount = yellows + oranges + blues

                if (!constellationOutput.allowList.contains(two)) {
                    if (vectorOne > MAX_VECTOR) return@second
                    if (yellows > MAX_COLOR) return@second
                    if (oranges > MAX_COLOR) return@second
                    if (blues > MAX_COLOR) return@second
                    if (twoStarCount > MAX_STARS) return@second
                }

                pointStrategy.calculatePoints(two, pathCalculator)

                if (!constellationOutput.blockedList.contains(two)) myths.add(two)

                constellations.subtract(two).forEach third@ { third ->
                    val three = setOf(first, second, third)
                    if (myths.contains(three)) return@third

                    val vectorTwo = pathCalculator.shortestPath(first, third)
                    val vectorThree = pathCalculator.shortestPath(second, third)
                    val threeRingComplexity = (first.ring.value + second.ring.value + third.ring.value) / 1.5
                    val sortedVectors = listOf(vectorOne, vectorTwo, vectorThree).sorted()
                    val shortestDistance = sortedVectors[0] + sortedVectors[1]
                    val pathComplexity = threeRingComplexity + shortestDistance
                    yellows += third.yellow
                    oranges += third.orange
                    blues += third.blue
                    val threeCount = yellows + oranges + blues

                    if (!constellationOutput.allowList.contains(three)) {
                        if (vectorTwo > MAX_VECTOR) return@third
                        if ((vectorOne + vectorTwo) > MAX_DISTANCE) return@third
                        if (vectorThree > MAX_VECTOR) return@third
                        if ((vectorOne + vectorThree) > MAX_DISTANCE) return@third
                        if (pathComplexity > MAX_PATH_COMPLEXITY) return@third
                        if (yellows > MAX_COLOR) return@third
                        if (oranges > MAX_COLOR) return@third
                        if (blues > MAX_COLOR) return@third
                        if (threeCount > MAX_STARS) return@third
                    }

                    pointStrategy.calculatePoints(three, pathCalculator)

                    if (!constellationOutput.blockedList.contains(three)) myths.add(three)
                }
            }
        }
        return myths
    }

    private fun pruneThrees(myths: Set<Set<Constellation>>,
                            constellationOutput: ConstellationOutput,
                            returnCount: Int): Set<Set<Constellation>> {

        // Filter for three size myths that pass validation
        val threes = mutableSetOf<Set<Constellation>>()
        myths.filter { myth -> myth.size == 3
                && !constellationOutput.blockedList.contains(myth)
        }.forEach match@ { match ->
            constellationOutput.allowList.forEach { allowed ->
                if (match.containsAll(allowed)) return@match
            }
            threes.add(match)
        }

        // Collect stats to see what would be best to prune
        val constellationCount = mutableMapOf<Constellation, Int>()
        val combinationCount = mutableMapOf<Set<Constellation>, Int>()
        val mythCombinationMap = mutableMapOf<Set<Constellation>, Set<Set<Constellation>>>()
        threes.forEach { myth ->

            val combinations = mutableSetOf<Set<Constellation>>()
            myth.forEach { first ->
                constellationCount[first] = (constellationCount[first] ?: 0) + 1
                myth.subtract(setOf(first)).forEach { second ->
                    combinations.add(setOf(first, second))
                }
            }

            mythCombinationMap[myth] = combinations
            combinations.forEach { combination ->
                combinationCount[combination] = (combinationCount[combination] ?: 0) + 1
            }
        }

        // Add a ranking for each myth, based on composition compared to previously collected stats
        val mythRatings = mutableMapOf<Set<Constellation>, Double>()
        threes.forEach { myth ->

            //TODO try sorting by least present
            //mythRatings[myth] = myth.map { constellation -> constellationCount[constellation] ?: 0 }
            //    .min()?.toDouble() ?: 0.0

            var constellationRating = 0.0
            myth.forEach { constellation ->
                constellationRating += (constellationCount[constellation] ?: 1) / constellation.ring.desiredOccurrence
            }

            var combinationRating = 0.0
            mythCombinationMap[myth]?.forEach { combination ->
                combinationRating += combinationCount[combination] ?: 1
            }

            mythRatings[myth] = constellationRating + combinationRating
        }

        // Sort by rating and return the best set
        return mythRatings.toList()
            .sortedBy { (_, value) -> value }
            .subList(0, returnCount)
            .map { entry -> entry.first }
            .toSet()
    }

    //TODO could consider more unique point allocations
    private fun pruner(myths: Set<Set<Constellation>>,
                       constellationOutput: ConstellationOutput
    ): Set<Set<Constellation>> {

        val constellations = constellationOutput.allConstellations
        val allCombinations = mutableSetOf<Set<Constellation>>()
        constellations.forEach { first ->
            constellations.subtract(setOf(first)).forEach { second ->
                allCombinations.add(setOf(first, second))
            }
        }

        val prunedMyths = mutableSetOf<Set<Constellation>>()
        prunedMyths.addAll(constellationOutput.allowList)

        // TODO pruning out "covered combinations" eliminates most of the 3 size myths
        // TODO they are more likely to contain one of the "combinations" that have already been seen
        val coveredCombinations = mutableSetOf<Set<Constellation>>()
        coveredCombinations.addAll(constellationOutput.allowList)

        val constellationCount = mutableMapOf<Constellation, Int>()
        val sizeCount = mutableMapOf<Int, Int>()
        allCombinations.forEach combination@ { combination ->
            if (coveredCombinations.contains(combination)) return@combination

            val matchingMyths = mutableSetOf<Set<Constellation>>()
            myths.filter { myth -> myth.containsAll(combination)
                    && !constellationOutput.blockedList.contains(myth)
                    && !prunedMyths.contains(myth)
            }.forEach match@ { match ->
                match.forEach { first ->
                    match.subtract(setOf(first)).forEach { second ->
                        if (coveredCombinations.contains(setOf(first, second))) return@match
                    }
                }
                matchingMyths.add(match)
            }
            if (matchingMyths.isEmpty()) return@combination

            var lowestRating = 0.0
            var bestMyth = emptySet<Constellation>()
            matchingMyths.forEach { match ->
                var cumulativeRating = 0.0
                match.forEach { constellation ->
                    cumulativeRating += ((constellationCount[constellation] ?: 1)
                            / constellation.ring.desiredOccurrence)
                }

                if (lowestRating == 0.0
                    || cumulativeRating < lowestRating
                    || (match.size == 3 && bestMyth.size < 3)) {
                    lowestRating = cumulativeRating
                    bestMyth = match
                }
            }
            if (bestMyth.isEmpty()) return@combination

            bestMyth.forEach { constellation ->
                constellationCount[constellation] = (constellationCount[constellation] ?: 0) + 1
                sizeCount[bestMyth.size] = (sizeCount[bestMyth.size] ?: 0) + 1
            }

            if (bestMyth.size == 2) {
                coveredCombinations.add(bestMyth)
            } else if (bestMyth.size > 2) {
                bestMyth.forEach { first ->
                    bestMyth.subtract(setOf(first)).forEach { second ->
                        coveredCombinations.add(setOf(first, second))
                    }
                }
            }

            prunedMyths.add(bestMyth)
        }

        return prunedMyths
    }
}
