package com.example.proj_inz

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class HelpActivityTest {

    @get:Rule
    var rule = ActivityTestRule(HelpActivity::class.java)

    @Test
    fun testIfLaunchedActivityIsSameAsTargetActivity() {
        assertEquals(HelpActivity::class.java, rule.activity::class.java)
    }
    @Test
    fun testIfButtonExist() {
        onView(withId(R.id.helpToMenu))
    }
    @Test
    fun testIfButtonIsClickable() {
        onView(withId(R.id.helpToMenu))
            .check(matches(isClickable()))
    }
    @Test
    fun testIfButtonIsFocusable() {
        onView(withId(R.id.helpToMenu))
            .check(matches(isFocusable()))
    }
    @Test
    fun testIfButtonIsDisplayed() {
        onView(withId(R.id.helpToMenu))
            .perform(scrollTo())
        onView(withId(R.id.helpToMenu))
            .check(matches(isDisplayed()))
    }
    @Test
    fun testIfTextIsCorrect() {
        onView(withId(R.id.helpToMenu))
            .check(matches(withText(containsString("Przejdź"))))
    }
}