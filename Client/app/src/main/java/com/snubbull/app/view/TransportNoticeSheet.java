package com.snubbull.app.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.snubbull.app.R;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.model.Utility;
import com.snubbull.app.model.Utility.MeansOfTransport;
import com.snubbull.app.repository.Connected;
import com.snubbull.app.repository.Ping;
import com.snubbull.app.repository.webservices.ServiceGenerator;
import com.snubbull.app.viewmodel.NoticeViewModel;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TransportNoticeSheet extends AppCompatActivity implements Connected, OnPlaceUpdatedListener {

  private final String TAG = TransportNoticeSheet.class.getSimpleName();
  private static final int PERMISSION_REQUEST_CODE = 1;
  private String wantPermission = Manifest.permission.READ_PHONE_STATE;
  private String number;

  String transporttype = "";
  private TransporterNotice transportnotice;
  private NoticeViewModel viewModel;
  private Button transporttypeEditText;
  private Button dateEditText;
  private EditText weightEditText;
  private EditText volumeEditText;
  private EditText priceEditText;
  private TextView phoneTextView;
  private Button addBtn;
  private String fromText="";
  private String toText="";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transport_notice_sheet);
    viewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);
    phoneTextView = findViewById(R.id.telephoneNumberId);
    
    if (!checkPermission(wantPermission)) {
      requestPermission(wantPermission);
    } else {
      setNumber();
    }

    //pingpong();

    transporttypeEditText = findViewById(R.id.inputfieldType);
    transporttypeEditText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(TransportNoticeSheet.this);
        View mView = getLayoutInflater().inflate(R.layout.type_of_transport, null);
        mBuilder.setView(mView);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            checkButton(mView);
          }
        });
        mBuilder.setTitle("Choose type of transport");
        mBuilder.show();
      }
    });

    dateEditText = findViewById(R.id.inputfieldDate);
    dateEditText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR) + 1900;

        DatePickerDialog dtp = new DatePickerDialog(TransportNoticeSheet.this
            , new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
            dateEditText.setText(
                "" + mYear + "-" + (mMonth / 10) + "" + ((1 + mMonth) % 10) + "-" + (mDay / 10) + ""
                    + (mDay % 10));
          }
        }, day, month, year);
        dtp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dtp.show();
      }
    });

    addBtn = findViewById(R.id.postBtn);
    addBtn.setOnClickListener(v -> {
      weightEditText = findViewById(R.id.inutfieldWeight);
      volumeEditText = findViewById(R.id.inputfieldVolume);
      priceEditText = findViewById(R.id.inputfieldPrice);

      try {
        String date = dateEditText.getText().toString();
        String phone = phoneTextView.getText().toString();
        float price = Float.parseFloat(priceEditText.getText().toString());

        String weight = weightEditText.getText().toString();
        String volume = volumeEditText.getText().toString();
        Utility.Capacity cap = Utility.Capacity.getSize(weight, volume);
        MeansOfTransport meansoftransport = MeansOfTransport.getMeansOfTransport(transporttype);

        transportnotice
            = new TransporterNotice(fromText, toText, phone, price, date, meansoftransport, cap);
        // viewModel.createTransporterNotice(transportnotice);
        viewModel.updateRepository();

        postTransporterNotice(transportnotice);

        Intent createAcceptedNotice = new Intent(getApplicationContext(), AcceptedNotice.class);
        startActivity(createAcceptedNotice);

        /*
        Intent postTransportNotice = new Intent(getApplicationContext(), NoticeView.class);
        postTransportNotice.putExtra("transporterNotice", transportnotice);
        //send notice object to the next view
        startActivity(postTransportNotice);*/

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
    if (ActivityCompat.checkSelfPermission(TransportNoticeSheet.this, wantPermission)
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
    if (ActivityCompat.shouldShowRequestPermissionRationale(TransportNoticeSheet.this, permission)) {
      Toast.makeText(TransportNoticeSheet.this,
          "Phone state permission allows us to get phone number. Please allow it for additional functionality.",
          Toast.LENGTH_LONG).show();
    }
    ActivityCompat.requestPermissions(TransportNoticeSheet.this, new String[]{permission},
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
          Toast.makeText(TransportNoticeSheet.this, "Permission Denied. We can't get phone number.",
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
      int result = ContextCompat.checkSelfPermission(TransportNoticeSheet.this, permission);
      return result == PackageManager.PERMISSION_GRANTED;
    } else {
      return true;
    }
  }


  public void checkButton(View v) {
    RadioGroup radiogroup = v.findViewById(R.id.radioGroupID);
    int id = radiogroup.getCheckedRadioButtonId();
    RadioButton radioButton = v.findViewById(id);
    transporttype = radioButton.getText() + "";
    transporttypeEditText.setText(transporttype);
  }

  /**
   *
   */

  private void postTransporterNotice(TransporterNotice transporterNotice) {
    Call<List<TransporterNotice>> call = ServiceGenerator.getInstance().getService()
        .createTransporterNotice(transporterNotice);
    call.enqueue(new Callback<List<TransporterNotice>>() {
      @Override
      public void onResponse(Call<List<TransporterNotice>> call,
          Response<List<TransporterNotice>> response) {
        if (response.isSuccessful()) {
          viewModel.clearTransporterNotices();
          for (TransporterNotice notice : response.body()) {
            viewModel.addTransporterNotice(transporterNotice);
          }
          // Intent postTransporterNotice = new Intent(getApplicationContext(), NoticeView.class);
          //  postTransporterNotice.putExtra("transporterNotice",transporterNotice);
          //  startActivity(postTransporterNotice);
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
