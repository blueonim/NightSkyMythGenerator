package calculations

import model.Constellation

class PathComplexity: PointStrategy {
    private val minorMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()
    private val majorMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()

    override fun calculatePoints(myth: Set<Constellation>, pathCalculator: PathCalculator) {
        if (minorMap[myth] != null && majorMap[myth] != null) return

        val list = myth.toList()
        if (myth.size == 2) {
            val first = list[0]
            val second = list[1]

            val ringComplexity = (first.ring.value + second.ring.value) / 1.2
            val vector = pathCalculator.shortestPath(first, second)
            val starTotal = starCount(first) + starCount(second)

            minorMap[myth] = ringComplexity.toInt() + (vector / 2)
            majorMap[myth] = ringComplexity.toInt() + vector + (starTotal / 7)

        } else if (myth.size == 3) {
            val first = list[0]
            val second = list[1]
            val third = list[2]

            val vectorOne = pathCalculator.shortestPath(first, second)
            val vectorTwo = pathCalculator.shortestPath(first, third)
            val vectorThree = pathCalculator.shortestPath(second, third)
            val ringComplexity = (first.ring.value + second.ring.value + third.ring.value) / 1.5
            val sortedVectors = listOf(vectorOne, vectorTwo, vectorThree).sorted()
            val shortestDistance = sortedVectors[0] + sortedVectors[1]
            val pathComplexity = ringComplexity + shortestDistance
            val starTotal = starCount(first) + starCount(second) + starCount(third)

            minorMap[myth] = ringComplexity.toInt() + (shortestDistance / 2)
            majorMap[myth] = pathComplexity.toInt() + (starTotal / 7)
        }
    }

    override fun getMinor(myth: Set<Constellation>): Int {
        return minorMap[myth] ?: 0
    }

    override fun getMajor(myth: Set<Constellation>): Int {
        return majorMap[myth] ?: 0
    }

    private fun starCount(constellation: Constellation): Int {
        return constellation.yellow + constellation.orange + constellation.blue
    }
}
