import calculations.EqualPoints
import generators.Equalizer
import model.createConstellations
import print.printConstellationData
import print.printMythData
import print.writeToFile

fun main(){
    val constellationOutput = createConstellations()
    printConstellationData(constellationOutput.allConstellations)

    val pointStrategy = EqualPoints()
    val myths = Equalizer().generateMyths(constellationOutput, pointStrategy)
    printMythData(myths, pointStrategy)
    writeToFile(myths, pointStrategy)
}
