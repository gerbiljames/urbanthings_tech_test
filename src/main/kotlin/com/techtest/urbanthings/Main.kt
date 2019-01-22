package com.techtest.urbanthings

import com.techtest.urbanthings.model.Lift
import com.techtest.urbanthings.model.Passenger
import com.techtest.urbanthings.state.LiftState
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

fun main(args: Array<String>) {
    calculateLiftTicks(weights, destinations, floors, maxPassengers, maxWeight, numberOfLifts)
}

/**
 * Function required by the specification.
 */
fun calculateLiftTicks(weights: Array<Int>, destinations: Array<Int>, floors: Int, maxPassengers: Int, maxWeight: Int,
                       numberOfLifts: Int) {

    // We don't want to continue if weights and destinations are different sizes.
    if (weights.size != destinations.size) throw IllegalArgumentException("weights.size != destinations.size")

    // We can't deliver passengers to a floor higher than max.
    if (!destinations.none { it > floors }) throw IllegalArgumentException("destination higher than floors")

    // We can't carry passengers above max weight.
    if (!weights.none { it > maxWeight }) throw IllegalArgumentException("passenger above max weight")

    // We need at least one lift.
    if (numberOfLifts == 0) throw IllegalArgumentException("at least one lift is required")

    // Create the Lift model.
    val lift = Lift(maxWeight, maxPassengers)

    // Create the Lift states providing the model. We can share the same model since it is immutable.
    val liftState = List(numberOfLifts) { LiftState(lift) }

    // Create the Passenger Queue and populate it.
    val passengers: Queue<Passenger> = LinkedBlockingQueue<Passenger>()

    weights.zip(destinations).forEach {
        passengers.offer(Passenger(it.first, it.second))
    }

    // Create the LiftManager and advance it until it's finished.
    val liftManager = LiftManager(liftState, passengers)

    while (liftManager.advance()) {}
}
