package model

data class Constellation (
    val name: String,
    val ring: Ring,
    val limit: Int = 6,
    val yellow: Int = 0,
    val orange: Int = 0,
    val blue: Int = 0
) {
    var connections: Set<Constellation> = emptySet()
}
