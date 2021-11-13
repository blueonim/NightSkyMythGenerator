package calculations

import model.Constellation

class PathComplexity: PointStrategy {
    private val minorMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()
    private val majorMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()
    private val singlePointMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()

    override fun calculatePoints(myth: Set<Constellation>, pathCalculator: PathCalculator) {
        if (minorMap[myth] != null && majorMap[myth] != null) return

        val list = myth.toList()
        if (myth.size == 2) {
            val first = list[0]
            val second = list[1]

            val ringComplexity = (first.ring.value + second.ring.value) / 2.5
            val vector = pathCalculator.shortestPath(first, second)
            val starTotal = starCount(first) + starCount(second)

            minorMap[myth] = (ringComplexity + (vector / 2.0)).toInt()
            majorMap[myth] = (ringComplexity + vector + (starTotal / 7.0)).toInt()

            val singleRingComplexity = (first.ring.value + second.ring.value) / 3.0
            singlePointMap[myth] = (singleRingComplexity + (vector / 2.8) + (starTotal / 8.0)).toInt()

        } else if (myth.size == 3) {
            val first = list[0]
            val second = list[1]
            val third = list[2]

            val vectorOne = pathCalculator.shortestPath(first, second)
            val vectorTwo = pathCalculator.shortestPath(first, third)
            val vectorThree = pathCalculator.shortestPath(second, third)
            val ringComplexity = (first.ring.value + second.ring.value + third.ring.value) / 2.5
            val sortedVectors = listOf(vectorOne, vectorTwo, vectorThree).sorted()
            val shortestDistance = sortedVectors[0] + sortedVectors[1]
            val starTotal = starCount(first) + starCount(second) + starCount(third)

            minorMap[myth] = (ringComplexity + (shortestDistance / 2.5)).toInt()
            majorMap[myth] = (ringComplexity + (shortestDistance / 1.5) + (starTotal / 7.0)).toInt()

            val singleRingComplexity = (first.ring.value + second.ring.value + third.ring.value) / 2.8
            singlePointMap[myth] = (singleRingComplexity + (shortestDistance / 2.8) + (starTotal / 7.0)).toInt()
        }
    }

    override fun getMinor(myth: Set<Constellation>): Int {
        return minorMap[myth] ?: 0
    }

    override fun getMajor(myth: Set<Constellation>): Int {
        return majorMap[myth] ?: 0
    }

    override fun getSinglePoint(myth: Set<Constellation>): Int {
        return singlePointMap[myth] ?: 0
    }

    private fun starCount(constellation: Constellation): Int {
        return constellation.yellow + constellation.orange + constellation.blue
    }
}
