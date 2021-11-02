package model

fun createConstellations(): ConstellationOutput {

    // Create all constellations and add them to the set
    val wolf = Constellation("Wolf", Ring.Starter, limit = 3, yellow = 1, orange = 1, blue = 1)
    val cat = Constellation("Cat", Ring.First, yellow = 1, orange = 2, blue = 2)
    val mouse = Constellation("Mouse", Ring.FirstSmall, limit = 2, yellow = 1)
    val fish = Constellation("Fish", Ring.Starter, limit = 3, yellow = 1, orange = 1, blue = 1)
    val boat = Constellation("Boat", Ring.First, yellow = 1, orange = 2, blue = 1)
    val anchor = Constellation("Anchor", Ring.FirstSmall, limit = 3, yellow = 1, orange = 1)
    val arch = Constellation("Arch", Ring.Starter, limit = 3, yellow = 1, orange = 1, blue = 1)
    val friends = Constellation("Friends", Ring.First, yellow = 2, orange = 1, blue = 1)
    val mountain = Constellation("Mountain", Ring.Starter, limit = 3, yellow = 1, orange = 1, blue = 1)
    val tree = Constellation("Tree", Ring.First, yellow = 2, orange = 3)
    val nut = Constellation("Nut", Ring.FirstSmall, limit = 2, orange = 1)
    val basket = Constellation("Basket", Ring.Second, yellow = 2, blue = 2)
    val fruit = Constellation("Fruit", Ring.SecondSmall, limit = 3, orange = 2)
    val snake = Constellation("Snake", Ring.Second, yellow = 3, orange = 2, blue = 1)
    val scales = Constellation("Scales", Ring.Second, yellow = 2, orange = 2)
    val fountain = Constellation("Fountain", Ring.Second, yellow = 1, orange = 1, blue = 2)
    val coin = Constellation("Coin", Ring.SecondSmall, limit = 2, blue = 1)
    val dancer = Constellation("Dancer", Ring.Second, yellow = 1, orange = 1, blue = 3)
    val ladle = Constellation("Ladle", Ring.Second, yellow = 2, orange = 2, blue = 1)
    val flower = Constellation("Flower", Ring.Second, yellow = 2, orange = 1, blue = 3)
    val seed = Constellation("Seed", Ring.SecondSmall, limit = 3, orange = 1, blue = 1)
    val eye = Constellation("Eye", Ring.Second, orange = 2, blue = 3)
    val hedgehog = Constellation("Hedgehog", Ring.Second, yellow = 2, orange = 2, blue = 2)
    val leaf = Constellation("Leaf", Ring.Second, yellow = 1, orange = 2, blue = 2)
    val fire = Constellation("Fire", Ring.Third, limit = 3, yellow = 3, orange = 1)
    val spark = Constellation("Spark", Ring.ThirdSmall, limit = 2, yellow = 2)
    val mask = Constellation("Mask", Ring.Third, limit = 3, orange = 1, blue = 2)
    val teapot = Constellation("Teapot", Ring.Third, limit = 3, yellow = 2, orange = 1, blue = 1)
    val cup = Constellation("Cup", Ring.ThirdSmall, limit = 2, blue = 2)
    val satchel = Constellation("Satchel", Ring.Third, limit = 3, yellow = 1, orange = 2)
    val chair = Constellation("Chair", Ring.Third, limit = 3, orange = 2, blue = 2)
    val musician = Constellation("Musician", Ring.Third, limit = 3, yellow = 1, orange = 3)
    val song = Constellation("Song", Ring.ThirdSmall, limit = 2, yellow = 1, blue = 1)
    val spider = Constellation("Spider", Ring.Third, limit = 3, yellow = 3, blue = 1)
    val bird = Constellation("Bird", Ring.Third, limit = 3, yellow = 1, orange = 1, blue = 2)
    val snail = Constellation("Snail", Ring.Third, limit = 3, yellow = 2, blue = 1)
    val book = Constellation("Book", Ring.Third, limit = 3, orange = 1, blue = 3)

    // Add all connections to each constellation
    wolf.connections = setOf(fish, arch, mountain, tree, cat, basket)
    cat.connections = setOf(wolf, fish, mouse, basket, snake, scales)
    mouse.connections = setOf(cat)
    fish.connections = setOf(arch, mountain, wolf, cat, boat, scales)
    boat.connections = setOf(fish, arch, anchor, fountain)
    anchor.connections = setOf(boat)
    arch.connections = setOf(mountain, wolf, fish, boat, friends, dancer)
    friends.connections = setOf(arch, mountain, ladle, flower)
    mountain.connections = setOf(wolf, fish, arch, friends, tree, eye)
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
    flower.connections = setOf(friends, ladle, eye, musician, spider, seed)
    seed.connections = setOf(flower)
    eye.connections = setOf(mountain, flower, hedgehog, spider, bird)
    hedgehog.connections = setOf(tree, eye, leaf, bird, snail)
    leaf.connections = setOf(tree, hedgehog, basket, snail, book)
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

    // Favorites
    allowList.add(setOf(coin, cup))

    // Create blocked list
    val blockedList = mutableSetOf<Set<Constellation>>()
    blockedList.add(setOf(wolf, arch))
    blockedList.add(setOf(fish, mountain))

    return ConstellationOutput(constellations, allowList, blockedList)
}
