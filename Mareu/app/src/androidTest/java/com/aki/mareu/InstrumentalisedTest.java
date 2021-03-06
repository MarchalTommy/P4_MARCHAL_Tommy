package com.aki.mareu;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ScrollToAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.aki.mareu.di.DI;
import com.aki.mareu.models.Reunion;
import com.aki.mareu.service.ReunionApiService;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
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

    ReunionApiService mApiService = DI.getNewInstanceApiService();


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

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.room_spinner_newReunion),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.first_cardview),
                                        0),
                                2),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(withClassName(is("androidx.appcompat.widget.DropDownListView")))
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
                .perform(typeText("Test Reunion"))
                .perform(ViewActions.closeSoftKeyboard())
                .perform(swipeUp());

        onView(withId(R.id.creatorname_edittext))
                .perform(click())
                .perform(typeText("Admin"))
                .perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.new_participant))
                .perform(typeText("NewParticipant@SdZ.fr"))
                .perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.second_cardview))
                .perform(swipeUp());

        onView(withId(R.id.create_new_reunion_button))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.list_reunions))
                .perform(RecyclerViewActions.actionOnItemAtPosition(12, click()));

        onView(withText("Test Reunion")).check(matches(isDisplayed()));
    }

    @Test
    public void C_filteringByDate() {

        //Création d'une réunion à la date du jour
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        String dayFormatted = String.valueOf(day);
        String monthFormatted = String.valueOf(month+1);
        if (day<10) {
            dayFormatted = "0" + day;
        }
        if(month<9) {
            monthFormatted = "0" + month;
        }
        mApiService.createReunion(new Reunion(mApiService.getNewId(), "Test Reunion", mApiService.getRooms().get(2), "10:00", dayFormatted+"/"+monthFormatted+"/"+year, 1, "Test", mApiService.getUsers()));

        //Test du filtre par date à la date du jour
        onView(withId(R.id.action_filter))
                .perform(click());

        onView(withId(R.id.radio_date))
                .perform(click(), swipeUp());

        onView(withId(R.id.filter_date_button))
                .perform(click());

        onView(withId(R.id.list_reunions))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText("Test Reunion")).check(matches(isDisplayed()));
    }

    @Test
    public void D_filteringByRoom() {

        mApiService.createReunion(new Reunion(mApiService.getNewId(), "Test Reunion", mApiService.getRooms().get(2), "10:00", "13/08/1996", mApiService.getUsers().size(), "Test", mApiService.getUsers()));


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
