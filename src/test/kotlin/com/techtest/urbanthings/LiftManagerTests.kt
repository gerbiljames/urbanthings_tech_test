package com.techtest.urbanthings

import com.techtest.urbanthings.model.Passenger
import com.techtest.urbanthings.state.LiftState
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.LinkedBlockingQueue

class LiftManagerTests {

    @RelaxedMockK
    private lateinit var mockLiftState: LiftState

    @Before
    fun before() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    fun testAdvance_exit() {

        val passengers = LinkedBlockingQueue<Passenger>(listOf(Passenger(200, 4)))

        every { mockLiftState.needsExit } returns true
        every { mockLiftState.exit() } just Runs

        val liftManager = LiftManager(mockLiftState, passengers)
        val returned = liftManager.advance()

        verify { mockLiftState.needsExit }
        verify { mockLiftState.exit() }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_board() {

        val passenger = Passenger(200, 4)
        val passengers = LinkedBlockingQueue<Passenger>(listOf(passenger))

        every { mockLiftState.currentFloor } returns 1
        every { mockLiftState.needsExit } returns false
        every { mockLiftState.canBoard(passenger) } returns true
        every { mockLiftState.board(passenger) } just Runs

        val liftManager = LiftManager(mockLiftState, passengers)
        val returned = liftManager.advance()

        verify { mockLiftState.canBoard(passenger) }
        verify { mockLiftState.board(passenger) }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_move() {

        val passenger = Passenger(200, 4)
        val passengers = LinkedBlockingQueue<Passenger>(listOf(passenger))

        every { mockLiftState.currentFloor } returns 2
        every { mockLiftState.needsExit } returns false
        every { mockLiftState.canBoard(passenger) } returns false
        every { mockLiftState.shouldMoveIn() } returns LiftState.Direction.UP
        every { mockLiftState.move(LiftState.Direction.UP) } just Runs

        val liftManager = LiftManager(mockLiftState, passengers)
        val returned = liftManager.advance()

        verify { mockLiftState.shouldMoveIn() }
        verify { mockLiftState.move(LiftState.Direction.UP) }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_complete() {

        val passengers = LinkedBlockingQueue<Passenger>()

        every { mockLiftState.needsExit } returns false
        every { mockLiftState.currentFloor } returns 1
        every { mockLiftState.shouldMoveIn() } returns LiftState.Direction.NONE

        val liftManager = LiftManager(mockLiftState, passengers)
        val returned = liftManager.advance()

        assertEquals(false, returned)
    }
}