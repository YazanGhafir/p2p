package com.snubbull.app;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static com.snubbull.app.SenderTransporterDualityUITests.assertCount;
import static com.snubbull.app.SenderTransporterDualityUITests.clickSender;
import static com.snubbull.app.SenderTransporterDualityUITests.clickTransporter;
import static com.snubbull.app.SenderTransporterDualityUITests.fillSenderFields;
import static com.snubbull.app.SenderTransporterDualityUITests.fillTransporterFields;
import static com.snubbull.app.SenderTransporterDualityUITests.getTestSenderNotices;
import static com.snubbull.app.SenderTransporterDualityUITests.getTestTransporterNotices;
import static com.snubbull.app.SenderTransporterDualityUITests.senderPost;
import static com.snubbull.app.SenderTransporterDualityUITests.transporterPost;
import static com.snubbull.app.ServerTestUtils.clearAllNotices;
import static com.snubbull.app.ServerTestUtils.postSenderNotices;
import static com.snubbull.app.ServerTestUtils.postTransporterNotices;
import static com.snubbull.app.ServerTestUtils.pullNotices;
import static com.snubbull.app.model.Utility.MeansOfTransport.CAR;
import static com.snubbull.app.model.Utility.MeansOfTransport.TRAIN;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import androidx.test.annotation.UiThreadTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.model.Utility.MeansOfTransport;
import com.snubbull.app.repository.NoticeRepository;
import com.snubbull.app.view.StartView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OrganizingUITest {

  private static final int NUMBER_OF_SENDER_NOTICES = 20;
  private static final int NUMBER_OF_TRANSPORTER_NOTICES = 24;
  @Rule
  public ActivityTestRule<StartView> activityRule
      = new ActivityTestRule<>(StartView.class);
  private NoticeRepository noticeRepository;

  @UiThreadTest
  @Before
  public void setUp() throws InterruptedException {

    clearAllNotices();

    postSenderNotices(getTestSenderNotices(NUMBER_OF_SENDER_NOTICES));
    postTransporterNotices(getTestTransporterNotices(NUMBER_OF_TRANSPORTER_NOTICES, CAR));
    /*
    noticeRepository = NoticeRepository.getNoticeRepository();

    noticeRepository.setSenderNotices(getTestSenderNotices(NUMBER_OF_SENDER_NOTICES));
    noticeRepository
        .setTransporterNotices(getTestTransporterNotices(NUMBER_OF_TRANSPORTER_NOTICES, CAR));*/
    pullNotices();
  }

  @Test
  public void meansOfTransportFiltersProperly() {
    if (true) return; // TODO Remove when fields are in place
    clickSender();
    fillSenderFields("from", "to");
    senderPost();
    chooseMeansOfTransport("Any");

    assertCount(TransporterNotice.class, NUMBER_OF_TRANSPORTER_NOTICES);

    chooseMeansOfTransport("Car");

    assertCount(TransporterNotice.class, NUMBER_OF_TRANSPORTER_NOTICES);

    chooseMeansOfTransport("Train");

    assertCount(TransporterNotice.class, 0);

    chooseMeansOfTransport("Car");

    assertCount(TransporterNotice.class, NUMBER_OF_TRANSPORTER_NOTICES);

    pressBack();
    pressBack();
    clickTransporter();

    fillTransporterFields("from", "to", MeansOfTransport.TRAIN);
    transporterPost();
    pressBack();
    transporterPost();
    pressBack();
    pressBack();

    clickSender();
    fillSenderFields("from", "to");
    senderPost();

    chooseMeansOfTransport("Any");

    assertCount(TransporterNotice.class, NUMBER_OF_TRANSPORTER_NOTICES + 2);

    chooseMeansOfTransport("Train");

    assertCount(TransporterNotice.class, 2);
  }

  @Test
  public void dropDownShowsForSender() {
    clickSender();
    fillSenderFields();
    senderPost();

    onView(withId(R.id.filterMoT)).check(matches(isDisplayed()));
  }

  @Test
  public void dropDownDoesNotShowsForTransporter() {
    clickTransporter();
    fillTransporterFields();
    transporterPost();

    onView(withId(R.id.filterMoT)).check(matches(not(isDisplayed())));
  }

  @Test
  public void navigatingForwardDoesNotResetDropDown() {
    clickSender();
    fillSenderFields("from", "to");
    senderPost();

    chooseMeansOfTransport("Car");

    onData(anything()).inAdapterView(withId(R.id.noticeList))
        .atPosition(0)
        .perform(click());

    pressBack();

    checkDropDownContent("Car");
  }

  @Test
  public void navigatingBackwardResetsDropDown() {
    if (true) return; // TODO Remove when fields are in place
    clickSender();
    fillSenderFields("from", "to");
    senderPost();

    chooseMeansOfTransport("Train");

    pressBack();

    senderPost();

    checkDropDownContent("Any");
  }

  @Test
  public void choosingMeansOfTransportShowsInDropDown() {
    clickSender();
    fillSenderFields();
    senderPost();

    chooseMeansOfTransport("Car");
    checkDropDownContent("Car");
  }

  @Test
  public void navigatingForwardRetainsNotices() {
    if (true) return; // TODO Remove when fields are in place
    clickTransporter();
    fillTransporterFields("from", "to", TRAIN);
    transporterPost();

    pressBack();
    pressBack();

    clickSender();
    fillSenderFields("from", "to");
    senderPost();

    chooseMeansOfTransport("Train");

    onData(anything()).inAdapterView(withId(R.id.noticeList))
        .atPosition(0)
        .perform(click());

    pressBack();

    assertCount(TransporterNotice.class, 1);
  }

  private void checkDropDownContent(String meansOfTransport) {
    onView(withId(R.id.filterMoT))
        .check(matches(withSpinnerText(containsString(meansOfTransport))));
  }

  private void chooseMeansOfTransport(String meansOfTransport) {
    onView(withId(R.id.filterMoT)).perform(click());
    onData(allOf(is(instanceOf(String.class)), is(meansOfTransport))).perform(click());
  }
}
