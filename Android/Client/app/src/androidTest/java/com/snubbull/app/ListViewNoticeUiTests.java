package com.snubbull.app;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import android.content.Intent;
import androidx.test.annotation.UiThreadTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.model.Utility;
import com.snubbull.app.model.Utility.Capacity;
import com.snubbull.app.model.Utility.MeansOfTransport;
import com.snubbull.app.repository.NoticeRepository;
import com.snubbull.app.view.NoticeView;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class ListViewNoticeUiTests {

  private final static int LAST_ELEMENT_INDEX = 500;
  @Rule
  public ActivityTestRule<NoticeView> activityRule
      = new ActivityTestRule<>(NoticeView.class, true, false);
  private NoticeRepository noticeRepository;
  private Intent intent;
  private SenderNotice postedSenderNotice;

  {
    try {
      postedSenderNotice = new SenderNotice(" ", " ", "735255503", 10, "2019-03-03", 1,
          Capacity.MEDIUM);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @UiThreadTest
  @Before
  public void setUp() {

    intent = new Intent(getApplicationContext(), NoticeView.class);
    intent.setAction(Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra("senderNotice", postedSenderNotice);

    noticeRepository = NoticeRepository.getNoticeRepository();

    noticeRepository.setTransporterNotices(getTestTransporterNotices());
  }

  private List<TransporterNotice> getTestTransporterNotices() {
    List<TransporterNotice> noticeList = new ArrayList<>();
    for (int i = 0; i < LAST_ELEMENT_INDEX + 1; i++) {
      try {
        noticeList.add(new TransporterNotice("FROM" + i, "TO" + i, "735255503", i, "2011-06-04",
            MeansOfTransport.CAR,
            Utility.Capacity.SMALL));
      } catch (Exception e) {
      }
    }
    return noticeList;
  }

  private void swipeUpAndDown(int swipes, int viewId) {
    for (int i = 0; i < swipes / 2; i++) {
      onView(withId(viewId)).perform(swipeUp()); // scroll down
    }
    for (int i = swipes / 2; i < swipes; i++) {
      onView(withId(viewId)).perform(swipeDown()); // scroll up
    }
  }

  @Test
  public void lastElementOfListIsDisplayedProperly() {
    activityRule.launchActivity(intent);
    onData(anything()).inAdapterView(withId(R.id.noticeList))
        //onData(instanceOf(TransporterNotice.class))
        .atPosition(LAST_ELEMENT_INDEX)
        .onChildView(withId(R.id.content))
        .check(matches(withText(
            String.valueOf((float) LAST_ELEMENT_INDEX)
        )));
  }

  // Existing views are reused and updated with new data, so this can fail if an Adapter is
  // working incorrectly.
  @Test
  public void scrollingDoesNotUpdateViewsWithIncorrectData() {
    activityRule.launchActivity(intent);
    swipeUpAndDown(10, R.id.noticeList);

    onData(anything()).inAdapterView(withId(R.id.noticeList)).atPosition(LAST_ELEMENT_INDEX / 2)
        //onData(instanceOf(TransporterNotice.class)).atPosition(LAST_ELEMENT_INDEX / 2)
        .onChildView(withId(R.id.content))
        .check(matches(withText(
            String.valueOf(LAST_ELEMENT_INDEX / 2f) // middle element is still correct.
        )));
  }
}
