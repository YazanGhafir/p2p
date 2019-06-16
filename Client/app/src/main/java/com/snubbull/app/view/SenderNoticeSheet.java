package com.snubbull.app.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.snubbull.app.R;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.Utility;
import com.snubbull.app.repository.Connected;
import com.snubbull.app.repository.Ping;
import com.snubbull.app.repository.webservices.ServiceGenerator;
import com.snubbull.app.viewmodel.NoticeViewModel;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO merge conflicts (Rest client)
/*
<<<<<<< HEAD
import com.snubbull.app.viewmodel.NoticeViewModel;
import ServiceGenerator;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SenderNoticeSheet extends AppCompatActivity {

    private final String TAG = SenderNoticeSheet.class.getSimpleName();
    private NoticeViewModel viewModel;
    private Notice senderNotice;
    private UUID id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_notice_sheet);
        viewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);

        */

public class SenderNoticeSheet extends AppCompatActivity implements Connected, OnPlaceUpdatedListener {

  private static final int PERMISSION_REQUEST_CODE = 1;
  private final String TAG = SenderNoticeSheet.class.getSimpleName();
  String wantPermission = Manifest.permission.READ_PHONE_STATE;
  String number;
  private SenderNotice sender;
  private NoticeViewModel viewModel;
  private String fromText = "";
  private String toText = "";
  private TextView phoneTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sender_notice_sheet);
    viewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);
    phoneTextView = findViewById(R.id.telephoneNumberId);

    if (!checkPermission(wantPermission)) {
      requestPermission(wantPermission);
    } else {
      setNumber();
    }
    //pingpong();

    Button btnDate = findViewById(R.id.inputfieldDate);
    btnDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR) + 1900;

        DatePickerDialog dtp = new DatePickerDialog(SenderNoticeSheet.this
            , new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
            btnDate.setText(
                "" + mYear + "-" + (mMonth / 10) + "" + ((1 + mMonth) % 10) + "-" + (mDay / 10) + ""
                    + (mDay % 10));
          }
        }, day, month, year);
        dtp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dtp.show();
      }
    });

    Button postButton = findViewById(R.id.postBtn);
    postButton.setOnClickListener(v -> {

      TextView quantityTextView = findViewById(R.id.inputfieldQuantity);
      TextView weigthTextView = findViewById(R.id.inutfieldWeight);
      TextView dimTextView = findViewById(R.id.inputfieldVolume);
      TextView priceTextView = findViewById(R.id.inputfieldPrice);
      TextView phoneTextView = findViewById(R.id.telephoneNumberId);

      try {
        String phone = phoneTextView.getText().toString();
        float priceText = Float.parseFloat(priceTextView.getText().toString());
        String dateText = btnDate.getText().toString();
        int quantityText = Integer.parseInt(quantityTextView.getText().toString());
        String dimText = dimTextView.getText().toString();
        String weightText = weigthTextView.getText().toString();
        Utility.Capacity capText = Utility.Capacity.getSize(dimText, weightText);

        sender = new SenderNotice(fromText, toText, phone, priceText, dateText, quantityText,
            capText);
        // viewModel.createSenderNotice(sender);
        viewModel.updateRepository();
        postSenderNotice(sender);

        Intent createAcceptedNotice = new Intent(getApplicationContext(), AcceptedNotice.class);
        startActivity(createAcceptedNotice);
                /*
                senderNotice = new Notice(titleText,discText,fromText,toText);

                Intent postSenderNotice = new Intent(getApplicationContext(), NoticeView.class);
                postSenderNotice.putExtra("senderNotice",senderNotice);
                //send notice object to the next view
                startActivity(postSenderNotice);



                sender = new SenderNotice(fromText, toText, phone, priceText, dateText,
                        quantityText, capText);

                Intent postSenderNotice = new Intent(getApplicationContext(), NoticeView.class);
                postSenderNotice.putExtra("senderNotice", sender);
                //send notice object to the next view
                startActivity(postSenderNotice);
                */
      } catch (Exception e) {
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
      }

    });
  }

  /*
  removes the following characters: spece, +46, + from the string
   */
  private void setNumber() {
    number = getPhone().replace(" ", "").replace("+46", "").replace("+", "");
    phoneTextView.setText(number);
  }

  private String getPhone() {
    TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    if (ActivityCompat.checkSelfPermission(SenderNoticeSheet.this, wantPermission)
        != PackageManager.PERMISSION_GRANTED) {
      return "";
    }
    return phoneMgr.getLine1Number();
  }

  /*
  Evaluate whether we should show permission rationale UI and if so, show information text.
  Lastly, the function calls requestPermissions() which prompt the user for the permission that is needed.
   */
  private void requestPermission(String permission) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(SenderNoticeSheet.this, permission)) {
      Toast.makeText(SenderNoticeSheet.this,
          "Phone state permission allows us to get phone number. Please allow it for additional functionality.",
          Toast.LENGTH_LONG).show();
    }
    ActivityCompat.requestPermissions(SenderNoticeSheet.this, new String[]{permission},
        PERMISSION_REQUEST_CODE);

  }

  /*
  Callback for the result from requesting permissions.
  This method is invoked for every call on requestPermissions(android.app.Activity, String[], int).
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_REQUEST_CODE:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          setNumber();
        } else {
          Toast.makeText(SenderNoticeSheet.this, "Permission Denied. We can't get phone number.",
              Toast.LENGTH_LONG).show();
        }
        break;
    }
  }

  /*
  Determine whether you have been granted READ_PHONE_STATE permission
   */
  private boolean checkPermission(String permission) {
    if (Build.VERSION.SDK_INT >= 23) {
      int result = ContextCompat.checkSelfPermission(SenderNoticeSheet.this, permission);
      return result == PackageManager.PERMISSION_GRANTED;
    } else {
      return true;
    }
  }

  private void postSenderNotice(SenderNotice senderNotice) {
    Call<List<SenderNotice>> call = ServiceGenerator.getInstance().getService()
        .createSenderNotice(senderNotice);
    call.enqueue(new Callback<List<SenderNotice>>() {
      @Override
      public void onResponse(Call<List<SenderNotice>> call, Response<List<SenderNotice>> response) {
        if (response.isSuccessful()) {
          viewModel.clearSenderNotices();
          for (SenderNotice notice : response.body()) {
            viewModel.addSenderNotice(notice);
          }
          //  Intent postSenderNotice = new Intent(getApplicationContext(), NoticeView.class);
          // postSenderNotice.putExtra("senderNotice", senderNotice);
          //  startActivity(postSenderNotice);
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
  @Override
  public void onFromUpdated(String newFrom) {
    fromText = newFrom;
  }

  @Override
  public void onToUpdated(String newTo) {
    toText = newTo;
  }

  @Override
  public void pingpong() {
    Ping.checkConnection(getApplicationContext(), Toast.LENGTH_LONG);
  }
}