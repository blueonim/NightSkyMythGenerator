package model

enum class Ring(val value: Double, val desiredOccurrence: Double) {
    Starter(1.0, 1.5),
    First(1.5, 2.0),
    FirstSmall(1.8, 0.7),
    Second(2.5, 3.0),
    SecondSmall(2.8, 0.6),
    Third(4.0, 2.5),
    ThirdSmall(4.3, 0.5)
}