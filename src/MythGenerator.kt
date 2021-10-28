private const val MAX_VECTOR = 4
private const val MAX_DISTANCE = 4
private const val MAX_PATH_COMPLEXITY = 13
private const val MAX_STARS = 15
private const val MAX_COLOR = 7

class MythGenerator {

    private val shortestMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()
    private val minorMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()
    private val majorMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()

    fun generate() {
        val constellationOutput = setupConstellations()
        //printConstellationData(constellationOutput.allConstellations)

        val myths = createMyths(constellationOutput)
        //printMythData(myths)

        val prunedMyths = pruner(myths, constellationOutput)
        printMythData(prunedMyths)
    }

    private fun setupConstellations(): ConstellationOutput {

        // Create all constellations and add them to the set
        val wolf = Constellation("Wolf", Ring.Starter, yellow = 1, orange = 1, blue = 1)
        val cat = Constellation("Cat", Ring.First, yellow = 1, orange = 2, blue = 2)
        val mouse = Constellation("Mouse", Ring.FirstSmall, yellow = 1)
        val fish = Constellation("Fish", Ring.Starter, yellow = 1, orange = 1, blue = 1)
        val boat = Constellation("Boat", Ring.First, yellow = 1, orange = 2, blue = 1)
        val anchor = Constellation("Anchor", Ring.FirstSmall, yellow = 1, orange = 1)
        val arch = Constellation("Arch", Ring.Starter, yellow = 1, orange = 1, blue = 1)
        val friends = Constellation("Friends", Ring.First, yellow = 2, orange = 1, blue = 1)
        val mountain = Constellation("Mountain", Ring.Starter, yellow = 1, orange = 1, blue = 1)
        val tree = Constellation("Tree", Ring.First, yellow = 1, orange = 1, blue = 2)
        val nut = Constellation("Nut", Ring.FirstSmall, orange = 1)
        val basket = Constellation("Basket", Ring.Second, yellow = 2, blue = 2)
        val fruit = Constellation("Fruit", Ring.SecondSmall, yellow = 1, blue = 1)
        val snake = Constellation("Snake", Ring.Second, yellow = 3, orange = 2)
        val scales = Constellation("Scales", Ring.Second, orange = 1, blue = 3)
        val fountain = Constellation("Fountain", Ring.Second, yellow = 2, orange = 3)
        val coin = Constellation("Coin", Ring.SecondSmall, blue = 1)
        val dancer = Constellation("Dancer", Ring.Second, yellow = 1, orange = 3)
        val ladle = Constellation("Ladle", Ring.Second, yellow = 2, blue = 1)
        val flower = Constellation("Flower", Ring.Second, orange = 2, blue = 2)
        val seed = Constellation("Seed", Ring.SecondSmall, orange = 1, blue = 1)
        val eye = Constellation("Eye", Ring.Second, orange = 2, blue = 3)
        val hedgehog = Constellation("Hedgehog", Ring.Second, yellow = 3, orange = 1)
        val leaf = Constellation("Leaf", Ring.Second, orange = 1, blue = 2)
        val fire = Constellation("Fire", Ring.Third, yellow = 1, orange = 3, blue = 2)
        val spark = Constellation("Spark", Ring.ThirdSmall, yellow = 2)
        val mask = Constellation("Mask", Ring.Third, yellow = 2, orange = 1, blue = 2)
        val teapot = Constellation("Teapot", Ring.Third, yellow = 2, orange = 1, blue = 1)
        val cup = Constellation("Cup", Ring.ThirdSmall, blue = 2)
        val satchel = Constellation("Satchel", Ring.Third, yellow = 1, orange = 2)
        val chair = Constellation("Chair", Ring.Third, yellow = 2, orange = 2)
        val musician = Constellation("Musician", Ring.Third, yellow = 1, orange = 2, blue = 2)
        val song = Constellation("Song", Ring.ThirdSmall, orange = 2)
        val spider = Constellation("Spider", Ring.Third, yellow = 2, orange = 2, blue = 2)
        val bird = Constellation("Bird", Ring.Third, yellow = 2, orange = 2, blue = 1)
        val snail = Constellation("Snail", Ring.Third, yellow = 1, orange = 1, blue = 2)
        val book = Constellation("Book", Ring.Third, yellow = 3, blue = 1)

        // Add all connections to each constellation
        wolf.connections = setOf(fish, arch, mountain, tree, cat, leaf, basket)
        cat.connections = setOf(wolf, fish, mouse, basket, snake, scales)
        mouse.connections = setOf(cat)
        fish.connections = setOf(arch, mountain, wolf, cat, boat, scales)
        boat.connections = setOf(fish, arch, anchor, fountain)
        anchor.connections = setOf(boat)
        arch.connections = setOf(mountain, wolf, fish, boat, friends, dancer)
        friends.connections = setOf(arch, mountain, ladle, flower)
        mountain.connections = setOf(wolf, fish, arch, friends, tree, flower, eye)
        tree.connections = setOf(mountain, wolf, nut, hedgehog, leaf)
        nut.connections = setOf(tree)
        basket.connections = setOf(wolf, cat, leaf, snake, book, fire, fruit)
        fruit.connections = setOf(basket)
        snake.connections = setOf(cat, basket, scales, fire, mask)
        scales.connections = setOf(cat, fish, snake, fountain, mask)
        fountain.connections = setOf(boat, scales, dancer, mask, teapot, coin)
        coin.connections = setOf(fountain)
        dancer.connections = setOf(arch, fountain, ladle, teapot, satchel)
        ladle.connections = setOf(friends, dancer, flower, satchel, chair, musician)
        flower.connections = setOf(friends, mountain, ladle, eye, musician, spider, seed)
        seed.connections = setOf(flower)
        eye.connections = setOf(mountain, flower, hedgehog, spider, bird)
        hedgehog.connections = setOf(tree, eye, leaf, bird, snail)
        leaf.connections = setOf(tree, wolf, hedgehog, basket, snail, book)
        fire.connections = setOf(basket, snake, book, mask, spark)
        spark.connections = setOf(fire)
        mask.connections = setOf(snake, scales, fountain, fire, teapot)
        teapot.connections = setOf(fountain, dancer, mask, satchel, cup)
        cup.connections = setOf(teapot)
        satchel.connections = setOf(dancer, ladle, teapot, chair)
        chair.connections = setOf(ladle, satchel, musician)
        musician.connections = setOf(ladle, flower, chair, spider, song)
        song.connections = setOf(musician)
        spider.connections = setOf(flower, eye, musician, bird)
        bird.connections = setOf(eye, hedgehog, spider, snail)
        snail.connections = setOf(hedgehog, leaf, bird, book)
        book.connections = setOf(leaf, basket, snail, fire)

        // Add constellations to the set
        val constellations = mutableSetOf<Constellation>()
        constellations.add(wolf)
        constellations.add(cat)
        constellations.add(mouse)
        constellations.add(fish)
        constellations.add(boat)
        constellations.add(anchor)
        constellations.add(arch)
        constellations.add(friends)
        constellations.add(mountain)
        constellations.add(tree)
        constellations.add(nut)
        constellations.add(basket)
        constellations.add(fruit)
        constellations.add(snake)
        constellations.add(scales)
        constellations.add(fountain)
        constellations.add(coin)
        constellations.add(dancer)
        constellations.add(ladle)
        constellations.add(flower)
        constellations.add(seed)
        constellations.add(eye)
        constellations.add(hedgehog)
        constellations.add(leaf)
        constellations.add(fire)
        constellations.add(spark)
        constellations.add(mask)
        constellations.add(teapot)
        constellations.add(cup)
        constellations.add(satchel)
        constellations.add(chair)
        constellations.add(musician)
        constellations.add(song)
        constellations.add(spider)
        constellations.add(bird)
        constellations.add(snail)
        constellations.add(book)

        // Create allow list
        val allowList = mutableSetOf<Set<Constellation>>()
        allowList.add(setOf(wolf, mountain))
        allowList.add(setOf(mountain, arch))
        allowList.add(setOf(arch, fish))
        allowList.add(setOf(fish, wolf))

        // Create blocked list
        val blockedList = mutableSetOf<Set<Constellation>>()
        blockedList.add(setOf(wolf, arch))
        blockedList.add(setOf(fish, mountain))

        return ConstellationOutput(constellations, allowList, blockedList)
    }

