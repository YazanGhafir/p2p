package com.snubbull.app;

import static junit.framework.TestCase.assertEquals;

import android.view.View;
import androidx.test.rule.ActivityTestRule;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.model.Utility.Capacity;
import com.snubbull.app.model.Utility.MeansOfTransport;
import com.snubbull.app.view.TransportNoticeSheet;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class TransportNoticeSheetTest {

  @Rule
  public ActivityTestRule<TransportNoticeSheet> mActivityTestRule = new ActivityTestRule<>(
      TransportNoticeSheet.class);
  private TransporterNotice transp;
  private TransportNoticeSheet mActivity = null;

  {
    try {
      transp = new TransporterNotice("gbg", "sthlm", "735255504", 10, "2019-02-11",
          MeansOfTransport.CAR,
          Capacity.BIG);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Before
  public void setUp() throws Exception {
    mActivity = mActivityTestRule.getActivity();
  }


  @Test
  public void testLaunch() {
    View view1 = mActivity.findViewById(R.id.inputfieldFrom);
    assert view1 != null;
  }

  @Test
  public void checkTransportNoticeConstructor() {
    TransporterNotice transporter2;
    try {
      transporter2 = new TransporterNotice("gbg", "sthlm", "735255504", 10, "2019-02-11",
          MeansOfTransport.CAR, Capacity.BIG);
      assertEquals("not the same object", transporter2, transp);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @After
  public void tearDown() {
    mActivity = null;
  }
}
