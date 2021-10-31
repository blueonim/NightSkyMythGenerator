import calculations.PathComplexity
import generators.Systematic
import model.createConstellations
import output.printMythData

fun main(){
    val pointStrategy = PathComplexity()
    printMythData(Systematic().generateMyths(createConstellations(), pointStrategy), pointStrategy)
}
