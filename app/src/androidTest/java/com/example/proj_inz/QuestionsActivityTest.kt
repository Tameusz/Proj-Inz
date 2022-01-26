package com.example.proj_inz

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.*
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class QuestionsActivityTest {

    @get:Rule
    var rule = ActivityTestRule(QuestionsActivity::class.java)

    @Test
    fun useAppContext() {

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.proj_inz", appContext.packageName)
    }
    @Test
    fun testIfLaunchedActivityIsSameAsTargetActivity() {
        assertEquals( QuestionsActivity::class.java, rule.activity::class.java)
    }
    @Test
    fun testToast() {
        onView(withId(R.id.confirm_button_questions))
            .perform(click())
        onView(withText(startsWith("Uzupełnij"))).inRoot(
            withDecorView(
                not(`is`(rule.getActivity().getWindow().getDecorView()))
            )
        ).check(matches(isDisplayed()))
    }
    @Test
    fun testIfButtonExist() {
        onView(withId(R.id.confirm_button_questions))
    }
    @Test
    fun testIfButtonIsDisplayed() {
        onView(withId(R.id.confirm_button_questions))
            .check(matches(isDisplayed()))
    }
    @Test
    fun testButton() {
        onView(withId(R.id.confirm_button_questions))
            .check(matches(isDisplayed()))
            .check(matches(isClickable()))
            .check(matches(isFocusable()))
    }
    @Test
    fun testIfButtonIsClickable() {
        onView(withId(R.id.confirm_button_questions))
            .check(matches(isClickable()))
    }
    @Test
    fun testIfButtonIsFocusable() {
        onView(withId(R.id.confirm_button_questions))
            .check(matches(isFocusable()))
    }
    @Test
    fun testIfTextIsCorrect() {
        onView(withId(R.id.hello))
            .check(matches(withText(containsString("Witaj"))))
            .check(matches(isDisplayed()))
    }
}