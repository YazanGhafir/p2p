package com.snubbull.app;

import android.util.Log;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.repository.NoticeRepository;
import com.snubbull.app.repository.webservices.ApiCalls;
import com.snubbull.app.repository.webservices.ServiceGenerator;
import com.snubbull.app.repository.webservices.ServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerTestUtils {

  private static final ServiceImpl serviceIml = new ServiceImpl();
  private static final ApiCalls calls = ServiceGenerator.getInstance().getService();
  private static final NoticeRepository noticeRepository = NoticeRepository.getNoticeRepository();
  private static final String TAG = ServiceGenerator.class.getSimpleName();

  /**
   * Clear all notices from the server.
   * @throws InterruptedException Sleeps this thread to await server
   */
  public static void clearAllNotices() throws InterruptedException {
    serviceIml.deleteAllNotices();
    TimeUnit.SECONDS.sleep(1);
  }

  /**
   * Posts a list of sender notices individually to the server.
   * @param senderNotices The list of sender notices to post.
   * @throws InterruptedException Sleeps this thread to await server (between each call)
   */
  public static void postSenderNotices(List<SenderNotice> senderNotices)
      throws InterruptedException {
    for (SenderNotice senderNotice : senderNotices) {
      serviceIml.postSenderNotice(senderNotice);
      TimeUnit.MILLISECONDS.sleep(200);
    }
  }


  /**
   * Posts a list of transporter notices individually to the server.
   * @param transporterNotices The list of transporter notices to post.
   * @throws InterruptedException Sleeps this thread to await server (between each call)
   */
  public static void postTransporterNotices(List<TransporterNotice> transporterNotices)
      throws InterruptedException {
    for (TransporterNotice transporterNotice : transporterNotices) {
      serviceIml.postTransporterNotice(transporterNotice);
      TimeUnit.MILLISECONDS.sleep(200);
    }
  }

  /**
   * Fetches all notices from server and updates the local repository
   * @throws InterruptedException Sleeps this thread to await server
   */
  public static void pullNotices() throws InterruptedException {
    calls.getSenderNotices().enqueue(new Callback<List<SenderNotice>>() {
      @Override
      public void onResponse(Call<List<SenderNotice>> call, Response<List<SenderNotice>> response) {
        if (response.isSuccessful()) {
          noticeRepository.clearSenderNotices();
          response.body().forEach(noticeRepository::addSenderNotice);
        }
      }

      @Override
      public void onFailure(Call<List<SenderNotice>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
      }
    });
    calls.getTransporterNotices().enqueue(new Callback<List<TransporterNotice>>() {
      @Override
      public void onResponse(Call<List<TransporterNotice>> call,
          Response<List<TransporterNotice>> response) {
        if (response.isSuccessful()) {
          noticeRepository.clearTransporterNotices();
          response.body().forEach(noticeRepository::addTransporterNotice);
        }
      }

      @Override
      public void onFailure(Call<List<TransporterNotice>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
      }
    });
    TimeUnit.SECONDS.sleep(1);
  }
}
