package output

import calculations.PointStrategy
import model.Constellation

fun printConstellationData(constellations: Set<Constellation>) {
    var yellowCount = 0
    var orangeCount = 0
    var blueCount = 0
    var twoYellow = 0
    var twoOrange = 0
    var twoBlue = 0
    var threeYellow = 0
    var threeOrange = 0
    var threeBlue = 0
    constellations.forEach {
        yellowCount += it.yellow
        orangeCount += it.orange
        blueCount += it.blue

        if (it.yellow == 2) twoYellow++
        if (it.orange ==2) twoOrange++
        if (it.blue == 2) twoBlue++
        if (it.yellow == 3) threeYellow++
        if (it.orange == 3) threeOrange++
        if (it.blue == 3) threeBlue++
    }

    println("Total Yellow: $yellowCount")
    println("Total Orange: $orangeCount")
    println("Total Blue: $blueCount")
    println()

    println("Two Yellow: $twoYellow")
    println("Two Orange: $twoOrange")
    println("Two Blue: $twoBlue")
    println()

    println("Three Yellow: $threeYellow")
    println("Three Orange: $threeOrange")
    println("Three Blue: $threeBlue")
    println()
}

fun printMythData(myths: Set<Set<Constellation>>, pointStrategy: PointStrategy) {
    val constellationCount = mutableMapOf<Constellation, Int>()
    val sizeCount = mutableMapOf<Int, Int>()
    myths.forEach { myth ->
        sizeCount[myth.size] = (sizeCount[myth.size] ?: 0) + 1
        myth.forEach { constellation ->
            constellationCount[constellation] = (constellationCount[constellation] ?: 0) + 1
            print(constellation.name + " ")
        }
        print(pointStrategy.getMinor(myth).toString() + " ")
        print(pointStrategy.getMajor(myth).toString() + " ")
        println()
    }
    println()

    println("Total Myths: " + myths.size)
    println()

    sizeCount.keys.forEach {
        println(it.toString() + " Count: " +sizeCount[it])
    }
    println()

    constellationCount.keys.forEach {
        println(it.name + ": " + constellationCount[it])
    }
    println()
}
