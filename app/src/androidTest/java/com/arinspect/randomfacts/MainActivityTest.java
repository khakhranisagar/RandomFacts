package com.arinspect.randomfacts;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.arinspect.randomfacts.activities.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

//import androidx.test.espresso.contrib.RecyclerViewActions;
//import androidx.test.rule

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                mIdlingResource = activity.getIdlingResource();
                // To prove that the test fails, omit this call:
//                IdlingRegistry.getInstance().register(mIdlingResource);
            }
        });
    }

    @Rule
    public ActivityTestRule mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    /**
     * This test checks (a negative scenario) that any of the items in recycler DOES NOT EXIST
     */
    @Test
    public void testRecyclerToBeFailed(){
        Espresso.onView(ViewMatchers.withId(R.id.tv_title_fact)).check(ViewAssertions.doesNotExist());
    }

    /**
     * This test checks (a negative scenario) that any of the items in recycler DOES NOT EXIST
     */
    @Test
    public void testRecycler(){
        Espresso.registerIdlingResources(mIdlingResource);
        Espresso.onView(ViewMatchers.withId(R.id.rvFacts)).check(RecyclerViewItemCountAssertion.withItemCount(10));

    }
}



/**
 * Custom Assertion Utility for RecyclerView Item Count
 */
class RecyclerViewItemCountAssertion implements ViewAssertion {
    private final Matcher<Integer> matcher;

    public static RecyclerViewItemCountAssertion withItemCount(int expectedCount) {
        return withItemCount(is(expectedCount));
    }

    public static RecyclerViewItemCountAssertion withItemCount(Matcher<Integer> matcher) {
        return new RecyclerViewItemCountAssertion(matcher);
    }

    private RecyclerViewItemCountAssertion(Matcher<Integer> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), matcher);
    }
}