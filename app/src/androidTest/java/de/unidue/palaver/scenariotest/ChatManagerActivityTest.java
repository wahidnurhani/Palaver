package de.unidue.palaver.scenariotest;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.unidue.palaver.R;
import de.unidue.palaver.activity.ChatManagerActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ChatManagerActivityTest {

    @Rule
    public ActivityTestRule<ChatManagerActivity> mChatManagerActivityActivityTestRule =
            new ActivityTestRule<>(ChatManagerActivity.class);

    @Test
    public void TestMenuAndSearch(){
        onView(ViewMatchers.withId(R.id.search_menu)).perform(click()).perform(typeText("search test")).
                perform(closeSoftKeyboard());

        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());

        onView(withText("Add Friend")).perform(click());

        onView(withId(R.id.addFriend_editText)).perform(typeText("saya")).perform(closeSoftKeyboard());

        onView(withId(R.id.addFriend_closeButton)).perform(click());

        onView(withId(R.id.search_menu)).perform(click()).perform(typeText("search2")).
                perform(closeSoftKeyboard());

        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());

        onView(withText("Setting")).perform(click());

    }


}
