package model

enum class Ring(val value: Double, val desiredOccurrence: Double, val isSmall: Boolean, val isOuter: Boolean) {
    Starter(1.0, 1.5, false, false),
    First(1.5, 2.0, false, false),
    FirstSmall(1.8, 0.7, true, false),
    Second(2.5, 3.0, false, false),
    SecondSmall(2.8, 0.6, true, true),
    Third(4.0, 2.5, false, true),
    ThirdSmall(4.3, 0.5, true, true)
}