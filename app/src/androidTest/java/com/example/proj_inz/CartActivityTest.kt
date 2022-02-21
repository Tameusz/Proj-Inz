package com.example.proj_inz

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.proj_inz.activities.CartActivity
import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class CartActivityTest {
    @get:Rule
    var rule = ActivityTestRule(CartActivity::class.java)

    @Test
    fun testIfLaunchedActivityIsSameAsTargetActivity() {
        assertEquals(CartActivity::class.java, rule.activity::class.java)

    }
    @Test
    fun testCalculateCaloriesFunction() {
        assertEquals(rule.activity.calculateCalories(120,190,22,1),2283)
    }
    @Test
    fun testIfButtonsExist() {
        onView(withId(R.id.cartToCartDetails))
        onView(withId(R.id.cartToBarcode))
        onView(withId(R.id.cartToRecognizer))
        onView(withId(R.id.cartClearButton))
    }
    @Test
    fun testIfButtonIsDisplayed() {
        onView(withId(R.id.cartClearButton))
            .check(matches(isDisplayed()))
    }
    @Test
    fun testIfButtonIsClickable() {
        onView(withId(R.id.cartToRecognizer))
            .check(matches(isClickable()))
    }
    @Test
    fun testIfButtonIsFocusable() {
        onView(withId(R.id.cartToBarcode))
            .check(matches(isFocusable()))
    }
    @Test
    fun testPerformClick() {
        onView(withId(R.id.cartToRecognizer))
            .perform(click())
    }
    @Test
    fun testIfTextIsCorrect() {
        onView(withId(R.id.shopItemList))
            .check(matches(withText(containsString("Produkty"))))
    }
    @Test
    fun testMenuButton() {
        onView(withId(R.id.cartDetailsButton))
            .perform(click())
    }
}