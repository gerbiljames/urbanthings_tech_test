package com.techtest.urbanthings.state

import com.techtest.urbanthings.model.Lift
import com.techtest.urbanthings.model.Passenger

/**
 * Holds the state of an express lift and provides functions to manipulate it.
 *
 * An express lift is a lift which only serves even floors (and floor 1).
 */
class ExpressLiftState(lift: Lift): LiftState(lift) {

    /**
     * Checks if it is possible to board a given passenger.
     */
    override fun canBoard(passenger: Passenger): Boolean {
        return super.canBoard(passenger) && passenger.floor % 2 == 0
    }
    
    /**
     * Moves the Lift in a given direction.
     */
    override fun move(direction: Direction) {
        // There are special cases for movement between floors 1 and 2.
        if (currentFloor == 2 && direction == Direction.DOWN) {
            currentFloor--
            return
        }

        if (currentFloor == 1 && direction == Direction.UP) {
            currentFloor++
            return
        }

        when (direction) {
            Direction.UP -> currentFloor += 2
            Direction.DOWN -> currentFloor -= 2
            Direction.NONE -> Unit
        }
    }
}