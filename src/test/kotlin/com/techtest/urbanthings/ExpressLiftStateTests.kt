package com.techtest.urbanthings

import com.techtest.urbanthings.model.Lift
import com.techtest.urbanthings.model.Passenger
import com.techtest.urbanthings.state.ExpressLiftState
import com.techtest.urbanthings.state.LiftState
import org.junit.Assert
import org.junit.Test

class ExpressLiftStateTests {

    @Test
    fun testCanBoard_allowed() {
        val lift = Lift(200, 2)
        val liftState = ExpressLiftState(lift)

        Assert.assertEquals(true, liftState.canBoard(Passenger(50, 2)))
    }

    @Test
    fun testCanBoard_denied_notExpress() {
        val lift = Lift(200, 2)
        val liftState = ExpressLiftState(lift)

        Assert.assertEquals(false, liftState.canBoard(Passenger(50, 3)))
    }

    @Test
    fun testMove_floor1to2() {
        val lift = Lift(2000, 2)

        val liftState = ExpressLiftState(lift)

        liftState.currentFloor = 1

        liftState.move(LiftState.Direction.UP)

        Assert.assertEquals(2, liftState.currentFloor)
    }

    @Test
    fun testMove_floor2to1() {
        val lift = Lift(2000, 2)

        val liftState = ExpressLiftState(lift)

        liftState.currentFloor = 2

        liftState.move(LiftState.Direction.DOWN)

        Assert.assertEquals(1, liftState.currentFloor)
    }

    @Test
    fun testMove_UP() {
        val lift = Lift(2000, 2)

        val liftState = ExpressLiftState(lift)

        liftState.currentFloor = 2

        liftState.move(LiftState.Direction.UP)

        Assert.assertEquals(4, liftState.currentFloor)
    }

    @Test
    fun testMove_DOWN() {
        val lift = Lift(2000, 2)

        val liftState = ExpressLiftState(lift)

        liftState.currentFloor = 6

        liftState.move(LiftState.Direction.DOWN)

        Assert.assertEquals(4, liftState.currentFloor)
    }
}