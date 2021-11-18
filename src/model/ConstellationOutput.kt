package model

data class ConstellationOutput (
    val allConstellations: Set<Constellation>,
    val starters: Set<Constellation>,
    val allowList: Set<Set<Constellation>>,
    val blockedList: Set<Set<Constellation>>
)
