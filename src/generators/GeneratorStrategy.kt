package generators

import calculations.PointStrategy
import model.Constellation
import model.ConstellationOutput

interface GeneratorStrategy {
    fun generateMyths(constellationOutput: ConstellationOutput, pointStrategy: PointStrategy): Set<Set<Constellation>>
}
