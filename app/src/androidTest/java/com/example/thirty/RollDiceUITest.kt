package com.example.thirty

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)




@LargeTest
class RollDiceUITest {

    @Before
    fun setUp() {
        // Launch the RollDice fragment in its container.
        launchFragmentInContainer<RollDice>()
    }


    @Test
    fun checkDiceImagesChangeAfterThrowClick() {
        // Check that the throw button is displayed
        onView(withId(R.id.throw_button)).check(matches(isDisplayed()))

        // Click the throw button
        onView(withId(R.id.throw_button)).perform(click())

        // Check that the specific ImageView is displayed
        onView(withId(R.id.imageView1)).check(matches(isDisplayed()))
    }



    @Test
    fun testRollDiceAction() {
        onView(withId(R.id.throw_button)).perform(click())
        onView(withId(R.id.imageView1)).check(matches(withTagValue(not(CoreMatchers.nullValue()))))
    }



    @Test
    fun spinnerGetsLockedAfterSelection() {
        onView(withId(R.id.categorySpinner)).perform(click())

        // Click on a random item (assuming second item here for simplicity)
        onView(withText("SIX"))
            .perform(scrollTo())
            .perform(click())

        // Check if spinner is not clickable (or disabled) after the selection
        onView(withId(R.id.categorySpinner))
            .check(matches(not(isEnabled())))
    }




    @Test
    fun testDiceSelectionFlow() {
        // Click on the Spinner to open the dropdown
        onView(withId(R.id.categorySpinner)).perform(click())

        onView(withText("LOW")).perform(click())

        // Click to throw the dice
        onView(withId(R.id.throw_button)).perform(click())

        onView(withId(R.id.imageView1)).perform(click())
        onView(withId(R.id.imageView2)).perform(click())
        onView(withId(R.id.imageView3)).perform(click())
        onView(withId(R.id.imageView4)).perform(click())
        onView(withId(R.id.imageView5)).perform(click())
        onView(withId(R.id.imageView6)).perform(click())

        // Check that the selectedList linear layout's child count is 6
        onView(withId(R.id.SelectedDices)).check(matches(withChildCount(6)))
    }



    private fun withChildCount(expectedCount: Int) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("with child count: $expectedCount")
        }

        override fun matchesSafely(view: View): Boolean {
            if (view !is ViewGroup) return false
            return view.childCount == expectedCount
        }
    }





}
