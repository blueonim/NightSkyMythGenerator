package model

fun createConstellations(): ConstellationOutput {

    // Create all constellations and add them to the set
    val orchid = Constellation("Orchid", Ring.Starter, orange = 2, yellow = 1, blue = 1)
    val tortoise = Constellation("Tortoise", Ring.First, orange = 2, yellow = 1)
    val snapdragon = Constellation("Snapdragon", Ring.Starter, orange = 1, yellow = 2, blue = 1)
    val sock = Constellation("Sock", Ring.First, orange = 1, yellow = 1, blue = 2)
    val sailboat = Constellation("Sailboat", Ring.Starter, orange = 2, yellow = 1, blue = 1)
    val salamander = Constellation("Salamander", Ring.First, orange = 1, yellow = 2)
    val seed = Constellation("Seed", Ring.FirstSmall, orange = 1, blue = 1)
    val lantern = Constellation("Lantern", Ring.FirstSmall, orange = 1, yellow = 1)
    val minnow = Constellation("Minnow", Ring.FirstSmall, orange = 2, blue = 1)
    val storyteller = Constellation("Storyteller", Ring.Second, yellow = 2, blue = 2)
    val seashell = Constellation("Seashell", Ring.Second, orange = 3, blue = 1)
    val comb = Constellation("Comb", Ring.Second, yellow = 1, blue = 2)
    val magpie = Constellation("Magpie", Ring.Second, yellow = 3, blue = 1)
    val dancer = Constellation("Dancer", Ring.Second, yellow = 1, blue = 3)
    val radish = Constellation("Radish", Ring.Second, orange = 2, blue = 2)
    val otter = Constellation("Otter", Ring.Second, orange = 3, blue = 1)
    val hedgehog = Constellation("Hedgehog", Ring.Second, orange = 2, yellow = 2)
    val thread = Constellation("Thread", Ring.Second, orange = 2, blue = 1)
    val mushroom = Constellation("Mushroom", Ring.SecondSmall, blue = 2)
    val child = Constellation("Child", Ring.SecondSmall, yellow = 2)
    val keyring = Constellation("Keyring", Ring.SecondSmall, yellow = 1, blue = 1)
    val fruit = Constellation("Fruit", Ring.SecondSmall, orange = 2)
    val thistle = Constellation("Thistle", Ring.Third, orange = 1, yellow = 1, blue = 1)
    val guitar = Constellation("Guitar", Ring.Third, yellow = 2, blue = 1)
    val teapot = Constellation("Teapot", Ring.Third, orange = 1, yellow = 2, blue = 1)
    val moth = Constellation("Moth", Ring.Third, orange = 1, blue = 2)
    val musician = Constellation("Musician", Ring.Third, yellow = 1, blue = 2)
    val teacher = Constellation("Teacher", Ring.Third, orange = 1, yellow = 2, blue = 1)
    val willow = Constellation("Willow", Ring.Third, orange = 2, yellow = 1)
    val bonfire = Constellation("Bonfire", Ring.Third, orange = 1, yellow = 3)
    val snail = Constellation("Snail", Ring.Third, yellow = 2, blue = 1)
    val performer = Constellation("Performer", Ring.Third, orange = 1, blue = 3)

    // Add all connections to each constellation
    orchid.connections = setOf(snapdragon, sailboat,tortoise, salamander, seed)
    tortoise.connections = setOf(snapdragon, orchid, storyteller, seashell)
    snapdragon.connections = setOf(sailboat, orchid, sock, tortoise, comb, magpie)
    sock.connections = setOf(sailboat, snapdragon, lantern, dancer, radish)
    sailboat.connections = setOf(orchid, snapdragon, salamander, sock, minnow)
    salamander.connections = setOf(orchid, sailboat, hedgehog, thread)
    seed.connections = setOf(orchid, storyteller)
    lantern.connections = setOf(sock, magpie)
    minnow.connections = setOf(sailboat, otter)
    storyteller.connections = setOf(seed, tortoise, thread, seashell, fruit, thistle)
    seashell.connections = setOf(tortoise, storyteller, comb, mushroom, guitar)
    comb.connections = setOf(snapdragon, seashell, magpie)
    magpie.connections = setOf(snapdragon, lantern, comb, dancer, teapot, moth)
    dancer.connections = setOf(sock, magpie, radish, moth, musician)
    radish.connections = setOf(sock, dancer, otter, child, teacher, willow)
    otter.connections = setOf(minnow, radish, hedgehog, keyring, willow)
    hedgehog.connections = setOf(salamander, otter, thread, bonfire, snail)
    thread.connections = setOf(salamander, hedgehog, storyteller, snail, performer)
    mushroom.connections = setOf(seashell, teapot)
    child.connections = setOf(radish, musician)
    keyring.connections = setOf(otter, bonfire)
    fruit.connections = setOf(storyteller, performer)
    thistle.connections = setOf(storyteller, performer, guitar)
    guitar.connections = setOf(seashell, thistle, teapot)
    teapot.connections = setOf(magpie, mushroom, guitar, moth)
    moth.connections = setOf(magpie, dancer, teapot, musician)
    musician.connections = setOf(dancer, child, moth, teacher)
    teacher.connections = setOf(radish, musician, willow)
    willow.connections = setOf(radish, otter, teacher, bonfire)
    bonfire.connections = setOf(hedgehog, keyring, willow, snail)
    snail.connections = setOf(hedgehog, thread, bonfire, performer)
    performer.connections = setOf(thread, fruit, snail, thistle)

    // Add constellations to the set
    val constellations = mutableSetOf<Constellation>()
    constellations.add(orchid)
    constellations.add(tortoise)
    constellations.add(snapdragon)
    constellations.add(sock)
    constellations.add(sailboat)
    constellations.add(salamander)
    constellations.add(seed)
    constellations.add(lantern)
    constellations.add(minnow)
    constellations.add(storyteller)
    constellations.add(seashell)
    constellations.add(comb)
    constellations.add(magpie)
    constellations.add(dancer)
    constellations.add(radish)
    constellations.add(otter)
    constellations.add(hedgehog)
    constellations.add(thread)
    constellations.add(mushroom)
    constellations.add(child)
    constellations.add(keyring)
    constellations.add(fruit)
    constellations.add(thistle)
    constellations.add(guitar)
    constellations.add(teapot)
    constellations.add(moth)
    constellations.add(musician)
    constellations.add(teacher)
    constellations.add(willow)
    constellations.add(bonfire)
    constellations.add(snail)
    constellations.add(performer)

    // Create starter list
    val starters = mutableSetOf<Constellation>()
    starters.add(orchid)
    starters.add(snapdragon)
    starters.add(sailboat)

    // Create allow list
    val allowList = mutableSetOf<Set<Constellation>>()

    // Create blocked list
    val blockedList = mutableSetOf<Set<Constellation>>()

    return ConstellationOutput(constellations, starters, allowList, blockedList)
}
