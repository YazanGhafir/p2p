package com.snubbull.app.repository;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.util.Log;

import com.snubbull.app.model.AbstractNotice;
import com.snubbull.app.model.AndroidID;
import com.snubbull.app.model.Notice;
import com.snubbull.app.model.NoticeManager;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.repository.Organizer.Order;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class NoticeRepository {

  private static NoticeRepository instance;

  private static NoticeManager noticeManager;
  private static MutableLiveData<List<TransporterNotice>> filteredTransporterNotices =
      new MutableLiveData<>();
  private static MutableLiveData<List<SenderNotice>> filteredSenderNotices =
      new MutableLiveData<>();
  private final String TAG = NoticeRepository.class.getSimpleName();
  private Organization organization;

  /**
   * This is a singleton utility class. Organization is set to the identity function, i.e. is set to
   * not modify which notices are displayed.
   */
  private NoticeRepository() {
    organization = new Organization() {
      @Override
      public <E extends SenderNotice> List<E> organizeSenderNotices(List<E> list) {
        return list;
      }

      @Override
      public <E extends TransporterNotice> List<E> organizeTransporterNotices(List<E> list) {
        return list;
      }
    }; //Identity
    noticeManager = new NoticeManager();
    noticeManager.observeTransporterNotices(this::updateTransporterNotices);
    noticeManager.observeSenderNotices(this::updateSenderNotices);
  }

  /**
   * Instantiate an object of this type and set a reference to it if none already exists.
   *
   * @return The single object instance.
   */
  public static NoticeRepository getNoticeRepository() {
    if (instance == null) {
      instance = new NoticeRepository();
    }
    return instance;
  }

  /**
   * Updates the filtered sender notices to the organizer's preferences based on the specified list
   * of sender notices.
   *
   * @param senderNotices The sender notices to update to (filtered and ordered by the
   * organization).
   */
  private void updateSenderNotices(List<SenderNotice> senderNotices) {
    List<SenderNotice> senderNoticesCopy = new ArrayList<>(senderNotices);
    filteredSenderNotices.setValue(organization.organizeSenderNotices(senderNoticesCopy));
  }

  /**
   * Updates the filtered transporter notices to the organizer's preferences based on the specified
   * list of transporter notices.
   *
   * @param transporterNotices The transporter notices to update to (filtered and ordered by the
   * organization).
   */
  private void updateTransporterNotices(List<TransporterNotice> transporterNotices) {
    List<TransporterNotice> transporterNoticesCopy = new ArrayList<>(transporterNotices);
    filteredTransporterNotices
        .setValue(organization.organizeTransporterNotices(transporterNoticesCopy));
  }

  /**
   * Retrieve the internal representation of stored notices.
   *
   * @return the internal representation of stored notices.
   */
  public List<SenderNotice> getSenderNotices() {
    updateSenderNoticesOrder();
    return noticeManager.getSenderNoticeList();
  }

  /**
   * Update the model representation of the notice list.
   *
   * @param notices The new list of notices.
   */
  public void setSenderNotices(List<SenderNotice> notices) {
    noticeManager.setSenderNoticeList(notices);
  }

  public List<TransporterNotice> getTransporterNotices() {
    updateTransporterNoticesOrder();
    return noticeManager.getTransporterNoticeList();
  }
  /**
   * Creates a list of all sender and transporter notices that match a given AndroidID.
   * @return a LinkedList of all sender and transporter notices that match a given AndroidID
   *
   */
  public List<Notice> getOwnNotices(){
      List<Notice> ownList = new LinkedList<>(noticeManager.getSenderNoticeList());
      ownList.addAll(noticeManager.getTransporterNoticeList());
      return Organizer.createOrganizer(ownList).filterByID(x->x, AndroidID.getAndroidID()).getElements();
  }

  public void setTransporterNotices(List<TransporterNotice> notices) {
    noticeManager.setTransporterNoticeList(notices);
  }

  /**
   * Update the sender notices. The notices will be sorted in descending order by price.
   */
  private void updateSenderNoticesOrder() {
    List<SenderNotice> sortedByPriceDesc = Organizer
        .createOrganizer(noticeManager.getSenderNoticeList())
        .orderByPrice(x -> x, Order.DESCENDING)
        .getElements();
    noticeManager.setSenderNoticeList(sortedByPriceDesc);
  }

  /**
   * Update the transporter notices. The notices will be sorted in ascending order by price.
   */
  private void updateTransporterNoticesOrder() {
    List<TransporterNotice> sortedByPriceAsc = Organizer
        .createOrganizer(noticeManager.getTransporterNoticeList())
        .orderByPrice(x -> x, Order.ASCENDING)
        .getElements();
    noticeManager.setTransporterNoticeList(sortedByPriceAsc);
  }

  public void addSenderNotice(SenderNotice notice) {
    noticeManager.addSenderNotice(notice);
    updateSenderNoticesOrder();
  }

  public void addTransporterNotice(TransporterNotice notice) {
    noticeManager.addTransporterNotice(notice);
    updateTransporterNoticesOrder();
  }

  /**
   * clear the transporter list
   */
  public void clearTransporterNotices() {
    noticeManager.clearTransporterNotices();
  }

  /**
   * clear the sender list
   */
  public void clearSenderNotices() {
    noticeManager.clearSenderNotices();
  }


  /**
   * Observe changes of the internal representation of sender notices.
   *
   * @param owner The lifecycle of the object that will observe.
   * @param observer The observer that will observe this the internal list sender notices.
   */
  public void observeSenderNotices(LifecycleOwner owner, Observer<List<SenderNotice>> observer) {
    filteredSenderNotices.observe(owner, observer);
    updateSenderNoticesOrder();
    invokeOrganization();
  }

  /**
   * Observe changes of the internal representation of transporter notice.
   *
   * @param owner The lifecycle of the object that will observe.
   * @param observer The observer that will observe this the internal list transporter notices.
   */
  public void observeTransporterNotices(LifecycleOwner owner,
      Observer<List<TransporterNotice>> observer) {
    filteredTransporterNotices.observe(owner, observer);
    updateTransporterNoticesOrder();
    invokeOrganization();
  }

  /**
   * Specify how notices shall be organized when updated.
   *
   * @param organization Specifies how notices shall be organized.
   */
  public void setOrganization(Organization organization) {
    this.organization = organization;
    updateSenderNotices(noticeManager.getSenderNoticeList());
    updateTransporterNotices(noticeManager.getTransporterNoticeList());
  }

  /**
   * Asks the NoticeManager to invoke its observers, and thereby update the NoticeRepository. This
   * will cause a re-organization.
   */
  public void invokeOrganization() {
    noticeManager.setSenderNoticeList(noticeManager.getSenderNoticeList());
    noticeManager.setTransporterNoticeList(noticeManager.getTransporterNoticeList());
  }

  /**
   * An interface specifying how a list of organizes shall be organized.
   */
  public interface Organization {

    /**
     * Organizes a list of sender notices and returns it.
     *
     * @param list The list of sender notices to be organized.
     * @param <E> The sender notice
     * @return The organized list of sender notices.
     */
    <E extends SenderNotice> List<E> organizeSenderNotices(List<E> list);

    /**
     * Organizes a list of transporter notices and returns it.
     *
     * @param list The list of transporter notices to be organized.
     * @param <E> The transporter notice
     * @return The organized list of transporter notices.
     */
    <E extends TransporterNotice> List<E> organizeTransporterNotices(List<E> list);
  }

}
