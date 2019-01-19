package com.techtest.urbanthings.model

data class Lift(val capacity: Int) {
    var passengers = mutableListOf<Passenger>()
    var currentFloor = 1
}