package calculations

import model.Constellation

interface PointStrategy {
    fun calculatePoints(myth: Set<Constellation>, pathCalculator: PathCalculator)
    fun getMinor(myth: Set<Constellation>): Int
    fun getMajor(myth: Set<Constellation>): Int
}
