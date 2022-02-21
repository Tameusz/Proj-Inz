package com.example.proj_inz

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.proj_inz.activities.CartActivity
import com.example.proj_inz.activities.MainActivity
import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class MainActivityTest {
    var activity: IntentsTestRule<CartActivity> = IntentsTestRule(CartActivity::class.java)

    @get:Rule
    var rule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testIfLaunchedActivityIsSameAsTargetActivity() {
        assertEquals( MainActivity::class.java, rule.activity::class.java)
    }
    @Test
    fun testAssertionOfButtonId() {
        assertEquals(rule.activity.resources.getResourceEntryName(R.id.menuConfirm),"menuConfirm")
    }

    @Test
    fun testIfEditTextIsCorrect() {
        onView(withId(R.id.etFor))
            .check(matches(withText(containsString(""))))
    }
    @Test
    fun testIfBMRIsUpdated() {
        onView(withId(R.id.etFor))
            .perform(typeText("2"))
        onView(withId(R.id.menuConfirm))
            .perform(click())
        activity.launchActivity(Intent())
        onView(withId(R.id.cartToCartDetails))
            .perform(click())
        onView(withId(R.id.textViewKCAL))
            .check(matches(withSubstring("4566")))
        onView(withId(R.id.textViewProtein))
            .check(matches(withSubstring("171")))
        onView(withId(R.id.textViewFat))
            .check(matches(withSubstring("152")))
        onView(withId(R.id.textViewCarbohydrates))
            .check(matches(withSubstring("627")))
    }
}