package calculations

import model.Constellation

interface PointStrategy {
    fun calculatePoints(myth: Set<Constellation>, starters: Set<Constellation>, pathCalculator: PathCalculator)
    fun setSinglePoint(myth: Set<Constellation>, value: Int)
    fun getMinor(myth: Set<Constellation>): Int
    fun getMajor(myth: Set<Constellation>): Int
    fun getSinglePoint(myth: Set<Constellation>): Int
}
