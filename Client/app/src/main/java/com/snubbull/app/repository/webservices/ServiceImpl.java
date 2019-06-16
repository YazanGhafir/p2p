package com.snubbull.app.repository.webservices;

import android.util.Log;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.repository.NoticeRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceImpl {

  private Boolean notified;
  private static final String TAG = ServiceGenerator.class.getSimpleName();
  private NoticeRepository noticeRepository;
  private ApiCalls service;

  private ServiceImpl(NoticeRepository noticeRepository, ApiCalls service) {
    this.noticeRepository = noticeRepository;
    this.service = service;
  }

  public ApiCalls getService() {
    return service;
  }

  /**
   * constructor
   */
  public ServiceImpl() {
    this.noticeRepository = NoticeRepository.getNoticeRepository();
    this.service = ServiceGenerator.getInstance().getService();
  }

  public Boolean getNotified() {
    return notified;
  }

  /**
   * This method sends an Http GET request to get a list of all sender notices and observe the
   * response using LiveData
   */
  public void updateRepository_SenderN() {
    Call<List<SenderNotice>> call = service
        .getSenderNotices();
    call.enqueue(new Callback<List<SenderNotice>>() {

      @Override
      public void onResponse(Call<List<SenderNotice>> call, Response<List<SenderNotice>> response) {
        if (response.isSuccessful()) {
          noticeRepository.clearSenderNotices();
          for (SenderNotice notice : response.body()) {
            noticeRepository.addSenderNotice(notice);
          }
          noticeRepository.invokeOrganization();
        } else {
          Log.e(TAG, response.message());
        }
      }

      @Override
      public void onFailure(Call<List<SenderNotice>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
      }
    });
  }

  /**
   * This method sends an Http GET request to get a list of all transporter notices and observe the
   * response using LiveData
   */
  public void updateRepository_TransporterN() {
    Call<List<TransporterNotice>> call = service
        .getTransporterNotices();
    call.enqueue(new Callback<List<TransporterNotice>>() {

      @Override
      public void onResponse(Call<List<TransporterNotice>> call,
          Response<List<TransporterNotice>> response) {
        if (response.isSuccessful()) {
          noticeRepository.clearTransporterNotices();
          for (TransporterNotice notice : response.body()) {
            noticeRepository.addTransporterNotice(notice);
          }
          noticeRepository.invokeOrganization();
        } else {
          Log.e(TAG, response.message());
        }
      }

      @Override
      public void onFailure(Call<List<TransporterNotice>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
      }
    });
  }


  /**
   * This method sends an Http POST request to save a sender notice entry and get an updated list of
   * all sender notices and observe the response using LiveData
   */
  public void postSenderNotice(SenderNotice senderNotice) {
    Call<List<SenderNotice>> call = service
        .createSenderNotice(senderNotice);
    call.enqueue(new Callback<List<SenderNotice>>() {

      @Override
      public void onResponse(Call<List<SenderNotice>> call, Response<List<SenderNotice>> response) {
        if (response.isSuccessful()) {
          noticeRepository.clearSenderNotices();
          for (SenderNotice notice : response.body()) {
            noticeRepository.addSenderNotice(notice);
          }
        } else {
          Log.e(TAG, response.message());
        }
      }

      @Override
      public void onFailure(Call<List<SenderNotice>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
      }
    });
  }


  /**
   * This method sends an Http POST request to save a transporter notice entry and get an updated
   * list of all transporter notices and observe the response using LiveData
   */
  public void postTransporterNotice(TransporterNotice transporterNotice) {
    Call<List<TransporterNotice>> call = service
        .createTransporterNotice(transporterNotice);
    call.enqueue(new Callback<List<TransporterNotice>>() {

      @Override
      public void onResponse(Call<List<TransporterNotice>> call,
          Response<List<TransporterNotice>> response) {
        if (response.isSuccessful()) {
          noticeRepository.clearTransporterNotices();
          for (TransporterNotice notice : response.body()) {
            noticeRepository.addTransporterNotice(notice);
          }
        } else {
          Log.e(TAG, response.message());
        }
      }

      @Override
      public void onFailure(Call<List<TransporterNotice>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
      }
    });
  }

  /**
   * This method sends an Http DELETE request to delete a sender notice and get an updated list of
   * all sender notices and observe the response using LiveData
   */
  public void deleteSenderNotice(UUID id)  {
    Call<List<SenderNotice>> call = service
        .deleteSenderNotice(id);
    call.enqueue(new Callback<List<SenderNotice>>() {

      @Override
      public void onResponse(Call<List<SenderNotice>> call, Response<List<SenderNotice>> response) {
        if (response.isSuccessful()) {
          noticeRepository.clearSenderNotices();
          for (SenderNotice notice : response.body()) {
            noticeRepository.addSenderNotice(notice);
          }
          notified = true;
        } else {
          Log.e(TAG, response.message());
        }
      }

      @Override
      public void onFailure(Call<List<SenderNotice>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
      }

    });
  }

  /**
   * This method sends an Http DELETE request to delete a transporter notice and get an updated list
   * of all transporter notices and observe the response using LiveData
   */
  public void deleteTransporterNotice(UUID id) {
    Call<List<TransporterNotice>> call = service
        .deleteTransporterNotice(id);
    call.enqueue(new Callback<List<TransporterNotice>>() {

      @Override
      public void onResponse(Call<List<TransporterNotice>> call,
          Response<List<TransporterNotice>> response) {
        if (response.isSuccessful()) {
          noticeRepository.clearTransporterNotices();
          for (TransporterNotice notice : response.body()) {
            noticeRepository.addTransporterNotice(notice);
          }
          notified = true;
        } else {
          Log.e(TAG, response.message());
        }
      }

      @Override
      public void onFailure(Call<List<TransporterNotice>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
      }
    });
  }

  /**
   * This method sends an Http PUT request to update a sender notice and get an updated list of all
   * sender notices and observe the response using LiveData
   */
  public void updateSenderNotice(SenderNotice senderNotice, UUID id) {
    Call<List<SenderNotice>> call = service
        .updateSenderNotice(senderNotice, id);
    call.enqueue(new Callback<List<SenderNotice>>() {

      @Override
      public void onResponse(Call<List<SenderNotice>> call, Response<List<SenderNotice>> response) {
        if (response.isSuccessful()) {
          noticeRepository.clearSenderNotices();
          for (SenderNotice notice : response.body()) {
            noticeRepository.addSenderNotice(notice);
          }
        } else {
          Log.e(TAG, response.message());
        }
      }

      @Override
      public void onFailure(Call<List<SenderNotice>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
      }
    });
  }

  /**
   * This method sends an Http PUT request to update a transporter notice and get an updated list of
   * all transporter notices and observe the response using LiveData
   */
  public void updateTransporterNotice(TransporterNotice transporterNotice, UUID id) {
    Call<List<TransporterNotice>> call = service
        .updateTransporterNotice(transporterNotice, id);
    call.enqueue(new Callback<List<TransporterNotice>>() {

      @Override
      public void onResponse(Call<List<TransporterNotice>> call,
          Response<List<TransporterNotice>> response) {
        if (response.isSuccessful()) {
          noticeRepository.clearTransporterNotices();
          for (TransporterNotice notice : response.body()) {
            noticeRepository.addTransporterNotice(notice);
          }
        } else {
          Log.e(TAG, response.message());
        }
      }

      @Override
      public void onFailure(Call<List<TransporterNotice>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
      }
    });
  }

  public void deleteAllNotices() {
    Call<Integer> call = service.deleteAllNotices();
    call.enqueue(new Callback<Integer>() {
      @Override
      public void onResponse(Call<Integer> call, Response<Integer> response) {
        if (response.isSuccessful()) {
          noticeRepository.clearTransporterNotices();
          noticeRepository.clearSenderNotices();
        } else {
          Log.e(TAG, response.message());
        }
      }

      @Override
      public void onFailure(Call<Integer> call, Throwable t) {
        Log.e(TAG, t.getMessage());
      }
    });
  }
}
