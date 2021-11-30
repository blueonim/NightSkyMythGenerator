package calculations

import model.Constellation

class AssignedPoint: PointStrategy {

    private val singlePointMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()

    override fun calculatePoints(
        myth: Set<Constellation>,
        starters: Set<Constellation>,
        pathCalculator: PathCalculator
    ) {}

    override fun setSinglePoint(myth: Set<Constellation>, value: Int) {
        singlePointMap[myth] = value
    }

    override fun getSinglePoint(myth: Set<Constellation>): Int = singlePointMap[myth] ?: 0

    override fun getMinor(myth: Set<Constellation>): Int = 0
    override fun getMajor(myth: Set<Constellation>): Int = 0
}
