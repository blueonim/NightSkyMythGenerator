import calculations.PathComplexity
import generators.Systematic
import model.createConstellations
import print.printMythData
import print.writeToFile

fun main(){
    val pointStrategy = PathComplexity()
    val myths = Systematic().generateMyths(createConstellations(), pointStrategy)
    printMythData(myths, pointStrategy)
    writeToFile(myths, pointStrategy)
}
