package com.example.temporario.Home

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.temporario.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    private lateinit var scenario: ActivityScenario<HomeActivity>

    @Before
    fun setUp() {
        // Prepare the intent with necessary extras
        val intent = Intent(ApplicationProvider.getApplicationContext(), HomeActivity::class.java).apply {
            putExtra("FirebaseUserUID", "jJtYXwJ9RwbJRjW5pGFXXcsXhCB3") // Use a valid user UID
        }

        // Launch the HomeActivity
        scenario = ActivityScenario.launch(intent)
    }

    @Test
    fun testCalendarIsDisplayed() {
        // Verify that the calendar is displayed
        onView(withId(R.id.calendar))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testAddEventButtonClick() {
        // Click the addEvent button
        onView(withId(R.id.add_event))
            .perform(click())

        // Verify that CreateEventFragment is displayed
        scenario.onActivity { activity ->
            val fragmentManager: FragmentManager = activity.supportFragmentManager
            val fragment = fragmentManager.findFragmentById(R.id.fragment_container_create)
            assert(fragment is CreateEventFragment) // Verify that the fragment is of the expected type
        }
    }

    @Test
    fun testExitButtonRemovesFragment() {
        onView(withId(R.id.calendar))
            .perform(click())

        scenario.onActivity { activity ->
            // Check if ListEventsFragment is displayed
            val fragmentManager: FragmentManager = activity.supportFragmentManager
            val fragment: Fragment? = fragmentManager.findFragmentById(R.id.fragment_container_create) // The container where ListEventsFragment is placed

            assert(fragment is ListEventsFragment) // Ensure that the fragment is indeed ListEventsFragment
        }

        // Simulate clicking the exit button
        onView(withId(R.id.exit_button))
            .perform(click())

        // Verify that the fragment is removed from the FragmentManager
        scenario.onActivity { activity ->
            val fragmentManager: FragmentManager = activity.supportFragmentManager
            val fragment: Fragment? = fragmentManager.findFragmentById(R.id.fragment_container_create) // Check if the fragment is removed
            assert(fragment == null) // Assert that the fragment is removed
        }
    }
}
