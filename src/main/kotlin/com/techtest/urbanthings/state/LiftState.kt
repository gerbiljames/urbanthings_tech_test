package com.techtest.urbanthings.state

import com.techtest.urbanthings.model.Lift
import com.techtest.urbanthings.model.Passenger

/**
 * Holds the state of a lift and provides functions to manipulate it.
 */
open class LiftState(private val lift: Lift) {

    /**
     * The floor this Lift is currently at.
     */
    var currentFloor = 1

    private var passengers = mutableListOf<Passenger>()

    private val currentWeightCapacity get() = passengers.sumBy { it.weight }

    private val currentPassengerCapacity get() = passengers.size

    /**
     * Checks if it is possible to board a given passenger.
     */
    fun canBoard(passenger: Passenger): Boolean =
        currentPassengerCapacity + 1 <= lift.passengerCapacity &&
                currentWeightCapacity + passenger.weight <= lift.weightCapacity

    /**
     * Boards a given passenger.
     */
    fun board(passenger: Passenger) {

        if (!canBoard(passenger)) throw IllegalArgumentException("Over capacity!")

        passengers.add(passenger)
    }

    /**
     * Checks if passengers need to exit the Lift at the current floor.
     */
    val needsExit get() = passengers.find { it.floor == currentFloor } != null

    /**
     * Exits passengers that require it at the current floor.
     */
    fun exit() {
        passengers.removeIf { it.floor == currentFloor }
    }

    /**
     * Determines the next move direction of the Lift.
     */
    fun shouldMoveIn(): Direction {
        if (currentFloor == 1 && passengers.isEmpty() || needsExit) {
            return Direction.NONE
        }

        if (passengers.isEmpty()) {
            return Direction.DOWN
        }

        return Direction.UP
    }

    /**
     * Moves the Lift in a given direction.
     */
    fun move(direction: Direction) {

        when (direction) {
            Direction.UP -> currentFloor++
            Direction.DOWN -> currentFloor--
            Direction.NONE -> Unit
        }
    }

    /**
     * Represents the possible movement directions of a Lift.
     */
    enum class Direction {
        UP, DOWN, NONE
    }
}