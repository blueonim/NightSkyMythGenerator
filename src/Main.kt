import calculations.PathComplexity
import generators.Systematic
import model.createConstellations
import print.printConstellationData
import print.printMythData
import print.writeToFile

fun main(){
    val constellationOutput = createConstellations()
    printConstellationData(constellationOutput.allConstellations)

    val pointStrategy = PathComplexity()
    val myths = Systematic().generateMyths(constellationOutput, pointStrategy)
    printMythData(myths, pointStrategy)
    writeToFile(myths, pointStrategy)
}
