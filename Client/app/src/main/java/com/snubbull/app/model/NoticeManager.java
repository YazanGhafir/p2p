package com.snubbull.app.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import java.util.ArrayList;
import java.util.List;

public class NoticeManager {

  private MutableLiveData<List<SenderNotice>> senderNoticeList = new MutableLiveData<>();
  private MutableLiveData<List<TransporterNotice>> transporterNoticeList = new MutableLiveData<>();

  /**
   * Initialize the local storage of transporter and sender notices.
   */
  public NoticeManager() {
    senderNoticeList.setValue(new ArrayList<>());
    transporterNoticeList.setValue(new ArrayList<>());
  }

  /**
   * Add a sender notice.
   *
   * @param n The notice to add
   */
  public void addSenderNotice(SenderNotice n) {
    senderNoticeList.getValue().add(n);
    senderNoticeList.setValue(senderNoticeList.getValue()); // Send update to observers
  }

  /**
   * Add a transporter notice.
   *
   * @param n The notice to add.
   */
  public void addTransporterNotice(TransporterNotice n) {
    transporterNoticeList.getValue().add(n);
    transporterNoticeList.setValue(transporterNoticeList.getValue()); // // Send update to observers
  }

  /**
   * clear the transporter list
   */
  public void clearTransporterNotices() {
    transporterNoticeList.getValue().clear();
    transporterNoticeList.setValue(transporterNoticeList.getValue()); // // Send update to observers
  }

  /**
   * clear the sender list
   */
  public void clearSenderNotices() {
    senderNoticeList.getValue().clear();
    senderNoticeList.setValue(senderNoticeList.getValue()); // // Send update to observers
  }

  /**
   * Get the entire list of senders.
   *
   * @return The list of senders.
   */
  public List<SenderNotice> getSenderNoticeList() { // TODO Return immutable collection?
    return senderNoticeList.getValue();
  }

  /**
   * Set the list of senders.
   *
   * @param notices The new list of sender notices.
   */
  public void setSenderNoticeList(List<SenderNotice> notices) {
    senderNoticeList.setValue(notices);
  }

  /**
   * Get the full list of transporters.
   *
   * @return The list of all the transporter notices.
   */
  public List<TransporterNotice> getTransporterNoticeList() {
    return transporterNoticeList.getValue();
  }

  /**
   * Set the internal representation of transporter notices to a new list.
   *
   * @param notices The new list of transporters.
   */
  public void setTransporterNoticeList(List<TransporterNotice> notices) {
    transporterNoticeList.setValue(notices);
  }

  /**
   * Observe changes of the internal representation of sender notices.
   *
   * @param observer The observer that will observe this list of senders.
   */
  public void observeSenderNotices(Observer<List<SenderNotice>> observer) {
    senderNoticeList.observeForever(observer);
  }

  /**
   * Observe changes of the internal representation of transporter notices.
   *
   * @param observer The observer that will observe this list of senders.
   */
  public void observeTransporterNotices(Observer<List<TransporterNotice>> observer) {
    transporterNoticeList.observeForever(observer);
  }
}
