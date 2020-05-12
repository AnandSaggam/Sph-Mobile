package com.dlminfosoft.sphmobile.utility

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * This class used with Espresso UI testing,
 * which wait until we get response from the api before executing UI test cases
 */
object EspressoIdlingResource {
    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}