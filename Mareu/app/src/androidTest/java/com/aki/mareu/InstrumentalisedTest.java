package com.aki.mareu;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstrumentalisedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void A_recyclerViewIsShowing() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion A"),
                        childAtPosition(
                                allOf(withId(R.id.recyclerview_layout),
                                        childAtPosition(
                                                withId(R.id.list_reunions),
                                                0)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Réunion A")));
    }

    @Test
    public void E_deletingAReunion() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.item_list_delete_button),
                        childAtPosition(
                                allOf(withId(R.id.recyclerview_layout),
                                        childAtPosition(
                                                withId(R.id.list_reunions),
                                                0)),
                                8),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.item_list_name), withText("Réunion B"),
                        childAtPosition(
                                allOf(withId(R.id.recyclerview_layout),
                                        childAtPosition(
                                                withId(R.id.list_reunions),
                                                0)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Réunion B")));
    }

    @Test
    public void B_creatingAReunion() {
        onView(withId(R.id.fab))
                .perform(click());

        onView(withId(R.id.room_spinner_newReunion))
                .perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        appCompatCheckedTextView.perform(click());

        onView(withId(R.id.date_edittext))
                .perform(click());
        onView(withId(R.id.date_edittext))
                .perform(click());
        onView(withId(android.R.id.button1))
                .perform(click());

        onView(withId(R.id.time_edittext))
                .perform(click());
        onView(withId(R.id.time_edittext))
                .perform(click());
        onView(withId(android.R.id.button1))
                .perform(click());

        onView(withId(R.id.name_edittext))
                .perform(click())
                .perform(typeText("Test Reunion"));

        onView(withId(R.id.creatorname_edittext))
                .perform(click())
                .perform(typeText("Admin"))
                .perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.new_participant))
                .perform(typeText("NewParticipant@SdZ.fr"))
                .perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.create_new_reunion_button))
                .perform(click());

        onView(withId(R.id.list_reunions))
                .perform(RecyclerViewActions.actionOnItemAtPosition(12, click()));

        onView(withText("Test Reunion")).check(matches(isDisplayed()));
    }

    @Test
    public void C_filteringByDate() {

        onView(withId(R.id.action_filter))
                .perform(click());

        onView(withId(R.id.radio_date))
                .perform(click());

        onView(withId(R.id.filter_date_button))
                .perform(click());

        onView(withId(R.id.list_reunions))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText("Test Reunion")).check(matches(isDisplayed()));
    }

    @Test
    public void D_filteringByRoom() {

        onView(withId(R.id.action_filter))
                .perform(click());

        onView(withId(R.id.radio_room))
                .perform(click());

        onView(withId(R.id.room_spinner))
                .perform(click());

        onData(allOf(is(instanceOf(String.class)),
                is("Room 3"))).inRoot(isPlatformPopup()).perform(click());

        onView(withId(R.id.filter_room_button))
                .perform(click());

        onView(withId(R.id.list_reunions))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText("Room 3")).check(matches(isDisplayed()));
    }
}
