package com.techtest.urbanthings.state

import com.techtest.urbanthings.model.Lift
import com.techtest.urbanthings.model.Passenger

class ExpressLiftState(lift: Lift): LiftState(lift) {

    override fun canBoard(passenger: Passenger): Boolean {
        return super.canBoard(passenger) && passenger.floor % 2 == 0
    }

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