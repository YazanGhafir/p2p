package com.snubbull.app;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;

import static com.snubbull.app.ServerTestUtils.*;
import static org.hamcrest.CoreMatchers.anything;

import android.view.View;
import android.widget.ListView;
import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.snubbull.app.model.Notice;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.model.Utility.Capacity;
import com.snubbull.app.model.Utility.MeansOfTransport;
import com.snubbull.app.repository.NoticeRepository;
import com.snubbull.app.view.StartView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SenderTransporterDualityUITests {

  private static final int NUMBER_OF_SENDER_NOTICES = 25;
  private static final int NUMBER_OF_TRANSPORTER_NOTICES = 18;
  private static final int MAX_PRICE = 100000;
  /**
   * Specifies whether the tests should type in text in fields or quickly paste them in. Set to
   * false for speed; to true for better tests.
   */
  private static final boolean TYPE_NOT_REPLACE = false;
  @Rule
  public ActivityTestRule<StartView> activityRule
      = new ActivityTestRule<>(StartView.class);
  private NoticeRepository noticeRepository;

  private static int compareNoticePrice(Notice o1, Notice o2) {
    float cmp = o1.getPrice() - o2.getPrice();
    if (cmp > 0) {
      return 1;
    } else if (cmp < 0) {
      return -1;
    } else {
      return 0;
    }
  }

  /**
   * Generates a Matcher that asserts that the selected View (ListView) contains a specified number
   * of items. Observe: The targeted view must be of type ListView.
   *
   * @param count The number of elements (of specified class) that are expected to be found.
   * @return The matcher.
   */
  public static Matcher<View> itemNumber(final int count) {
    return new TypeSafeMatcher<View>() {
      @Override
      public boolean matchesSafely(final View view) {
        return ((ListView) view).getCount() == count;
      }

      @Override
      public void describeTo(final Description description) {
        description.appendText("ListView should have " + count + " items");
      }
    };
  }

  /**
   * Generates a Matcher that asserts that the selected View (ListView) contains a specified number
   * of items of the specified class. Observe: The targeted view must be of type ListView.
   *
   * @param noticeClass The class that the matcher should count among the list elements of the
   * ListView
   * @param count The number of elements (of specified class) that are expected to be found.
   * @return The matcher.
   */
  public static Matcher<View> itemNumber(final Class<? extends Notice> noticeClass, int count) {
    return new TypeSafeMatcher<View>() {
      @Override
      public boolean matchesSafely(final View view) {
        return getCount(view) == count;
      }

      private int getCount(final View view) {
        int count = 0;
        for (int i = 0; i < ((ListView) view).getCount(); i++) {
          if (((ListView) view).getItemAtPosition(i).getClass().equals(noticeClass)) {
            count++;
          }
        }
        return count;
      }

      @Override
      public void describeTo(final Description description) {
        description.appendText("ListView should have " + count + " items");
      }
    };
  }

  static List<SenderNotice> getTestSenderNotices(int amount) {
    List<SenderNotice> noticeList = new ArrayList<>();
    try {
      for (int i = 0; i < amount; i++) {
        noticeList.add(new SenderNotice("from", "to", "735255504", i,
            "2019-01-01", 1, Capacity.BIG));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return noticeList;
  }

  static List<TransporterNotice> getTestTransporterNotices(int amount) {
    return getTestTransporterNotices(amount, null);
  }

  static List<TransporterNotice> getTestTransporterNotices(int amount,
      MeansOfTransport meansOfTransport) {
    long motSeed = 3614328226142167655L,
        priceSeed = 7199962233501589582L;
    Random rnd = new Random(motSeed),
        rndPrice = new Random(priceSeed);
    List<TransporterNotice> noticeList = new ArrayList<>();
    try {
      for (int i = 0; i < amount; i++) {
        MeansOfTransport rndMoT = MeansOfTransport.values()[rnd
            .nextInt(MeansOfTransport.values().length)];
        noticeList.add(
            new TransporterNotice("from", "to", "735255504", rndPrice.nextInt(MAX_PRICE) / 100.0f,
                "2019-01-1", meansOfTransport == null ? rndMoT : meansOfTransport, Capacity.BIG));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return noticeList;
  }

  static void assertCount(Class<? extends Notice> noticeClass, int count) {
    onView(withId(R.id.noticeList)).check(matches(itemNumber(noticeClass, count)));
  }

  static void senderPost() {
    if (true) return; // TODO Remove when fields are in place
    onView(withId(R.id.postBtn)).perform(click());
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  static void transporterPost() {
    if (true) return; // TODO Remove when fields are in place
    onView(withId(R.id.postBtn)).perform(click());
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  static void clickSender() {
    onView(withId(R.id.senderButton))
        .perform(click());
  }

  static void clickTransporter() {
    onView(withId(R.id.BtnTransport))
        .perform(click());
  }

  static void fillSenderFields() {
    fillSenderFields("Here", "There");
  }

  static void fillSenderFields(String from, String to) {
    onView(withId(R.id.match_button)).perform(click());
    if (true) return; // TODO Remove temp solution when fields are available to fill
    TyperSupplier typerSupplier;
    if (TYPE_NOT_REPLACE) {
      typerSupplier = ViewActions::typeText;
    } else {
      typerSupplier = ViewActions::replaceText;
    }

    onView(withId(R.id.inputfieldFrom)).perform(typerSupplier.getTyper(from))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.inputfieldTo)).perform(typerSupplier.getTyper(to))
        .perform(closeSoftKeyboard());

    onView(withId(R.id.telephoneNumberId)).perform(typerSupplier.getTyper("735255504"));

    onView(withId(R.id.inputfieldDate)).perform(click());
    onView(withText("OK")).inRoot(isDialog()).check(matches(ViewMatchers.isDisplayed()))
        .perform(click());

    onView(withId(R.id.inutfieldWeight)).perform(typerSupplier.getTyper("10"))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.inputfieldVolume)).perform(typerSupplier.getTyper("10"))
        .perform(closeSoftKeyboard());

    onView(withId(R.id.inputfieldQuantity)).perform(typerSupplier.getTyper("2"))
        .perform(closeSoftKeyboard());

    onView(withId(R.id.inputfieldPrice)).perform(typerSupplier.getTyper("100"))
        .perform(closeSoftKeyboard());
  }

  static void fillTransporterFields() {
    fillTransporterFields("Here", "There");
  }

  static void fillTransporterFields(String from, String to) {
    fillTransporterFields(from, to, MeansOfTransport.CAR);
  }

  static void fillTransporterFields(String from, String to, MeansOfTransport meansOfTransport) {
    onView(withId(R.id.match_button)).perform(click());
    if (true) return; // TODO Remove temp solution when fields are available to fill
    TyperSupplier typerSupplier;
    if (TYPE_NOT_REPLACE) {
      typerSupplier = ViewActions::typeText;
    } else {
      typerSupplier = ViewActions::replaceText;
    }

    onView(withId(R.id.inputfieldFrom)).perform(typerSupplier.getTyper(from))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.inputfieldTo)).perform(typerSupplier.getTyper(to))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.telephoneNumberId)).perform(typerSupplier.getTyper("735255504"))
        .perform(closeSoftKeyboard());

    onView(withId(R.id.inputfieldType)).perform(click());
    onView(withText("OK")).inRoot(isDialog()).check(matches(ViewMatchers.isDisplayed()))
        .perform(click());

    onView(withId(R.id.inputfieldDate)).perform(click());
    onView(withText("OK")).inRoot(isDialog()).check(matches(ViewMatchers.isDisplayed()))
        .perform(click());

    onView(withId(R.id.inutfieldWeight)).perform(typerSupplier.getTyper("10"))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.inputfieldVolume)).perform(typerSupplier.getTyper("10"))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.inputfieldPrice)).perform(typerSupplier.getTyper("100"))
        .perform(closeSoftKeyboard());
  }

  @UiThreadTest
  @Before
  public void setUp() throws InterruptedException {

    clearAllNotices();

    postSenderNotices(getTestSenderNotices(NUMBER_OF_SENDER_NOTICES));
    postTransporterNotices(getTestTransporterNotices(NUMBER_OF_TRANSPORTER_NOTICES));
    /*
    noticeRepository = NoticeRepository.getNoticeRepository();

    noticeRepository.setSenderNotices(getTestSenderNotices(NUMBER_OF_SENDER_NOTICES));
    noticeRepository
        .setTransporterNotices(getTestTransporterNotices(NUMBER_OF_TRANSPORTER_NOTICES));*/
    pullNotices();
  }

  @Test
  public void clickNotices() {
    clickTransporter();
    fillTransporterFields();
    transporterPost();
    pressBack();
    pressBack();
    clickSender();
    fillSenderFields();
    senderPost();
    onData(anything()).inAdapterView(withId(R.id.noticeList)).atPosition(0).perform(click());
    onView(withId(R.id.accountIdItem)).check(matches(withText("735255504")));

  }

  @Test
  public void transporterViewsSenderNotices() {
    clickTransporter();

    fillTransporterFields("from", "to");
    transporterPost();

    assertCount(SenderNotice.class, NUMBER_OF_SENDER_NOTICES);
  }

  @Test
  public void senderViewsTransporterNotices() {
    clickSender();

    fillSenderFields("from", "to");
    senderPost();

    assertCount(TransporterNotice.class, NUMBER_OF_TRANSPORTER_NOTICES);
  }

  @Test
  public void sortedByPrice_AscendingForSenders() {
    double[] orderedPrices = getTestTransporterNotices(NUMBER_OF_TRANSPORTER_NOTICES)
        .stream()
        .sorted(SenderTransporterDualityUITests::compareNoticePrice)
        .mapToDouble(Notice::getPrice)
        .toArray(); // Sorts the items' prices

    clickSender();
    fillSenderFields("from", "to");
    senderPost();

    for (int i = 0; i < orderedPrices.length; i++) {
      // Checks if the items' prices are in the expected order
      onData(anything()).inAdapterView(withId(R.id.noticeList))
          //onData(instanceOf(TransporterNotice.class))
          .atPosition(i)
          .onChildView(withId(R.id.content))
          .check(matches(withText(String.valueOf((float) orderedPrices[i]))));
    }
  }

  @Test
  public void sortedByPrice_DescendingForTransporters() {
    double[] orderedPrices = getTestSenderNotices(NUMBER_OF_SENDER_NOTICES)
        .stream()
        .sorted((o1, o2) -> -compareNoticePrice(o1, o2))
        .mapToDouble(Notice::getPrice)
        .toArray(); // Sorts the items' prices

    clickTransporter();
    fillTransporterFields("from", "to");
    transporterPost();

    for (int i = 0; i < orderedPrices.length; i++) {
      // Checks if the items' prices are in the expected order
      onData(anything()).inAdapterView(withId(R.id.noticeList))
          //onData(instanceOf(SenderNotice.class))
          .atPosition(i)
          .onChildView(withId(R.id.content))
          .check(matches(withText(String.valueOf((float) orderedPrices[i]))));
    }
  }

  @Test
  public void postingAsTransporterIsVisibleBySenderAndViceVersa() {
    if (true) return; // TODO Remove when fields are in place
    clickTransporter();
    fillTransporterFields("from", "to");
    transporterPost();

    pressBack();
    pressBack();

    clickSender();
    fillSenderFields("from", "to");
    senderPost();

    // Check that transporter's post is present
    assertCount(TransporterNotice.class, NUMBER_OF_TRANSPORTER_NOTICES + 1);

    pressBack();
    pressBack();

    clickTransporter();
    fillTransporterFields("from", "to");
    transporterPost();

    // Check that sender's post is present
    assertCount(SenderNotice.class, NUMBER_OF_SENDER_NOTICES + 1);
  }

  @Test
  public void postingMultipleNoticesVisibleToOther() {
    if (true) return; // TODO Remove when fields are in place
    int nTransporterNotices = 3;
    for (int i = 0; i < nTransporterNotices; i++) {
      clickTransporter();
      fillTransporterFields("from", "to");
      transporterPost();
      pressBack();
      pressBack();
    }

    clickSender();
    fillSenderFields("from", "to");
    senderPost();

    // Check the appropriate number of notices are present
    assertCount(TransporterNotice.class, NUMBER_OF_TRANSPORTER_NOTICES + nTransporterNotices);
  }

  @Test
  public void postedNoticesAreRetainedAfterNavigation() {
    if (true) return; // TODO Remove when fields are in place
    int nSenderNotices = 3, nTransporterNotices = 2;
    for (int i = 0; i < nSenderNotices; i++) {
      clickSender();
      fillSenderFields("from", "to");
      senderPost();
      pressBack();
      pressBack();
    }

    for (int i = 0; i < nTransporterNotices; i++) {
      clickTransporter();
      fillTransporterFields("from", "to");
      transporterPost();
      pressBack();
      pressBack();
    }

    clickSender();
    fillSenderFields("from", "to");
    senderPost();

    assertCount(TransporterNotice.class, NUMBER_OF_TRANSPORTER_NOTICES + nTransporterNotices);

    pressBack();
    pressBack();
    clickTransporter();
    fillTransporterFields("from", "to");
    transporterPost();

    assertCount(SenderNotice.class, NUMBER_OF_SENDER_NOTICES + nSenderNotices + 1);
  }

  @Test
  public void filteredByLocation() {
    if (true) return; // TODO Remove when fields are in place
    String from = "Chalmers", to = "Backaplan";

    clickTransporter();
    fillTransporterFields(from, to);
    transporterPost();

    assertCount(SenderNotice.class, 0);

    pressBack();
    pressBack();

    clickSender();
    fillSenderFields(from, to);
    senderPost();

    assertCount(TransporterNotice.class, 1);

    pressBack();
    pressBack();

    clickTransporter();
    fillTransporterFields(from, to);
    transporterPost();

    assertCount(SenderNotice.class, 1);

    pressBack();

    fillTransporterFields();
    transporterPost();

    assertCount(SenderNotice.class, 0);

  }

  @Test
  public void filterByLocationUnaffectedByNavigation() {
    if (true) return; // TODO Remove when fields are in place
    clickSender();
    fillSenderFields();
    senderPost();
    pressBack();
    senderPost();

    pressBack();
    pressBack();

    clickTransporter();
    fillTransporterFields();
    transporterPost();

    assertCount(SenderNotice.class, 2);

    pressBack();
    transporterPost();

    assertCount(SenderNotice.class, 2);

    pressBack();
    pressBack();
    clickSender();
    pressBack();
    clickTransporter();
    fillTransporterFields();
    transporterPost();

    assertCount(SenderNotice.class, 2);
  }

  /**
   * Interface used to switch between {@link ViewActions#typeText typeText} and {@link
   * ViewActions#replaceText(String) replaceText}
   */
  private interface TyperSupplier {

    ViewAction getTyper(String string);
  }
}