    private fun createMyths(constellationOutput: ConstellationOutput): Set<Set<Constellation>> {
        val constellations = constellationOutput.allConstellations
        val myths = mutableSetOf<Set<Constellation>>()
        constellations.forEach { first ->
            constellations.subtract(setOf(first)).forEach second@ { second ->
                val two = setOf(first, second)

                val vectorOne = shortestPath(first, second)
                var yellows = first.yellow + second.yellow
                var oranges = first.orange + second.orange
                var blues = first.blue + second.blue
                val twoStarCount = yellows + oranges + blues

                if (!constellationOutput.allowList.contains(two)) {
                    if (vectorOne > MAX_VECTOR) return@second
                    if (yellows > MAX_COLOR) return@second
                    if (oranges > MAX_COLOR) return@second
                    if (blues > MAX_COLOR) return@second
                    if (twoStarCount > MAX_STARS) return@second
                }

                val twoRingComplexity = (first.ring.value + second.ring.value) / 1.2
                minorMap[two] = twoRingComplexity.toInt() + (vectorOne / 2)
                majorMap[two] = twoRingComplexity.toInt() + vectorOne + (twoStarCount / 7)

                if (!constellationOutput.blockedList.contains(two)) myths.add(two)

                constellations.subtract(two).forEach third@ { third ->
                    val three = setOf(first, second, third)

                    val vectorTwo = shortestPath(first, third)
                    val vectorThree = shortestPath(second, third)
                    val threeRingComplexity = (first.ring.value + second.ring.value + third.ring.value) / 1.5
                    val sortedVectors = listOf(vectorOne, vectorTwo, vectorThree).sorted()
                    val shortestDistance = sortedVectors[0] + sortedVectors[1]
                    val pathComplexity = threeRingComplexity + shortestDistance
                    yellows += third.yellow
                    oranges += third.orange
                    blues += third.blue
                    val threeCount = yellows + oranges + blues

                    if (!constellationOutput.allowList.contains(three)) {
                        if (vectorTwo > MAX_VECTOR) return@third
                        if ((vectorOne + vectorTwo) > MAX_DISTANCE) return@third
                        if (vectorThree > MAX_VECTOR) return@third
                        if ((vectorOne + vectorThree) > MAX_DISTANCE) return@third
                        if (pathComplexity > MAX_PATH_COMPLEXITY) return@third
                        if (yellows > MAX_COLOR) return@third
                        if (oranges > MAX_COLOR) return@third
                        if (blues > MAX_COLOR) return@third
                        if (threeCount > MAX_STARS) return@third
                    }

                    minorMap[three] = threeRingComplexity.toInt() + (shortestDistance / 2)
                    majorMap[three] = pathComplexity.toInt() + (threeCount / 7)

                    if (!constellationOutput.blockedList.contains(three)) myths.add(three)
                }
            }
        }
        return myths
    }

