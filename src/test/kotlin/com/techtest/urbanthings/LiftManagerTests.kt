package com.techtest.urbanthings

import com.techtest.urbanthings.model.Passenger
import com.techtest.urbanthings.state.ExpressLiftState
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

    @RelaxedMockK
    private lateinit var mockLiftState2: LiftState

    @RelaxedMockK
    private lateinit var mockExpressLiftState: ExpressLiftState

    @Before
    fun before() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    fun testAdvance_exit() {

        val passengers = LinkedBlockingQueue<Passenger>(listOf(Passenger(200, 3)))

        every { mockLiftState.needsExit } returns true
        every { mockLiftState.exit() } just Runs

        val liftManager = LiftManager(listOf(mockLiftState), passengers)
        val returned = liftManager.advance()

        verify { mockLiftState.needsExit }
        verify { mockLiftState.exit() }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_board() {

        val passenger = Passenger(200, 3)
        val passengers = LinkedBlockingQueue<Passenger>(listOf(passenger))

        every { mockLiftState.currentFloor } returns 1
        every { mockLiftState.needsExit } returns false
        every { mockLiftState.canBoard(passenger) } returns true
        every { mockLiftState.board(passenger) } just Runs

        val liftManager = LiftManager(listOf(mockLiftState), passengers)
        val returned = liftManager.advance()

        verify { mockLiftState.canBoard(passenger) }
        verify { mockLiftState.board(passenger) }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_move() {

        val passenger = Passenger(200, 3)
        val passengers = LinkedBlockingQueue<Passenger>(listOf(passenger))

        every { mockLiftState.currentFloor } returns 2
        every { mockLiftState.needsExit } returns false
        every { mockLiftState.canBoard(passenger) } returns false
        every { mockLiftState.shouldMoveIn() } returns LiftState.Direction.UP
        every { mockLiftState.move(LiftState.Direction.UP) } just Runs

        val liftManager = LiftManager(listOf(mockLiftState), passengers)
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

        val liftManager = LiftManager(listOf(mockLiftState), passengers)
        val returned = liftManager.advance()

        assertEquals(false, returned)
    }

    @Test
    fun testAdvance_multiLift_exit() {
        val passengers = LinkedBlockingQueue<Passenger>(listOf(Passenger(200, 3)))

        every { mockLiftState.needsExit } returns true
        every { mockLiftState.exit() } just Runs
        every { mockLiftState2.needsExit } returns true
        every { mockLiftState2.exit() } just Runs

        val liftManager = LiftManager(listOf(mockLiftState, mockLiftState2), passengers)
        val returned = liftManager.advance()

        verify { mockLiftState.needsExit }
        verify { mockLiftState.exit() }
        verify { mockLiftState2.needsExit }
        verify { mockLiftState2.exit() }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_multiLift_board() {

        val passenger = Passenger(200, 3)
        val passengers = LinkedBlockingQueue<Passenger>(listOf(passenger))

        every { mockLiftState.currentFloor } returns 5
        every { mockLiftState.needsExit } returns false
        every { mockLiftState.canBoard(passenger) } returns false
        every { mockLiftState2.currentFloor } returns 1
        every { mockLiftState2.needsExit } returns false
        every { mockLiftState2.canBoard(passenger) } returns true
        every { mockLiftState2.board(passenger) } just Runs

        val liftManager = LiftManager(listOf(mockLiftState, mockLiftState2), passengers)
        val returned = liftManager.advance()

        verify { mockLiftState2.canBoard(passenger)}
        verify { mockLiftState2.board(passenger) }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_multiLift_move() {

        val passenger = Passenger(200, 3)
        val passengers = LinkedBlockingQueue<Passenger>(listOf(passenger))

        every { mockLiftState.currentFloor } returns 2
        every { mockLiftState.needsExit } returns false
        every { mockLiftState.canBoard(passenger) } returns false
        every { mockLiftState.shouldMoveIn() } returns LiftState.Direction.UP
        every { mockLiftState.move(LiftState.Direction.UP) } just Runs
        every { mockLiftState2.currentFloor } returns 2
        every { mockLiftState2.needsExit } returns false
        every { mockLiftState2.canBoard(passenger) } returns false
        every { mockLiftState2.shouldMoveIn() } returns LiftState.Direction.DOWN
        every { mockLiftState2.move(LiftState.Direction.DOWN) } just Runs

        val liftManager = LiftManager(listOf(mockLiftState, mockLiftState2), passengers)
        val returned = liftManager.advance()

        verify { mockLiftState.shouldMoveIn() }
        verify { mockLiftState.move(LiftState.Direction.UP) }
        verify { mockLiftState2.shouldMoveIn() }
        verify { mockLiftState2.move(LiftState.Direction.DOWN) }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_multiLift_complete() {

        val passengers = LinkedBlockingQueue<Passenger>()

        every { mockLiftState.needsExit } returns false
        every { mockLiftState.currentFloor } returns 1
        every { mockLiftState.shouldMoveIn() } returns LiftState.Direction.NONE
        every { mockLiftState2.needsExit } returns false
        every { mockLiftState2.currentFloor } returns 1
        every { mockLiftState2.shouldMoveIn() } returns LiftState.Direction.NONE

        val liftManager = LiftManager(listOf(mockLiftState, mockLiftState2), passengers)
        val returned = liftManager.advance()

        assertEquals(false, returned)
    }

    @Test
    fun testAdvance_express_exit() {
        val passengers = LinkedBlockingQueue<Passenger>(listOf(Passenger(200, 2)))

        every { mockExpressLiftState.needsExit } returns true
        every { mockExpressLiftState.exit() } just Runs

        val liftManager = LiftManager(listOf(mockExpressLiftState), passengers)
        val returned = liftManager.advance()

        verify { mockExpressLiftState.needsExit }
        verify { mockExpressLiftState.exit() }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_express_board() {

        val passenger = Passenger(200, 2)
        val passengers = LinkedBlockingQueue<Passenger>(listOf(passenger))

        every { mockExpressLiftState.currentFloor } returns 1
        every { mockExpressLiftState.needsExit } returns false
        every { mockExpressLiftState.canBoard(passenger) } returns true
        every { mockExpressLiftState.board(passenger) } just Runs

        val liftManager = LiftManager(listOf(mockExpressLiftState), passengers)
        val returned = liftManager.advance()

        verify { mockExpressLiftState.canBoard(passenger) }
        verify { mockExpressLiftState.board(passenger) }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_express_move() {

        val passenger = Passenger(200, 4)
        val passengers = LinkedBlockingQueue<Passenger>(listOf(passenger))

        every { mockExpressLiftState.currentFloor } returns 2
        every { mockExpressLiftState.needsExit } returns false
        every { mockExpressLiftState.canBoard(passenger) } returns false
        every { mockExpressLiftState.shouldMoveIn() } returns LiftState.Direction.UP
        every { mockExpressLiftState.move(LiftState.Direction.UP) } just Runs

        val liftManager = LiftManager(listOf(mockExpressLiftState), passengers)
        val returned = liftManager.advance()

        verify { mockExpressLiftState.shouldMoveIn() }
        verify { mockExpressLiftState.move(LiftState.Direction.UP) }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_regularAndExpress_exit() {
        val passengers = LinkedBlockingQueue<Passenger>(listOf(
            Passenger(40, 3),
            Passenger(50, 4))
        )

        every { mockLiftState.needsExit } returns true
        every { mockLiftState.exit() } just Runs
        every { mockExpressLiftState.needsExit } returns true
        every { mockExpressLiftState.exit() } just Runs

        val liftManager = LiftManager(listOf(mockLiftState, mockExpressLiftState), passengers)
        val returned = liftManager.advance()

        verify { mockLiftState.needsExit }
        verify { mockLiftState.exit() }
        verify { mockExpressLiftState.needsExit }
        verify { mockExpressLiftState.exit() }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_regularAndExpress_board() {
        val passenger1 = Passenger(50, 3)
        val passenger2 = Passenger(40, 4)

        val passengers = LinkedBlockingQueue<Passenger>(listOf(passenger1, passenger2))

        every { mockLiftState.currentFloor } returns 1
        every { mockLiftState.needsExit } returns false
        every { mockLiftState.canBoard(passenger1) } returns true
        every { mockLiftState.board(passenger1) } just Runs

        every { mockExpressLiftState.currentFloor } returns 1
        every { mockExpressLiftState.needsExit } returns false
        every { mockExpressLiftState.canBoard(passenger2) } returns true
        every { mockExpressLiftState.board(passenger2) } just Runs

        val liftManager = LiftManager(listOf(mockLiftState, mockExpressLiftState), passengers)
        val returned = liftManager.advance()

        verify { mockLiftState.canBoard(passenger1) }
        verify { mockExpressLiftState.board(passenger2) }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvance_regularAndExpress_move() {

        val passenger = Passenger(200, 3)
        val passengers = LinkedBlockingQueue<Passenger>(listOf(passenger))

        every { mockLiftState.currentFloor } returns 1
        every { mockLiftState.needsExit } returns false
        every { mockLiftState.canBoard(passenger) } returns false
        every { mockLiftState.shouldMoveIn() } returns LiftState.Direction.UP
        every { mockLiftState.move(LiftState.Direction.UP) } just Runs
        every { mockExpressLiftState.currentFloor } returns 2
        every { mockExpressLiftState.needsExit } returns false
        every { mockExpressLiftState.canBoard(passenger) } returns false
        every { mockExpressLiftState.shouldMoveIn() } returns LiftState.Direction.UP
        every { mockExpressLiftState.move(LiftState.Direction.UP) } just Runs

        val liftManager = LiftManager(listOf(mockLiftState, mockExpressLiftState), passengers)
        val returned = liftManager.advance()

        verify { mockLiftState.shouldMoveIn() }
        verify { mockLiftState.move(LiftState.Direction.UP) }
        verify { mockExpressLiftState.shouldMoveIn() }
        verify { mockExpressLiftState.move(LiftState.Direction.UP) }

        assertEquals(true, returned)
    }

    @Test
    fun testAdvanc_regularAndExpress_complete() {

        val passengers = LinkedBlockingQueue<Passenger>()

        every { mockLiftState.needsExit } returns false
        every { mockLiftState.currentFloor } returns 1
        every { mockLiftState.shouldMoveIn() } returns LiftState.Direction.NONE
        every { mockExpressLiftState.needsExit } returns false
        every { mockExpressLiftState.currentFloor } returns 1
        every { mockExpressLiftState.shouldMoveIn() } returns LiftState.Direction.NONE

        val liftManager = LiftManager(listOf(mockLiftState, mockExpressLiftState), passengers)
        val returned = liftManager.advance()

        assertEquals(false, returned)
    }
}