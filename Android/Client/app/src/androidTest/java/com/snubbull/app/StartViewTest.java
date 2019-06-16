package com.snubbull.app;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertNotEquals;

import android.content.Intent;
import androidx.test.annotation.UiThreadTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.snubbull.app.model.AndroidID;
import com.snubbull.app.view.StartView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing androidIDinit in particular
 */
@RunWith(AndroidJUnit4.class)
public class StartViewTest {

  @Rule
  public ActivityTestRule<StartView> activityRule
      = new ActivityTestRule<>(StartView.class, true, true);
  private Intent intent;

  @UiThreadTest
  @Before
  public void setUp() throws Exception {
    intent = new Intent(getApplicationContext(), StartView.class);
    intent.setAction(Intent.ACTION_SEND);
  }

  @Test
  public void androidIDinit() {
    assertNotEquals(AndroidID.getAndroidID(), "");
  }
}