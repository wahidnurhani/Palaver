package de.unidue.palaver;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.unidue.palaver.ui.SplashScreenActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ScenarioTests {

    @Rule
    public ActivityTestRule<SplashScreenActivity> mActivityActivityTestRule =
            new ActivityTestRule<>(SplashScreenActivity.class);

    @Test
    public void testLoginAndLogout() throws Exception{

        Thread.sleep(2000 /*Or any other time*/);

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

        onView(withId(R.id.login_login_button)).
                perform(click());

        Thread.sleep(5000 /*Or any other time*/);

        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());

        onView(withText("Logout")).perform(click());

        Thread.sleep(3000 /*Or any other time*/);
    }

    @Test
    public void RegisterThanLogin() throws Exception{

        //if user already exist

        Thread.sleep(5000 /*Or any other time*/);

        onView(withId(R.id.login_register_button)).
                perform(click());

        Thread.sleep(500 /*Or any other time*/);

        onView(withId(R.id.register_userName_editText)).
                perform(typeText("test1991")).perform(closeSoftKeyboard());

        onView(withId(R.id.register_password_editText)).
                perform(typeText("test1991")).perform(closeSoftKeyboard());

        onView(withId(R.id.register_repassword_editText)).
                perform(typeText("test1991")).perform(closeSoftKeyboard());

        onView(withId(R.id.register_register_button)).
                perform(click());

        Thread.sleep(2000 /*Or any other time*/);

        onView(withId(R.id.register_backToLogin)).
                perform(click());

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

        onView(withId(R.id.login_login_button)).
                perform(click());

        Thread.sleep(3000 /*Or any other time*/);

        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());

        onView(withText("Logout")).perform(click());

        Thread.sleep(3000 /*Or any other time*/);

        onView(withId(R.id.login_login_button)).
                check(matches(isDisplayed()));

    }

}
