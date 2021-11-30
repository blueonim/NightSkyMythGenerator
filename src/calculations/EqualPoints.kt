package calculations

import model.Constellation

class EqualPoints: PointStrategy {

    override fun calculatePoints(myth: Set<Constellation>,
                                 starters: Set<Constellation>,
                                 pathCalculator: PathCalculator) {}
    override fun setSinglePoint(myth: Set<Constellation>, value: Int) {}
    override fun getSinglePoint(myth: Set<Constellation>): Int = 0
    override fun getMinor(myth: Set<Constellation>): Int = 0
    override fun getMajor(myth: Set<Constellation>): Int = 0
}