    private fun shortestPath(first: Constellation, second: Constellation): Int {
        val vector = setOf(first, second)
        shortestMap[vector]?.let { return it }

        var steps = 1
        var toVisit = first.connections
        while (!toVisit.contains(second)) {
            steps ++
            toVisit = toVisit.flatMap { it.connections }.toSet()
        }

        shortestMap[vector] = steps
        return steps
    }

    private fun printMythData(myths: Set<Set<Constellation>>) {
        val constellationCount = mutableMapOf<Constellation, Int>()
        val sizeCount = mutableMapOf<Int, Int>()
        myths.forEach { myth ->
            sizeCount[myth.size] = (sizeCount[myth.size] ?: 0) + 1
            myth.forEach { constellation ->
                constellationCount[constellation] = (constellationCount[constellation] ?: 0) + 1
                print(constellation.name + " ")
            }
            print(minorMap[myth]?.toString() + " ")
            print(majorMap[myth]?.toString() + " ")
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

    private fun printConstellationData(constellations: Set<Constellation>) {
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

    //TODO could consider more unique point allocations
    private fun pruner(myths: Set<Set<Constellation>>,
                       constellationOutput: ConstellationOutput): Set<Set<Constellation>> {

        val constellations = constellationOutput.allConstellations
        val allCombinations = mutableSetOf<Set<Constellation>>()
        constellations.forEach { first ->
            constellations.subtract(setOf(first)).forEach { second ->
                allCombinations.add(setOf(first, second))
            }
        }

        val prunedMyths = mutableSetOf<Set<Constellation>>()
        prunedMyths.addAll(constellationOutput.allowList)

        // TODO pruning out "covered combinations" eliminates most of the 3 size myths
        // TODO they are more likely to contain one of the "combinations" that have already been seen
        val coveredCombinations = mutableSetOf<Set<Constellation>>()
        coveredCombinations.addAll(constellationOutput.allowList)

        val constellationCount = mutableMapOf<Constellation, Int>()
        val sizeCount = mutableMapOf<Int, Int>()
        allCombinations.forEach combination@ { combination ->
            if (coveredCombinations.contains(combination)) return@combination

            val matchingMyths = mutableSetOf<Set<Constellation>>()
            myths.filter { myth -> myth.containsAll(combination)
                    && !constellationOutput.blockedList.contains(myth)
                    && !prunedMyths.contains(myth)
            }.forEach match@ { match ->
                match.forEach { first ->
                    match.subtract(setOf(first)).forEach { second ->
                        if (coveredCombinations.contains(setOf(first, second))) return@match
                    }
                }
                matchingMyths.add(match)
            }
            if (matchingMyths.isEmpty()) return@combination

            var lowestRating = 0.0
            var bestMyth = emptySet<Constellation>()
            matchingMyths.forEach { match ->
                var cumulativeRating = 0.0
                match.forEach { constellation ->
                    cumulativeRating += ((constellationCount[constellation] ?: 1)
                            / constellation.ring.desiredOccurence)
                }

                if (lowestRating == 0.0
                    || cumulativeRating < lowestRating
                    || (match.size == 3 && bestMyth.size < 3)) {
                    lowestRating = cumulativeRating
                    bestMyth = match
                }
            }
            if (bestMyth.isEmpty()) return@combination

            bestMyth.forEach { constellation ->
                constellationCount[constellation] = (constellationCount[constellation] ?: 0) + 1
                sizeCount[bestMyth.size] = (sizeCount[bestMyth.size] ?: 0) + 1
            }

            if (bestMyth.size == 2) {
                coveredCombinations.add(bestMyth)
            } else if (bestMyth.size > 2) {
                bestMyth.forEach { first ->
                    bestMyth.subtract(setOf(first)).forEach { second ->
                        coveredCombinations.add(setOf(first, second))
                    }
                }
            }

            prunedMyths.add(bestMyth)
        }

        return prunedMyths
    }
}
