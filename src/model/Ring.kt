package model

enum class Ring(val value: Double, val desiredOccurrence: Double, val isSmall: Boolean) {
    Starter(1.0, 1.5, false),
    First(1.5, 2.0, false),
    FirstSmall(1.8, 0.7, true),
    Second(2.5, 3.0, false),
    SecondSmall(2.8, 0.6, true),
    Third(4.0, 2.5, false),
    ThirdSmall(4.3, 0.5, true)
}