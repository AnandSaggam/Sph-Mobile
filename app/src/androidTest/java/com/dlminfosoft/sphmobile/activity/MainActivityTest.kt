package com.dlminfosoft.sphmobile.activity

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dlminfosoft.sphmobile.R
import com.dlminfosoft.sphmobile.adapter.AdapterDataUsage
import com.dlminfosoft.sphmobile.utility.EspressoIdlingResource
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class contains Espresso UI testing for Main activity
 */
@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get : Rule
    val mainActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        mainActivityScenarioRule
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun destroy() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun test_recycler_view_visible() {
        onView(withId(R.id.recycle_view_data_usage)).check(matches(isDisplayed()))
    }

    @Test
    fun test_progress_bar_gets_hide() {
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun verify_by_id_textView_txt_year_is_display_in_recyclerview_item() {
        onView(withId(R.id.recycle_view_data_usage))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<AdapterDataUsage.ItemHolder>(
                        1,
                        viewActionListItemChildViewWithId(R.id.txt_year, "Year: 2009")
                    )
            )
    }

    @Test
    fun verify_click_on_list_item_with_drop_volume_image_display_alert_message_dialog() {
        onView(withId(R.id.recycle_view_data_usage))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<AdapterDataUsage.ItemHolder>(
                        3, viewActionClickOnListItemDecreaseImage(
                            R.id.img_btn_decrease
                        )
                    )
            )
    }

    @Test
    fun verify_recyclerview_scroll_to_last_item_in_list() {
        onView(withId(R.id.recycle_view_data_usage))
            .perform(RecyclerViewActions.scrollToPosition<AdapterDataUsage.ItemHolder>(10))
        onView(withText("Year: 2018")).check(matches(isDisplayed()))
    }

    @Test
    fun verify_textView_is_display_specific_text_in_recyclerview_item() {
        onView(withId(R.id.recycle_view_data_usage))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<AdapterDataUsage.ItemHolder>(0, click())
            )
        val itemText = "Year: 2008"
        onView(withText(itemText)).check(matches(isDisplayed()))
    }

    private fun viewActionClickOnListItemDecreaseImage(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id) as View
                if (v.visibility == View.VISIBLE)
                    v.performClick()
            }
        }
    }

    private fun viewActionListItemChildViewWithId(id: Int, textToBeTyped: String): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id) as TextView
                v.text = textToBeTyped
            }
        }
    }
}
