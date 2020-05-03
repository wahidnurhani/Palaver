package de.unidue.palaver;


import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.unidue.palaver.ui.LoginActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void onClickRegisterTextView() throws Exception{
        Thread.sleep(3000 /*Or any other time*/);

        onView(withId(R.id.login_login_button)).
                check(matches(isDisplayed()));
        onView(withId(R.id.login_password_editText)).
                check(matches(isDisplayed()));
        onView(withId(R.id.login_password_editText)).
                check(matches(isDisplayed()));
        onView(withId(R.id.login_password_editText)).
                check(matches(isDisplayed()));
        onView(withId(R.id.login_register_button)).
                check(matches(isDisplayed()));

        onView(withId(R.id.login_userName_editText)).
                perform(typeText("test1991")).
                perform(closeSoftKeyboard());
        onView(withId(R.id.login_password_editText)).
                perform(typeText("test1991")).
                perform(closeSoftKeyboard());

        Thread.sleep(1000 /*Or any other time*/);

        onView(withId(R.id.login_register_button)).
                perform(click());

        Thread.sleep(3000 /*Or any other time*/);
    }
}
