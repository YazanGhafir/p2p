package com.snubbull.app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.v7.widget.AppCompatEditText;
import android.widget.EditText;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.snubbull.app.view.MatchView;
import java.util.concurrent.TimeUnit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PlacesTest {

  @Rule
  public ActivityTestRule<MatchView> activityRule
      = new ActivityTestRule<>(MatchView.class);

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  @Test
  public void placesMatchesSimilarString() throws InterruptedException {
    onView(withId(R.id.placesFrom)).perform(click());
    onView(instanceOf(EditText.class)).perform(typeText("Chalmers Tekniska Hogskola"));
    TimeUnit.SECONDS.sleep(2);
    onView(instanceOf(EditText.class)).perform(pressKey(66)).perform(pressKey(66));
    TimeUnit.MILLISECONDS.sleep(200);
    onView(allOf(instanceOf(AppCompatEditText.class), withText(containsString("Chalmers"
        + " University"))))
        .check(matches(isDisplayed()));
  }

}
