package com.techtest.urbanthings

import com.techtest.urbanthings.model.Lift
import com.techtest.urbanthings.model.Passenger
import com.techtest.urbanthings.state.LiftState
import org.junit.Assert.assertEquals
import org.junit.Test

class LiftStateTests {

    @Test
    fun testCanBoard_allowed() {

        val lift = Lift(200, 2)
        val liftState = LiftState(lift)

        assertEquals(true, liftState.canBoard(Passenger(50, 3)))
    }

    @Test
    fun testCanBoard_denied_weight() {
        val lift = Lift(200, 2)
        val liftState = LiftState(lift)

        liftState.board(Passenger(160, 2))

        assertEquals(false, liftState.canBoard(Passenger(50, 3)))
    }

    @Test
    fun testCanBoard_denied_capacity() {
        val lift = Lift(2000, 2)
        val liftState = LiftState(lift)

        liftState.board(Passenger(160, 2))
        liftState.board(Passenger(150, 2))

        assertEquals(false, liftState.canBoard(Passenger(50, 3)))
    }

    @Test
    fun testNeedsExit_true() {
        val lift = Lift(2000, 2)
        val liftState = LiftState(lift)

        liftState.board(Passenger(160, 3))
        liftState.board(Passenger(150, 3))

        liftState.currentFloor = 3

        assertEquals(true, liftState.needsExit)
    }

    @Test
    fun testNeedsExit_false() {
        val lift = Lift(2000, 2)
        val liftState = LiftState(lift)

        liftState.board(Passenger(160, 5))
        liftState.board(Passenger(150, 5))

        liftState.currentFloor = 3

        assertEquals(false, liftState.needsExit)
    }

    @Test
    fun testExit() {
        val lift = Lift(2000, 2)

        val liftState = LiftState(lift)

        liftState.board(Passenger(160, 5))
        liftState.board(Passenger(150, 3))
        liftState.currentFloor = 3

        assertEquals(true, liftState.needsExit)

        liftState.exit()

        assertEquals(false, liftState.needsExit)
    }

    @Test
    fun testShouldMoveIn_UP_floor1() {
        val lift = Lift(2000, 2)

        val liftState = LiftState(lift)

        liftState.board(Passenger(160, 2))
        liftState.board(Passenger(150, 3))

        liftState.currentFloor = 1

        assertEquals(LiftState.Direction.UP, liftState.shouldMoveIn())
    }

    @Test
    fun testShouldMoveIn_NONE_exit() {
        val lift = Lift(2000, 2)

        val liftState = LiftState(lift)

        liftState.board(Passenger(160, 2))
        liftState.board(Passenger(150, 3))

        liftState.currentFloor = 3

        assertEquals(LiftState.Direction.NONE, liftState.shouldMoveIn())
    }

    @Test
    fun testShouldMoveIn_DOWN() {
        val lift = Lift(2000, 2)

        val liftState = LiftState(lift)

        liftState.currentFloor = 3

        assertEquals(LiftState.Direction.DOWN, liftState.shouldMoveIn())
    }

    @Test
    fun testShouldMoveIn_NONE() {
        val lift = Lift(2000, 2)

        val liftState = LiftState(lift)

        liftState.currentFloor = 1

        assertEquals(LiftState.Direction.NONE, liftState.shouldMoveIn())
    }

    @Test
    fun testMove_UP() {
        val lift = Lift(2000, 2)

        val liftState = LiftState(lift)

        liftState.currentFloor = 1

        liftState.move(LiftState.Direction.UP)

        assertEquals(2, liftState.currentFloor)
    }

    @Test
    fun testMove_DOWN() {
        val lift = Lift(2000, 2)

        val liftState = LiftState(lift)

        liftState.currentFloor = 2

        liftState.move(LiftState.Direction.DOWN)

        assertEquals(1, liftState.currentFloor)
    }
}