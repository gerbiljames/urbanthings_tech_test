package com.techtest.urbanthings

import com.techtest.urbanthings.model.Passenger
import com.techtest.urbanthings.state.LiftState
import java.util.*

/**
 * Class to manage the state of a Lift and a Queue of Passengers.
 */
class LiftManager(
    private val liftStates: List<LiftState>,
    private val passengers: Queue<Passenger>
) {

    init {
        println("Tick\tLift Status")
    }

    /**
     * The current time tick.
     */
    private var time = 0

    /**
     * Advances time one unit and updates the stored state accordingly.
     *
     * @return true if state was modified, false if not.
     */
    fun advance(): Boolean {
        time++

        print("$time")

        val stateModified = liftStates.mapIndexed { index, it ->
            print("\t\tLift ${index + 1}: " )
            advanceLiftState(it)
        }.reduce { acc, b -> acc || b }

        println(if (stateModified) "" else "\t\tCompleted")

        return stateModified
    }

    /**
     * Advances the state of a Lift.
     */
    private fun advanceLiftState(liftState: LiftState): Boolean {

        if (exitIfNeeded(liftState)) {
            println("Unloading at floor ${liftState.currentFloor}")
            return true
        }

        if (boardIfNeeded(liftState, passengers)) {
            println("Loading at floor ${liftState.currentFloor}")
            return true
        }

        if (moveIfNeeded(liftState))  {
            println("Moving to floor ${liftState.currentFloor}")
            return true
        }

        println("Idle ")

        return false
    }

    /**
     * Boards passengers to a Lift if needed.
     *
     * @return True if the state was modified, false if not.
     */
    private fun boardIfNeeded(liftState: LiftState, passengers: Queue<Passenger>): Boolean {

        if (liftState.currentFloor != 1) return false

        if (passengers.size == 0) return false

        if (!liftState.canBoard(passengers.peek())) return false

        while (passengers.size > 0 && liftState.canBoard(passengers.peek())) {
            liftState.board(passengers.poll())
        }

        return true
    }

    /**
     * Exits passengers from a Lift if needed.
     *
     * @return True if the state was modified, false if not.
     */
    private fun exitIfNeeded(liftState: LiftState): Boolean {

        if (!liftState.needsExit) return false

        liftState.exit()

        return true
    }

    /**
     * Moves a lift if needed.
     *
     * @return True if the state was modified, false if not.
     */
    private fun moveIfNeeded(liftState: LiftState): Boolean {

        if (liftState.shouldMoveIn() == LiftState.Direction.NONE) return false

        liftState.move(liftState.shouldMoveIn())

        return true
    }
}