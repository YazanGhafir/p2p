package com.snubbull.app.viewmodel;

import com.snubbull.app.model.AbstractNotice;
import java.util.ArrayList;


public interface NoticeProvider {


  Iterable<AbstractNotice> getNotices();

  void setNotices(ArrayList<AbstractNotice> notices);
}
