import calculations.AssignedPoint
import generators.SystematicRanked
import model.createConstellations
import print.printConstellationData
import print.printMythData
import print.writeToFile

fun main(){
    val constellationOutput = createConstellations()
    printConstellationData(constellationOutput.allConstellations)

    val pointStrategy = AssignedPoint()
    val myths = SystematicRanked().generateMyths(constellationOutput, pointStrategy)
    printMythData(myths, pointStrategy)
    writeToFile(myths, pointStrategy)
}
