package com.example.android.bakingapp;

import android.support.test.espresso.IdlingResource;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    //Basic Example of Espresso Usage

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void IdlingResource() {
        //As the Activity is using Retrofit to get JSON, a IdlingResource for testing must be used
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
    }

    @Test
    public void mainActivityTest() {
        //Here go the UI Tests with Espresso
        onView(withId(R.id.recipe_linear_layout)).check(matches(isDisplayed()));
    }

}
