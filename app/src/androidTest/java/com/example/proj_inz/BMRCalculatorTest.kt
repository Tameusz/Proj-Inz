package com.example.proj_inz

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class BMRCalculatorTest {
    var activity: IntentsTestRule<CartActivity> = IntentsTestRule(CartActivity::class.java)

   @get:Rule
   var rule = ActivityTestRule(QuestionsActivity::class.java)

    @Test
    fun testBMRCalculator() {
        onView(withId(R.id.qWeight))
           .perform(typeText("120"))
        onView(withId(R.id.qHeight))
           .perform(typeText("190"))
        onView(withId(R.id.qAge))
           .perform(typeText("22"))
        onView(withId(R.id.radioButtonMale))
           .perform(scrollTo())
        onView(withId(R.id.radioButtonMale))
           .perform(click())
        onView(withId(R.id.confirm_button_questions))
           .perform(scrollTo())
        onView(withId(R.id.confirm_button_questions))
           .perform(click())
        activity.launchActivity(Intent())
        onView(withId(R.id.cartToCartDetails))
            .perform(click())
        onView(withId(R.id.textViewKCAL))
            .check(matches(withSubstring("2283")))
        onView(withId(R.id.textViewProtein))
            .check(matches(withSubstring("85")))
        onView(withId(R.id.textViewFat))
            .check(matches(withSubstring("76")))
        onView(withId(R.id.textViewCarbohydrates))
            .check(matches(withSubstring("313")))

    }
}