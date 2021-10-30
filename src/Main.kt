import calculations.PathComplexity
import generators.MythPruner
import model.createConstellations

fun main(){
    MythPruner().generateMyths(createConstellations(), PathComplexity())
}
