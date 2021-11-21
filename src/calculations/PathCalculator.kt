package calculations

import model.Constellation

class PathCalculator {
    private val shortestMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()

    fun shortestPath(first: Constellation, second: Constellation): Int {
        if (first == second) return 0

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

    fun shortestPathToMultiple(single: Constellation, multiple: Set<Constellation>): Int {
        if (multiple.contains(single)) return 0
        return multiple.map { shortestPath(single, it) }.min() ?: 0
    }
}
