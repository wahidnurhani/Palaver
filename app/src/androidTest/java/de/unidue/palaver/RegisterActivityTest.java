package de.unidue.palaver;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.unidue.palaver.ui.RegisterActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> mRegisterActivityActivityTestRule = new ActivityTestRule<>(RegisterActivity.class);

    @Test
    public void registerDataInputScenarioTest() {
        onView(withId(R.id.register_userName_editText)).
                perform(typeText("test1991")).perform(closeSoftKeyboard());

        onView(withId(R.id.register_password_editText)).
                perform(typeText("test1991")).perform(closeSoftKeyboard());

        onView(withId(R.id.register_repassword_editText)).
                perform(typeText("test1991")).perform(closeSoftKeyboard());

    }

}
