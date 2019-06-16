package com.snubbull.app.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import com.snubbull.app.model.AbstractNotice;
import com.snubbull.app.model.Notice;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.model.Utility.Capacity;
import com.snubbull.app.model.Utility.MeansOfTransport;
import com.snubbull.app.repository.NoticeRepository;
import com.snubbull.app.repository.NoticeRepository.Organization;
import com.snubbull.app.repository.Organizer;
import com.snubbull.app.repository.webservices.ApiCalls;
import com.snubbull.app.repository.webservices.ServiceImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class NoticeViewModel extends ViewModel {

  private static AbstractNotice filterNotice;
  private NoticeRepository noticeRepository = NoticeRepository.getNoticeRepository();
  private MeansOfTransport filterMeansOfTransport;
  private ServiceImpl service = new ServiceImpl();

  /**
   * Creates a new NoticeViewModel. Sets how notices shall be organized before retrieving them from
   * the repository.
   *
   * @throws Exception Tries to initiate an AbstractNotice to filter by.
   */
  public NoticeViewModel() throws Exception {
    if (filterNotice == null)
      filterNotice = new AbstractNotice(" ", " ", "701234567", 0, "2019-02-02", Capacity.MEDIUM) {
    };
    noticeRepository.setOrganization(
        new Organization() {
          /**
           * Organizes lists according to mutual preferences.
           */
          private <E extends Notice> Organizer<E> organizeNotices(List<E> list) {
            Organizer<E> organizer = Organizer
                .createOrganizer(list)
                .filterByRoute(x -> x, filterNotice.getFrom(), filterNotice.getTo());
            return organizer;
          }

          @Override
          public <E extends SenderNotice> List<E> organizeSenderNotices(List<E> list) {
            return organizeNotices(list).getElements();
          }

          @Override
          public <E extends TransporterNotice> List<E> organizeTransporterNotices(List<E> list) {
            Organizer<E> intermediate = organizeNotices(list);
            if (filterMeansOfTransport != null) {
              intermediate = intermediate
                  .filterByMeansOfTransport(x -> x, filterMeansOfTransport);
            }
            return intermediate.getElements();
          }
        }
    );
  }

  public List<SenderNotice> getSenderNotices() {
    return noticeRepository.getSenderNotices();
  }

  public List<TransporterNotice> getTransporterNotices() {
    return noticeRepository.getTransporterNotices();
  }

  public void addSenderNotice(SenderNotice notice) {
    noticeRepository.addSenderNotice(notice);
    filterNotice = notice;
  }

  public void addTransporterNotice(TransporterNotice notice) {
    noticeRepository.addTransporterNotice(notice);
    filterNotice = notice;
  }

  /**
   * clear all transporter notices
   */
  public void clearTransporterNotices() {
    noticeRepository.clearTransporterNotices();
  }

  /**
   * clear all sender notices
   */
  public void clearSenderNotices() {
    noticeRepository.clearSenderNotices();
  }

  /**
   * clear all notices
   */
  public void clearNotices() {
    clearSenderNotices();
    clearTransporterNotices();
  }

  /*
  public void addNotice(Notice notice) {
    noticeRepository.setNotice(notice);
   */

  /**
   * Observe changes of the internal representation of senders.
   *
   * @param owner The lifecycle of the object that will observe.
   * @param observer The observer that will observe the internal list of senders.
   */
  public void observeSenderNotices(LifecycleOwner owner, Observer<List<SenderNotice>> observer) {
    noticeRepository.observeSenderNotices(owner, observer);
  }

  /**
   * Observe changes of the internal representation of transporter notices.
   *
   * @param owner The lifecycle of the object that will observe.
   * @param observer The observer that will observe this the internal list transporter notices.
   */
  public void observeTransporterNotices(LifecycleOwner owner,
      Observer<List<TransporterNotice>> observer) {
    noticeRepository.observeTransporterNotices(owner, observer);
  }

  public void setMeansOfTransport(MeansOfTransport meansOfTransport) {
    filterMeansOfTransport = meansOfTransport;
    noticeRepository.invokeOrganization();
  }

  /**
   * update the local repository from the server
   */
  public void updateRepository() {
    service.updateRepository_SenderN();
    service.updateRepository_TransporterN();
  }

  /**
   * Insert sender notice entry on the server
   * @param senderNotice
   */
  public void createSenderNotice(SenderNotice senderNotice) {
    service.postSenderNotice(senderNotice);
  }

  /**
   * Insert transporter notice entry on the server
   * @param transporterNotice
   */
  public void createTransporterNotice(TransporterNotice transporterNotice) {
    service.postTransporterNotice(transporterNotice);
  }

  /**
   * Delete sender notice entry on the server
   * @param id
   */
  public void deleteSenderNotice(UUID id) {
    service.deleteSenderNotice(id);
  }

  /**
   * Delete transporter notice entry on the server
   * @param id
   */
  public void deleteTransporterNotice(UUID id) {
    service.updateRepository_TransporterN();
  }

  /**
   * edit sender notice
   * @param senderNotice
   * @param id
   */
  public void editSenderNotice(SenderNotice senderNotice, UUID id) {
    service.updateSenderNotice(senderNotice, id);
  }

  public <E extends AbstractNotice> void setFilterNotice(E notice) {
    filterNotice = notice;
    noticeRepository.invokeOrganization();
  }

  /**
   * edit transporter notice
   * @param transporterNotice
   * @param id
   */
  public void editTransporterNotice(TransporterNotice transporterNotice, UUID id) {
    service.updateTransporterNotice(transporterNotice, id);
  }

  /**
   * used to synchronize communications with the server
   * @return true if notified
   */
  public ApiCalls getService() {
    return service.getService();
  }

}
