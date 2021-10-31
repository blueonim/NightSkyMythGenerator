package calculations

import model.Constellation

class PathCalculator {
    private val shortestMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()

    fun shortestPath(first: Constellation, second: Constellation): Int {
        val vector = setOf(first, second)
        shortestMap[vector]?.let { return it }

        var steps = 1
        var toVisit = first.connections
        while (!toVisit.contains(second)) {
            steps ++
            toVisit = toVisit.flatMap { it.connections }.toSet()
        }

        shortestMap[vector] = steps
        return steps
    }
}