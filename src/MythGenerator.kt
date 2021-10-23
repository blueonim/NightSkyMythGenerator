private const val MAX_VECTOR = 4
private const val MAX_DISTANCE = 4
private const val MAX_PATH_COMPLEXITY = 10
private const val RING_MULTIPLIER = 2
private const val MAX_STARS = 13
private const val MAX_COLOR = 6

class MythGenerator {

    private val shortestMap: MutableMap<Set<Constellation>, Int> = mutableMapOf()

    fun generate() {
        val constellations = setupConstellations()
        val myths = createMyths(constellations)

        val constellationCount: MutableMap<Constellation, Int> = mutableMapOf()
        myths.forEach { myth ->
            myth.forEach { constellation ->
                constellationCount[constellation] = (constellationCount[constellation] ?: 0) + 1
                print(constellation.name + " ")
            }
            println()
        }
        println()
        println("Total Myths: " + myths.size)
        println()

        constellations.forEach { println(it.name + ": " + constellationCount[it]) }

        //TODO look for similarities - cut myths that are similar and have one of the high count constellations (cut myth with higher "total" counts)
        //TODO prefer to have more constellation counts of mid ring constellations
    }

    private fun setupConstellations(): Set<Constellation> {
        val constellations = mutableSetOf<Constellation>()

        // Create all constellations and add them to the set
        val wolf = Constellation("Wolf", 1.0, yellow = 1, orange = 1, blue = 1)
        val cat = Constellation("Cat", 1.0, yellow = 1, orange = 2, blue = 2)
        val mouse = Constellation("Mouse", 1.5, yellow = 1)
        val fish = Constellation("Fish", 1.0, yellow = 1, orange = 1, blue = 1)
        val boat = Constellation("Boat", 1.0, yellow = 1, orange = 2, blue = 1)
        val anchor = Constellation("Anchor", 1.5, yellow = 1, orange = 1)
        val arch = Constellation("Arch", 1.0, yellow = 1, orange = 1, blue = 1)
        val friends = Constellation("Friends", 1.0, yellow = 2, orange = 1, blue = 1)
        val mountain = Constellation("Mountain", 1.0, yellow = 1, orange = 1, blue = 1)
        val tree = Constellation("Tree", 1.0, yellow = 1, orange = 1, blue = 2)
        val nut = Constellation("Nut", 1.5, orange = 1)
        val basket = Constellation("Basket", 2.0, yellow = 2, blue = 2)
        val fruit = Constellation("Fruit", 2.5, yellow = 1, blue = 1)
        val snake = Constellation("Snake", 2.0, yellow = 3, orange = 2)
        val scales = Constellation("Scales", 2.0, orange = 1, blue = 3)
        val fountain = Constellation("Fountain", 2.0, yellow = 2, orange = 3)
        val coin = Constellation("Coin", 2.5, blue = 1)
        val dancer = Constellation("Dancer", 2.0, yellow = 1, orange = 3)
        val ladle = Constellation("Ladle", 2.0, yellow = 2, blue = 1)
        val flower = Constellation("Flower", 2.0, orange = 2, blue = 2)
        val seed = Constellation("Seed", 2.5, orange = 1, blue = 1)
        val eye = Constellation("Eye", 2.0, orange = 2, blue = 3)
        val hedgehog = Constellation("Hedgehog", 2.0, yellow = 3, orange = 1)
        val leaf = Constellation("Leaf", 2.0, orange = 1, blue = 2)
        val fire = Constellation("Fire", 3.0, yellow = 1, orange = 3, blue = 2)
        val spark = Constellation("Spark", 3.5, yellow = 2)
        val mask = Constellation("Mask", 3.0, yellow = 2, orange = 1, blue = 2)
        val teapot = Constellation("Teapot", 3.0, yellow = 2, orange = 1, blue = 1)
        val cup = Constellation("Cup", 3.5, blue = 2)
        val satchel = Constellation("Satchel", 3.0, yellow = 1, orange = 2)
        val chair = Constellation("Chair", 3.0, yellow = 2, orange = 2)
        val musician = Constellation("Musician", 3.0, yellow = 1, orange = 2, blue = 2)
        val song = Constellation("Song", 3.5, orange = 2)
        val spider = Constellation("Spider", 3.0, yellow = 2, orange = 2, blue = 2)
        val bird = Constellation("Bird", 3.0, yellow = 2, orange = 2, blue = 1)
        val snail = Constellation("Snail", 3.0, yellow = 1, orange = 1, blue = 2)
        val book = Constellation("Book", 3.0, yellow = 3, blue = 1)

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

        return constellations
    }

    private fun createMyths(constellations: Set<Constellation>): Set<Set<Constellation>> {
        val myths = mutableSetOf<Set<Constellation>>()
        constellations.forEach { first ->
            constellations.subtract(setOf(first)).forEach second@ { second ->
                val vectorOne = shortestPath(first, second)
                if (vectorOne > MAX_VECTOR) return@second

                var yellows = first.yellow + second.yellow
                if (yellows > MAX_COLOR) return@second

                var oranges = first.orange + second.orange
                if (oranges > MAX_COLOR) return@second

                var blues = first.blue + second.blue
                if (blues > MAX_COLOR) return@second

                if ((yellows + oranges + blues) > MAX_STARS) return@second

                val two = setOf(first, second)
                myths.add(two)
                constellations.subtract(two).forEach third@ { third ->
                    val vectorTwo = shortestPath(first, third)
                    if (vectorTwo > MAX_VECTOR) return@third
                    if ((vectorOne + vectorTwo) > MAX_DISTANCE) return@third

                    val vectorThree = shortestPath(second, third)
                    if (vectorThree > MAX_VECTOR) return@third
                    if ((vectorOne + vectorThree) > MAX_DISTANCE) return@third

                    val avgRing = (first.ring + second.ring + third.ring) / 3
                    val sortedVectors = listOf(vectorOne, vectorTwo, vectorThree).sorted()
                    val shortestDistance = sortedVectors[0] + sortedVectors[1]
                    val pathComplexity = (avgRing * RING_MULTIPLIER) + shortestDistance
                    if (pathComplexity > MAX_PATH_COMPLEXITY) return@third

                    yellows += third.yellow
                    if (yellows > MAX_COLOR) return@third

                    oranges += third.orange
                    if (oranges > MAX_COLOR) return@third

                    blues += third.blue
                    if (blues > MAX_COLOR) return@third

                    if ((yellows + oranges + blues) > MAX_STARS) return@third

                    myths.add(setOf(first, second, third))
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
}
