package com.snubbull.app;

import android.util.Log;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.model.Utility;
import com.snubbull.app.repository.NoticeRepository;
import com.snubbull.app.repository.webservices.ApiCalls;
import com.snubbull.app.repository.webservices.ServiceGenerator;
import com.snubbull.app.repository.webservices.ServiceImpl;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServiceImplTest {

  List<SenderNotice> senderNotices;
  List<TransporterNotice> transporterNotices;

  @Mock
  ApiCalls mockedService;

  @Mock
  NoticeRepository mockedNoticeRepository;

  @InjectMocks
  ServiceImpl srcImpl;

  @Before
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);

    senderNotices = new ArrayList<>();
    senderNotices.add(
        new SenderNotice("gbg", "stklm", "723451571", 50, "11-11-2019", 3, Utility.Capacity.SMALL));
    senderNotices.add(
        new SenderNotice("Uddevalla", "gbg", "721251572", 50, "6-6-2019", 3, Utility.Capacity.BIG));
    senderNotices.add(new SenderNotice("gbg", "Uddevalla", "723551573", 50, "7-7-2019", 3,
        Utility.Capacity.MEDIUM));
    senderNotices.add(new SenderNotice("Trolhattan", "Uddevalla", "723651574", 50, "8-9-2019", 3,
        Utility.Capacity.SMALL));
    senderNotices.add(
        new SenderNotice("Mlm", "stklm", "723751575", 50, "10-10-2019", 3, Utility.Capacity.BIG));

    transporterNotices = new ArrayList<>();
    transporterNotices.add(new TransporterNotice("gbg", "stklm", "777888999", 50, "11-11-2019",
        Utility.MeansOfTransport.CAR, Utility.Capacity.SMALL));
    transporterNotices.add(new TransporterNotice("Uddevalla", "gbg", "777888666", 50, "11-11-2019",
        Utility.MeansOfTransport.TRAIN, Utility.Capacity.BIG));
    transporterNotices.add(new TransporterNotice("gbg", "Uddevalla", "777888555", 50, "11-11-2019",
        Utility.MeansOfTransport.CAR, Utility.Capacity.SMALL));
    transporterNotices.add(
        new TransporterNotice("Trolhattan", "stklm", "777888444", 50, "11-11-2019",
            Utility.MeansOfTransport.CAR, Utility.Capacity.MEDIUM));
    transporterNotices.add(new TransporterNotice("Mlm", "stklm", "777888333", 50, "11-11-2019",
        Utility.MeansOfTransport.TRAIN, Utility.Capacity.SMALL));

  }

  @Test
  public void testUpdateRepository_SenderN() {

    Call<List<SenderNotice>> mockedCall = Mockito.mock(Call.class);
    ArgumentCaptor<SenderNotice> argumentCaptor = ArgumentCaptor.forClass(SenderNotice.class);

    doAnswer(invocation -> {
      Callback<List<SenderNotice>> callback = invocation.getArgument(0);
      callback.onResponse(mockedCall, Response.success(senderNotices));
      return null;
    }).when(mockedCall).enqueue(any(Callback.class));

    when(mockedService.getSenderNotices()).thenReturn(mockedCall);
    srcImpl.updateRepository_SenderN();
    verify(mockedNoticeRepository, times(5)).addSenderNotice(argumentCaptor.capture());

    assertEquals(senderNotices, argumentCaptor.getAllValues());
  }

  @Test
  public void testUpdateRepository_TransporterN() {

    Call<List<TransporterNotice>> mockedCall = Mockito.mock(Call.class);
    ArgumentCaptor<TransporterNotice> argumentCaptor = ArgumentCaptor
        .forClass(TransporterNotice.class);

    doAnswer(invocation -> {
      Callback<List<TransporterNotice>> callback = invocation.getArgument(0);
      callback.onResponse(mockedCall, Response.success(transporterNotices));
      return null;
    }).when(mockedCall).enqueue(any(Callback.class));

    when(mockedService.getTransporterNotices()).thenReturn(mockedCall);
    srcImpl.updateRepository_TransporterN();
    verify(mockedNoticeRepository, times(5)).addTransporterNotice(argumentCaptor.capture());

    assertEquals(transporterNotices, argumentCaptor.getAllValues());
  }


  @Test
  public void testPostSenderNotice() throws Exception {

    SenderNotice senderNotice = new SenderNotice("Bengtsfors", "stklm", "723751888", 40,
        "10-10-2019", 3, Utility.Capacity.BIG);

    Call<List<SenderNotice>> mockedCall = Mockito.mock(Call.class);
    ArgumentCaptor<SenderNotice> argumentCaptor = ArgumentCaptor.forClass(SenderNotice.class);

    doAnswer(invocation -> {
      Callback<List<SenderNotice>> callback = invocation.getArgument(0);
      senderNotices.add(senderNotice);
      callback.onResponse(mockedCall, Response.success(senderNotices));
      return null;
    }).when(mockedCall).enqueue(any(Callback.class));

    when(mockedService.createSenderNotice(any(SenderNotice.class))).thenReturn(mockedCall);
    srcImpl.postSenderNotice(senderNotice);
    verify(mockedNoticeRepository, times(6)).addSenderNotice(argumentCaptor.capture());

    assertEquals(senderNotices, argumentCaptor.getAllValues());
  }

  @Test
  public void testPostTransporterNotice() throws Exception {

    TransporterNotice transporterNotice = new TransporterNotice("Bengtsfors", "stklm", "723751888",
        40, "10-10-2019", Utility.MeansOfTransport.CAR, Utility.Capacity.BIG);

    Call<List<TransporterNotice>> mockedCall = Mockito.mock(Call.class);
    ArgumentCaptor<TransporterNotice> argumentCaptor = ArgumentCaptor
        .forClass(TransporterNotice.class);

    doAnswer(invocation -> {
      Callback<List<TransporterNotice>> callback = invocation.getArgument(0);
      transporterNotices.add(transporterNotice);
      callback.onResponse(mockedCall, Response.success(transporterNotices));
      return null;
    }).when(mockedCall).enqueue(any(Callback.class));

    when(mockedService.createTransporterNotice(any(TransporterNotice.class)))
        .thenReturn(mockedCall);
    srcImpl.postTransporterNotice(transporterNotice);
    verify(mockedNoticeRepository, times(6)).addTransporterNotice(argumentCaptor.capture());

    assertEquals(transporterNotices, argumentCaptor.getAllValues());
  }

  @Test
  public void testDeleteSenderNotice() throws Exception {

    SenderNotice senderNotice = new SenderNotice("Mlm", "stklm", "723751575", 50, "10-10-2019", 3,
        Utility.Capacity.BIG);
    UUID id = UUID.randomUUID();

    Call<List<SenderNotice>> mockedCall = Mockito.mock(Call.class);
    ArgumentCaptor<SenderNotice> argumentCaptor = ArgumentCaptor.forClass(SenderNotice.class);

    doAnswer(invocation -> {
      Callback<List<SenderNotice>> callback = invocation.getArgument(0);
      senderNotices.remove(senderNotice);
      callback.onResponse(mockedCall, Response.success(senderNotices));
      return null;
    }).when(mockedCall).enqueue(any(Callback.class));

    when(mockedService.deleteSenderNotice(any(UUID.class))).thenReturn(mockedCall);
    srcImpl.deleteSenderNotice(id);
    verify(mockedNoticeRepository, times(4)).addSenderNotice(argumentCaptor.capture());

    assertEquals(senderNotices, argumentCaptor.getAllValues());
  }

  @Test
  public void testDeleteTransporterNotice() throws Exception {

    TransporterNotice transporterNotice = new TransporterNotice("gbg", "stklm", "777888999", 50,
        "11-11-2019", Utility.MeansOfTransport.CAR, Utility.Capacity.SMALL);
    UUID id = UUID.randomUUID();

    Call<List<TransporterNotice>> mockedCall = Mockito.mock(Call.class);
    ArgumentCaptor<TransporterNotice> argumentCaptor = ArgumentCaptor
        .forClass(TransporterNotice.class);

    doAnswer(invocation -> {
      Callback<List<TransporterNotice>> callback = invocation.getArgument(0);
      transporterNotices.remove(transporterNotice);
      callback.onResponse(mockedCall, Response.success(transporterNotices));
      return null;
    }).when(mockedCall).enqueue(any(Callback.class));

    when(mockedService.deleteTransporterNotice(any(UUID.class))).thenReturn(mockedCall);
    srcImpl.deleteTransporterNotice(id);
    verify(mockedNoticeRepository, times(4)).addTransporterNotice(argumentCaptor.capture());

    assertEquals(transporterNotices, argumentCaptor.getAllValues());
  }

  @Test
  public void testUpdateSenderNotice() throws Exception {

    SenderNotice senderNotice = new SenderNotice("Mlm", "stklm", "723751575", 50, "10-10-2019", 3,
        Utility.Capacity.BIG);
    UUID id = UUID.randomUUID();

    Call<List<SenderNotice>> mockedCall = Mockito.mock(Call.class);
    ArgumentCaptor<SenderNotice> argumentCaptor = ArgumentCaptor.forClass(SenderNotice.class);

    doAnswer(invocation -> {
      Callback<List<SenderNotice>> callback = invocation.getArgument(0);
      for (SenderNotice sN : senderNotices) {
        if (senderNotice.equals(sN)) {
          sN.setPrice(30);
        }
      }
      callback.onResponse(mockedCall, Response.success(senderNotices));
      return null;
    }).when(mockedCall).enqueue(any(Callback.class));

    when(mockedService.updateSenderNotice(any(SenderNotice.class), any(UUID.class)))
        .thenReturn(mockedCall);
    srcImpl.updateSenderNotice(senderNotice, id);
    verify(mockedNoticeRepository, times(5)).addSenderNotice(argumentCaptor.capture());

    assertEquals(senderNotices, argumentCaptor.getAllValues());

    senderNotice.setPrice(30);
    for (SenderNotice sN : senderNotices) {
      if (senderNotice.equals(sN)) {
        assertEquals(senderNotice.getPrice(), sN.getPrice());
      }
    }
  }

  @Test
  public void testUpdateTransporterNotice() throws Exception {

    TransporterNotice transporterNotice = new TransporterNotice("gbg", "stklm", "777888999", 50,
        "11-11-2019", Utility.MeansOfTransport.CAR, Utility.Capacity.SMALL);
    UUID id = UUID.randomUUID();

    Call<List<TransporterNotice>> mockedCall = Mockito.mock(Call.class);
    ArgumentCaptor<TransporterNotice> argumentCaptor = ArgumentCaptor
        .forClass(TransporterNotice.class);

    doAnswer(invocation -> {
      Callback<List<TransporterNotice>> callback = invocation.getArgument(0);
      for (TransporterNotice tN : transporterNotices) {
        if (transporterNotice.equals(tN)) {
          tN.setPrice(30);
        }
      }
      callback.onResponse(mockedCall, Response.success(transporterNotices));
      return null;
    }).when(mockedCall).enqueue(any(Callback.class));

    when(mockedService.updateTransporterNotice(any(TransporterNotice.class), any(UUID.class)))
        .thenReturn(mockedCall);
    srcImpl.updateTransporterNotice(transporterNotice, id);
    verify(mockedNoticeRepository, times(5)).addTransporterNotice(argumentCaptor.capture());

    assertEquals(transporterNotices, argumentCaptor.getAllValues());

    transporterNotice.setPrice(30);
    for (TransporterNotice tN : transporterNotices) {
      if (transporterNotice.equals(tN)) {
        assertEquals(transporterNotice.getPrice(), tN.getPrice());
      }
    }
  }


}
